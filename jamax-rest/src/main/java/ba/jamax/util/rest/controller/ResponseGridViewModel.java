package ba.jamax.util.rest.controller;

import java.io.Serializable;
import java.util.List;

public class ResponseGridViewModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<T> gridData;
	private int currPage;
	private int totalPages;
	private int totalRecords;

	public ResponseGridViewModel() {

	}
	public ResponseGridViewModel(List<T> gridData, int currPage, int totalPages, int totalRecords) {
		this.gridData = gridData;
		this.currPage = currPage;
		this.totalPages = totalPages;
		this.totalRecords = totalRecords;
	}
	public List<T> getGridData() {
		return gridData;
	}
	public void setGridData(List<T> gridData) {
		this.gridData = gridData;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
}