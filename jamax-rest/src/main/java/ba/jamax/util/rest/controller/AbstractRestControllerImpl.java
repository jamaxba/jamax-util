package ba.jamax.util.rest.controller;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import ba.jamax.util.rest.controller.model.RequestGridViewModel;
import ba.jamax.util.rest.controller.model.ResponseGridViewModel;
import ba.jamax.util.rest.model.BaseEntity;
import ba.jamax.util.rest.service.GenericService;
import ba.jamax.util.rest.util.GenericUtils;
import ba.jamax.util.rest.util.TypeUtils;

public abstract class AbstractRestControllerImpl<T extends BaseEntity> implements AbstractRestController<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestControllerImpl.class);

	@Autowired
	private ApplicationContext context;
	@Autowired
	private LocaleResolver localeResolver;

	private GenericService<T> service;
	private GenericUtils<T> genericUtils = new GenericUtils<T>();
	private TypeUtils typeUtils = new TypeUtils();
	protected abstract Class<T> getPersistentClass();

	public GenericService<T> getService() {
		return this.service;
	}	
	public void setService(GenericService<T> service) {
		this.service = service;
	}
	public ApplicationContext getContext() {
		return this.context;
	}
	public LocaleResolver getLocaleResolver() {
		return this.localeResolver;
	}
	@Override
	@RequestMapping(value = "/index", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseGridViewModel<T> index(
			@RequestBody final RequestGridViewModel gridViewModel,
			final HttpServletRequest request) {
        LOGGER.debug("gridViewModel[{}]", new Object[]{gridViewModel});
		prepareGridViewModel(gridViewModel, request);
		return getFilteredEntity(gridViewModel);
	}
	@Override
	@RequestMapping(value = "/insert", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> doInsert(@RequestBody final T t,
			final HttpServletRequest request) {
        LOGGER.debug("t[{}]", new Object[]{t});
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			getService().addNew(t);
			responseMap.put("object", t);
		} catch (Exception e) {
			String localizedMessage = this.context.getMessage(
					"General.list.message.error.add.failed", null,
					this.localeResolver.resolveLocale(request));
			responseMap.put("error", new String[] { localizedMessage });
		}
		return responseMap;
	}
	@Override
	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> doUpdate(@RequestBody final T t,
			final HttpServletRequest request) {
        LOGGER.debug("t[{}]", new Object[]{t});
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			getService().update(t);
		} catch (Exception e) {
			String localizedMessage = this.context.getMessage(
					"General.list.message.error.update.failed", null,
					this.localeResolver.resolveLocale(request));
			responseMap.put("error", new String[] { localizedMessage });
		}
		return responseMap;
	}
	@Override
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> doDelete(
			@RequestParam("id") final Serializable entityId,
			final HttpServletRequest request) {
        LOGGER.debug("entityId[{}]", new Object[]{entityId});
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			T t = getService().findById(typeUtils.getCorrectObjectType(getGetterMethod("id"),entityId));
			if (t != null) {
				getService().delete(t);
			}
		} catch (Exception e) {
			String localizedMessage = this.context.getMessage(
					"General.list.message.error.delete.failed", null,
					this.localeResolver.resolveLocale(request));
			responseMap.put("error", new String[] { localizedMessage });
		}
		return responseMap;
	}
	@Override
	@RequestMapping(value = { "/updatePreferedGridRowNum" }, method = { RequestMethod.POST })
	@ResponseBody
	public String updatePreferedGridRowNum(@RequestParam final Integer newRowNum,
			final HttpServletRequest request) {
        LOGGER.debug("newRowNum[{}]", new Object[]{newRowNum});
		HttpSession session = request.getSession();
		session.setAttribute("gridRowNum", newRowNum);
		return null;
	}
	public ResponseGridViewModel<T> getFilteredEntity(RequestGridViewModel gridViewModel) {
		Map<String, Object> criteriaMap = gridViewModel.getCriteria();
		int page = gridViewModel.getPage();
		int rows = gridViewModel.getRows();
		// criterias
		if (gridViewModel.getSearch() && !gridViewModel.getCriteria().isEmpty()) {
			criteriaMap.putAll(gridViewModel.getCriteria());
		}
		/*
		 * if (!gridViewModel.getCriteria().containsKey("active")) {
		 * criteriaMap.put("active", Boolean.TRUE); }
		 */

		// ordering
		String sort = (StringUtils.isEmpty(gridViewModel.getSort())) ? "id"
				: gridViewModel.getSort();
		Order order = ("desc".equals(gridViewModel.getOrder())) ? Order
				.desc(sort) : Order.asc(sort);

		/*List<T> records = getService().findByCriteria(criteriaMap, 
				false, (page - 1) * rows, rows, order);
		int totalRecords = getService().countByCriteria(criteriaMap, false);*/
		List<T> records = getService().findByCriteria(criteriaMap, 
				gridViewModel.getFilter(), false,
				(page - 1) * rows, rows, order);
		int totalRecords = getService().countByCriteria(criteriaMap, 
				gridViewModel.getFilter(), false);
		int totalPages = totalRecords / rows;
		if (totalRecords % rows > 0) {
			totalPages += 1;
		}
		ResponseGridViewModel<T> model = new ResponseGridViewModel<T>(records,
				page, totalPages, totalRecords);
		return model;
	}
	protected void prepareGridViewModel(
			final RequestGridViewModel gridViewModel,
			final HttpServletRequest request) {
		if (!gridViewModel.getCriteria().isEmpty()) {
			Map<String, Object> criteriaMap = new HashMap<String, Object>();
			for (String key : gridViewModel.getCriteria().keySet()) {
				Object value = gridViewModel.getCriteria().get(key);
				value = typeUtils.getCorrectObjectType(getGetterMethod(key), (Serializable) value);
				criteriaMap.put(key, value);
			}
			gridViewModel.setCriteria(criteriaMap);
		}
	}
	protected Method getGetterMethod(String key) {
		Method m = this.genericUtils.getGetter(getPersistentClass(), key);
		if(key != null && key.contains(".")) {
			// we have relation: entity.someOtherEntity.moreEntity.id
			String[] entities = key.split("\\.");
			if(entities != null && entities.length > 1) {
				m = getChainedGetterMethod(getPersistentClass(), entities, 0);
			}
		}
		return m;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Method getChainedGetterMethod(Class type, String[] entities, int counter) {
		Method method = this.genericUtils.getGetter(type, entities[counter]);
		counter++;
		if(counter >= entities.length) {
			return method;
		}
		return getChainedGetterMethod(method.getReturnType(), entities, counter);
	}
}