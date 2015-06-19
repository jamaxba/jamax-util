package ba.jamax.util.rest.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ba.jamax.util.rest.model.Filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties({"nd", "$edit", "$newRow"})
public class RequestGridViewModel implements Serializable {	
	private static Logger logger = LoggerFactory.getLogger(RequestGridViewModel.class);
	private static final long serialVersionUID = 5679777077467867776L;

	//@JsonIgnore
	private Filter filter;
	@JsonIgnore
	private final ObjectMapper mapper = new ObjectMapper();
	private Map<String,Object> criteria = new HashMap<String,Object>();
	private boolean search;
	private int page;
	private int rows;
	private String order;
	private String sort;
	private String filters;
	private String searchField;
	private String searchOper;
	private String searchString;

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("RequestGridViewModel [");
		if (criteria != null)
			builder.append("criteria=")
					.append(toString(criteria.entrySet(), maxLen)).append(", ");
		builder.append("search=").append(search).append(", page=").append(page)
				.append(", rows=").append(rows).append(", ");
		if (order != null)
			builder.append("order=").append(order).append(", ");
		if (sort != null)
			builder.append("sort=").append(sort).append(", ");
		if (filters != null)
			builder.append("filters=").append(filters).append(", ");
		if (searchField != null)
			builder.append("searchField=").append(searchField).append(", ");
		if (searchOper != null)
			builder.append("searchOper=").append(searchOper).append(", ");
		if (searchString != null)
			builder.append("searchString=").append(searchString).append(", ");
		if (filter != null)
			builder.append("filter=").append(filter);
		builder.append("]");
		return builder.toString();
	}
	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	private void addFiltersToCriteria(String filters) {
		if(filters != null && !filters.isEmpty()) {		
			try {
				this.filter = mapper.readValue(filters, Filter.class);
			} catch (JsonParseException e) {
				logger.error("Could not parse json data",e);
			} catch (JsonMappingException e) {
				logger.error("Could not map json data",e);
			} catch (IOException e) {
				logger.error("Input-output error",e);
			}
		}
	}	
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}		
	public String getFilters() {
		return filters;
	}	
	public void setFilters(String filters) {
		this.filters = filters;
		addFiltersToCriteria(filters);
	}
	public Map<String,Object> getCriteria() {
		return criteria;
	}
	public void setCriteria(Map<String,Object> criteria) {
		this.criteria = criteria;
	}
	public boolean getSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getSearchOper() {
		return searchOper;
	}
	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}