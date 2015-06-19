package ba.jamax.util.rest.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import ba.jamax.util.rest.model.BaseEntity;
import ba.jamax.util.rest.model.Filter;

public interface GenericDAO<T extends BaseEntity> {
    List<T> findByCriteria(Map<String, Object> criterias, final Filter filter, boolean strict, int firstResult, int maxResults, Order order);
    int countByCriteria(Map<String, Object> criteriaMap, final Filter filter, boolean strict);

    T findById(Serializable id);
    T addNew(T t);
    Collection<T> addAll(Collection<T> tList);
	void delete(T t);	
	void update(T t);
}
