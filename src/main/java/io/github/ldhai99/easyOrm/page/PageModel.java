package io.github.ldhai99.easyOrm.page;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public  class PageModel {

    //页面信息
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

    //数据信息
    //主键
    String id="";


    //开始行id
    long pageStartId=0;

    List<Map<String, Object>> dataMaps;
    PageData pageData;

    // 构造函数
    public PageModel(){

    }


    // 获取数据

    public PageModel execute()  {

            totalRows = pageData.getRowCount();
            toPage();

            pageData.setPage(this);
            dataMaps= pageData.getPageData();
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
    public String getId() {
        return id;
    }

    public PageModel setId(String id) {
        this.id = id;
        return  this;
    }

    public long getPageStartId() {
        return pageStartId;
    }

    public PageModel setPageStartId(long pageStartId) {
        this.pageStartId = pageStartId;
        return  this;
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

    public PageModel setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return  this;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public PageModel setTotalRows(int totalRows) {
        this.totalRows = totalRows;
        return  this;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public PageModel setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
        return  this;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public PageModel setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
        return  this;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public PageModel setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return  this;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public PageModel setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
        return this;
    }

    public int getNextPage() {
        return nextPage;
    }

    public PageModel setNextPage(int nextPage) {
        this.nextPage = nextPage;
        return  this;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public PageModel setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
        return  this;
    }


    public int getCurrentPage() {
        return currentPage;
    }


    public int getPageRecorders() {
        return pageRecorders;
    }
    public List<Map<String, Object>> getDataMaps() {
        return dataMaps;
    }

    public PageModel setDataMaps(List<Map<String, Object>> page_data) {
        this.dataMaps = page_data;
        return  this;
    }

    public PageData getPageData() {
        return pageData;
    }

    public PageModel setPageData(PageData pageData) {
        this.pageData = pageData;
        return  this;
    }
}
