package ba.jamax.util.rest.controller;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ba.jamax.util.rest.controller.model.RequestGridViewModel;
import ba.jamax.util.rest.controller.model.ResponseGridViewModel;
import ba.jamax.util.rest.model.BaseEntity;

public interface AbstractRestController<T extends BaseEntity> {
	ResponseGridViewModel<T> index(final RequestGridViewModel gridViewModel,final HttpServletRequest request);
	Map<String, ? extends Object> doInsert(final T t,final HttpServletRequest request);
	Map<String, ? extends Object> doUpdate(final T t,final HttpServletRequest request);
	Map<String, ? extends Object> doDelete(final Serializable entityId,final HttpServletRequest request);
	String updatePreferedGridRowNum(final Integer newRowNum,final HttpServletRequest request);
}