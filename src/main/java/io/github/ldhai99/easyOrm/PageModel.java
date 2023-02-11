package io.github.ldhai99.easyOrm;

import io.github.ldhai99.easyOrm.Dialect.Dialect;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PageModel {

	private int currentPage = 1; // 当前页
	private int totalPages = 0; // 总页数


	private int pageRecorders = 10;// 每页5条数据
	private int totalRows = 0; // 总数据数
	private int pageStartRow = 0;;// 每页的起始数
	private int pageEndRow = 0;; // 每页显示数据的终止数
	private boolean hasNextPage = false; // 是否有下一页
	private boolean hasPreviousPage = false; // 是否有前一页
	private int nextPage = 0;// 下一页的页码

	private int previousPage = 0;// 上一页的页码

	List <Map<String, Object>> pageData;
	Dialect dialect;

	// 构造函数
	public PageModel(){

	}


	// 获取数据

	public PageModel getData() throws SQLException {
		totalRows = dialect.getRowCount();
		toPage();

		dialect.setPage(this);
		pageData=dialect.getPageData();
		return  this;
	}

	public void toPage() {

		//计算总页数
		if ((totalRows % pageRecorders) == 0) {
			totalPages = (int) (totalRows / pageRecorders);
		} else {
			totalPages = (int) (totalRows / pageRecorders + 1);
		}

		//计算当前页
		if (currentPage < totalPages) {
			pageStartRow = ((currentPage - 1) * pageRecorders);
			this.pageEndRow = pageStartRow + pageRecorders;

			hasNextPage = true;
			nextPage = currentPage + 1;
		} else if (currentPage == totalPages) {
			pageStartRow = ((currentPage - 1) * pageRecorders);
			this.pageEndRow = totalRows;

			hasNextPage = false;
			nextPage = currentPage;
		}

		//计算前一页
		if (currentPage < 2) {
			previousPage = currentPage;
			hasPreviousPage = false;
		} else if (currentPage > 1) {
			previousPage = currentPage - 1;
			hasPreviousPage = true;
		}
	}
	//设置每页行数
	public PageModel setPageRecorders(int pageRecorders) {

		this.pageRecorders = pageRecorders;
		return  this;
	}

	//设置当前页
	public PageModel setCurrentPage(String pages) {

		if (pages == null || pages.equals("null") || pages.trim().length() == 0) {
			this.setCurrentPage(1);
		} else {
			this.setCurrentPage(new Integer(pages).intValue());
		}
		return this;

	}
	public PageModel setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		return  this;
	}



	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageStartRow() {
		return pageStartRow;
	}

	public void setPageStartRow(int pageStartRow) {
		this.pageStartRow = pageStartRow;
	}

	public int getPageEndRow() {
		return pageEndRow;
	}

	public void setPageEndRow(int pageEndRow) {
		this.pageEndRow = pageEndRow;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public int getPageRecorders() {
		return pageRecorders;
	}
	public List<Map<String, Object>> getPageData() {
		return pageData;
	}

	public void setPageData(List<Map<String, Object>> pageData) {
		this.pageData = pageData;

	}

	public Dialect getDialect() {
		return dialect;
	}

	public PageModel setDialect(Dialect dialect) {
		this.dialect = dialect;
		return  this;
	}
}
