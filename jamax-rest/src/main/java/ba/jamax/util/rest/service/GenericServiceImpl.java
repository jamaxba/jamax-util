package ba.jamax.util.rest.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import ba.jamax.util.rest.dao.GenericDAO;
import ba.jamax.util.rest.model.BaseEntity;
import ba.jamax.util.rest.model.Filter;

public abstract class GenericServiceImpl<T extends BaseEntity> implements GenericService<T> {
    
	private GenericDAO<T> dao;

	public GenericDAO<T> getDAO() {
		return dao;
	}
	public void setDAO(GenericDAO<T> dao) {
		this.dao = dao;
	}
	@Transactional(readOnly = true)
	public List<T> findByCriteria(Map<String, Object> criterias, Filter filter, boolean strict,
			int firstResult, int maxResults, Order order) {
		return this.dao.findByCriteria(criterias, filter, strict, firstResult, maxResults, order);
	}
	@Transactional(readOnly = true)
	public int countByCriteria(Map<String, Object> criteriaMap, Filter filter, boolean strict) {
		return this.dao.countByCriteria(criteriaMap, filter, strict);
	}
	@Transactional(readOnly = true)
	public T findById(Serializable id) {
		return this.dao.findById(id);
	}
	@Transactional
	public T addNew(T t) {
		return this.dao.addNew(t);
	}
	@Transactional
	public Collection<T> addAll(Collection<T> tList){
		return this.dao.addAll(tList);
	}
	@Transactional
	public void delete(T t) {
		this.dao.delete(t);
	}
	@Transactional
	public void update(T t) {
		this.dao.update(t);
	}
}