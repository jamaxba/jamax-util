package ba.jamax.util.rest.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ba.jamax.util.rest.model.BaseEntity;
import ba.jamax.util.rest.model.Filter;
import ba.jamax.util.rest.model.FilterRule;
import ba.jamax.util.rest.model.Rule;
import ba.jamax.util.rest.service.GenericServiceImpl;
import ba.jamax.util.rest.util.GenericUtils;
import ba.jamax.util.rest.util.TypeUtils;

public abstract class GenericDAOImpl<T extends BaseEntity> implements GenericDAO<T> {
	
    private final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);    
    private static final String UNKNOWN = "Unknown";
    private final Map<String, String> aliasMap = new HashMap<String, String>();
    
    @Autowired
    private SessionFactory sessionFactory;

	private Class<T> entityClass;
	private GenericUtils<T> utils = new GenericUtils<T>();
	private TypeUtils typeUtils = new TypeUtils();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}	
	@Autowired
	public void init(SessionFactory factory) {
	    setSessionFactory(factory);
	}
	@SuppressWarnings("unchecked")
	public T findById(Serializable id) {
		return (T) getSessionFactory().getCurrentSession().get(entityClass, id);
	}
	public T addNew(T t) {
		t.setCreated(new Date());
		t.setCreatedBy(getUsernameInSession());
		t.setModified(new Date());
		t.setModifiedBy(getUsernameInSession());
		getSessionFactory().getCurrentSession().saveOrUpdate(t);
		return t;
	}
	public Collection<T> addAll(Collection<T> tList){
		Date currentDate = new Date();
		String currentUser = getUsernameInSession();
		for (T t : tList) {
			t.setCreated(currentDate);
			t.setCreatedBy(currentUser);
			getSessionFactory().getCurrentSession().saveOrUpdate(t);
		}
		return tList;
	}
	public void delete(T t){
		getSessionFactory().getCurrentSession().delete(t);
	}
	public void update(T t){
		t.setModified(new Date());
		t.setModifiedBy(getUsernameInSession());
		getSessionFactory().getCurrentSession().update(t);
	}
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(final Map<String, Object> criterias, 
			final Filter filter, final boolean strict,
			final int firstResult, final int maxResults, final Order order) {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(entityClass);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);
			createCriteria(criterias, filter, strict, criteria, order);

			return criteria.list();
	}
    @SuppressWarnings("unchecked")
	public int countByCriteria(final Map<String, Object> criteriaMap, 
    		final Filter filter, final boolean strict) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(entityClass);
        createCriteria(criteriaMap, filter, strict, criteria, null);
        criteria.setProjection(Projections.rowCount());
        List<Number> counts = criteria.list();
        return getSumOfCounts(counts);
    }
	private int getSumOfCounts(final List<Number> counts) {
		int totalCounts = 0;
		if(counts != null && counts.size() > 0) {
			for(Number count : counts) {
				totalCounts += count.intValue();
			}
		}
		return totalCounts;
	}
		
	private void addAliasesToCriteria(final String key, final Criteria criteria) {
		if (key != null && key.contains(".")) {
			if (key.contains(",")) {
				String[] keys = key.split(",");
				for (String k : keys) {
					addAliasesToCriteria(k, criteria);
				}
			} else {
				// we have relation: entity.someOtherEntity.moreEntity.id
				String[] entities = key.split("\\.");
				if (entities != null && entities.length > 1) {
					// create the first one without the dot
					createAlias(entities[0], entities[0], criteria);
					createAliasesRecursive(criteria, entities, 0);
				}
			}
		}
	}
	private void createAliasesRecursive(final Criteria criteria, String[] entities, int counter) {
		if((counter+2) >= entities.length) {
			return;
		}
		counter++;
		createAlias(entities[counter]+"."+entities[counter+1], entities[counter+1], criteria);
		createAliasesRecursive(criteria, entities, counter);
	}
	private void createAlias(final String key, final String value, final Criteria criteria) {
		String aValue = aliasMap.get(key);
		if(aValue == null) {
			// add only if it doesnt exist
			criteria.createAlias(key, value); // inner join by default
			aliasMap.put(key, value);
		}
	}
	private void addRestrictionsToCriteria(final Map<String, Object> criterias, 
				final boolean strict, final Criteria criteria) {
		for (Entry<String, Object> entry : criterias.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			addAliasesToCriteria(key, criteria);
			if (value instanceof String) {
                if (strict) {
                    criteria.add(Restrictions.eq(key, value));
                } else {
                    criteria.add(Restrictions.ilike(key, (String) value, MatchMode.ANYWHERE));
                }
			} else {
				criteria.add(Restrictions.eq(key, value));
			}
		}		
	}
	protected void createCriteria(final Map<String, Object> criterias, 
			final Filter filter, final boolean strict,
			final Criteria criteria, final Order order) {
		aliasMap.clear();
		if(order != null) {
			addAliasesToCriteria(order.getPropertyName(), criteria);
		}
		addRestrictionsToCriteria(criterias, strict, criteria);
		addFiltersToCriteria(filter, criteria);
		if (order != null) {
			if (order.getPropertyName().contains(",")) {
				String[] keys = order.getPropertyName().split(",");
				for (String k : keys) {
					criteria.addOrder(Order.asc(k.trim()));
				}
			} else {
				criteria.addOrder(order);
			}
		}
	}
	private void addFiltersToCriteria(final Filter filter, final Criteria criteria) {
		// "filter": "{"groupOp":"AND","rules":[
		//						"rule":{"field":"location","op":"cn","data":"wi"},
		// 					   	"rule":{"field":"maker","op":"cn","data":"test"},
		//						"filter":{"groupOp":"AND","rules":[{"field":"location","op":"cn","data":"wi"},
		// 					   							{"field":"maker","op":"cn","data":"test"}]}
		// 					]}"
		if(filter!=null) {
			List<? extends FilterRule> rules = filter.getRules();
			String groupOp = filter.getGroupOp();
			Criterion criterion = null;
			String data, field;
			Object dataObj = null;
			for (FilterRule filterRule : rules) {
				try {
					if(filterRule instanceof Rule) {
						Rule rule = (Rule) filterRule;
						data = rule.getData();
						field = rule.getField();
						Method m = this.utils.getGetter(this.entityClass, field);
						dataObj = typeUtils.getCorrectObjectType(m, data);						
						if(criterion == null) {
							criterion = decodeOp(rule.getOp(),field,dataObj);
						} else if(groupOp.equals("AND")) {
							criterion = Restrictions.and(criterion, decodeOp(rule.getOp(),field,dataObj));
						} else if(groupOp.equals("OR")) {
							criterion = Restrictions.or(criterion, decodeOp(rule.getOp(),field,dataObj));
						}
					} else if(filterRule instanceof Filter) {
						Filter filter2 = (Filter) filterRule;
						addFiltersToCriteria(filter2, criteria);
					}
				} catch (Exception e) {
					logger.error("Could not create criterion",e);
				}
			}
			if(criterion != null) {
				criteria.add(criterion);
			}
		}
	}
	protected String getUsernameInSession() {
		String username = UNKNOWN;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			username = auth.getName(); //get logged in username
		} catch (Exception e) {
			logger.info("Could not get username");
		}
		return username;
	}	
	/**
	 * All available option are: 
	 * 'eq' => 'equal'
	 * 'ne' => 'not equal'
	 * 'lt' => 'less'
	 * 'le' => 'less or equal'
	 * 'gt' => 'greater'
	 * 'ge' => 'greater or equal'
	 * 'bw' => 'begins with'
	 * 'bn' => 'does not begin with'
	 * 'in' => 'is in'
	 * 'ni' => 'is not in'
	 * 'ew' => 'ends with'
	 * 'en' => 'does not end with'
	 * 'cn' => 'contains'
	 * 'nc' => 'does not contain'
	 * 'nu' => 'is null'
	 * 'nn' => 'not null'
	 * Note that the elements in sopt array can be mixed in any order.
	 * @param op
	 * @return
	 * @throws Exception 
	 */
	private Criterion decodeOp(final String op, final String propertyName, 
			final Object value) throws Exception {
		if(op.equals("eq")) {
			return Restrictions.eq(propertyName, value);
		} else if(op.equals("ne")) {
			return Restrictions.ne(propertyName, value);
		} else if(op.equals("lt")) {
			return Restrictions.lt(propertyName, value);
		} else if(op.equals("le")) {
			return Restrictions.le(propertyName, value);
		} else if(op.equals("gt")) {
			return Restrictions.gt(propertyName, value);
		} else if(op.equals("ge")) {
			return Restrictions.ge(propertyName, value);
		} else if(op.equals("bw")) {
			return Restrictions.ilike(propertyName, value.toString(), MatchMode.START);
		} else if(op.equals("bn")) {
			return Restrictions.not(Restrictions.ilike(propertyName, value.toString(), MatchMode.START));
		} else if(op.equals("in")) {
			return Restrictions.in(propertyName, value.toString().split(" "));
		} else if(op.equals("ni")) {
			return Restrictions.not(Restrictions.in(propertyName, value.toString().split(" ")));
		} else if(op.equals("ew")) {
			return Restrictions.ilike(propertyName, value.toString(), MatchMode.END);
		} else if(op.equals("en")) {
			return Restrictions.not(Restrictions.ilike(propertyName, value.toString(), MatchMode.END));
		} else if(op.equals("cn")) {
			return Restrictions.ilike(propertyName, value.toString(), MatchMode.ANYWHERE);
		} else if(op.equals("nc")) {
			return Restrictions.not(Restrictions.ilike(propertyName, value.toString(), MatchMode.ANYWHERE));
		} else if(op.equals("nu")) {
			return Restrictions.isNull(propertyName);
		} else if(op.equals("nn")) {
			return Restrictions.isNotNull(propertyName);
		}
		throw new Exception("Unknown op: \"" + op + "\"");
	}
}