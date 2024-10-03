package io.github.ldhai99.easyOrm.page;


import java.util.List;
import java.util.Map;

public class PageModel <T>{
    //记录信息
    protected long total = 0; // 总记录数
    protected long size = 10;// 每页5条数据

    //页面信息
    protected long current = 1; // 当前页
    protected long pages = 0l;//总页数


    protected boolean hasNext = false; // 是否有下一页


    protected boolean hasPrevious = false; // 是否有前一页

    // 每页的起始数
    protected long pageStartRow = 0;

    // 每页显示数据的终止数
    protected long pageEndRow = 0;

    protected long nextPage = 0;// 下一页的页码

    protected long previousPage = 0;// 上一页的页码


    //数据信息
    //主键
    protected String countId = "";


    protected boolean searchCount = true; // 是否计算总条数

    //开始行id
    protected long pageStartId = 0;



    protected List<Map<String, Object>> recordsMaps;


    protected   List<T> records;
    PAGE page;

    // 构造函数
    public PageModel() {

    }



    public List<Map<String, Object>> getRecordsMaps() {
        return recordsMaps;
    }

    public PageModel setRecordsMaps(List<Map<String, Object>> recordsMaps) {
        this.recordsMaps = recordsMaps;
        return this;
    }
    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }



    PageModel setPages(long pages) {
        this.pages = pages;
        return this;
    }

    public long getPages() {
        //计算总页数
        if ((this.getTotal() % this.getSize()) == 0) {
            pages = (long) (this.getTotal() % this.getSize());
        } else {
            pages = (long) (this.getTotal() % this.getSize() + 1);
        }

        //当前页不能大于总页数
        if(this.current>this.pages)
            this.current=this.pages;

        //计算当前页,下一页
        if (current < pages) {
            this.pageStartRow = ((current - 1) * this.size);
            this.pageEndRow = this.pageStartRow + this.size;

            this.hasNext = true;
            this.nextPage = this.current + 1;
        } else if (current == pages) {
            this.pageStartRow = ((this.current - 1) * this.size);
            this.pageEndRow = total;

            hasNext = false;
            nextPage = current;
        }

        //计算前一页
        if (current < 2) {
            this.previousPage = this.current;
            this.hasPrevious = false;
        } else if (current > 1) {
            this.previousPage = this.current - 1;
            this.hasPrevious = true;
        }

        return pages;
    }

    public long getPageStartId() {
        return pageStartId;
    }

    public PageModel setPageStartId(long pageStartId) {
        this.pageStartId = pageStartId;
        return this;
    }
    //设置每页行数
    public PageModel setSize(int size) {
        this.size = size;
        return this;
    }

    //获取每页行数
    public long getSize() {
        return this.size;
    }

    //设置当前页
    public PageModel setCurrent(String pages) {

        if (pages == null || pages.equals("null") || pages.trim().length() == 0) {
            this.setCurrent(1);
        } else {
            this.setCurrent(new Integer(pages).intValue());
        }
        return this;

    }

    public PageModel setCurrent(long current) {
        this.current = current;
        return this;
    }

    public long getCurrent() {
        return this.current;
    }



    public String getCountId() {
        return countId;
    }

    public PageModel setCountId(String countId) {
        this.countId = countId;
        return this;
    }
    public PageModel setTotal(long total) {
        this.total = total;
        this.getPages();
        return this;
    }

    public long getTotal() {
        return total;
    }
    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public long getPageStartRow() {
        return pageStartRow;
    }

    public PageModel setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
        return this;
    }

    public long getPageEndRow() {
        return pageEndRow;
    }

    public PageModel setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
        return this;
    }

    public long getNextPage() {
        return nextPage;
    }

    public PageModel setNextPage(int nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public long getPreviousPage() {
        return previousPage;
    }

    public PageModel setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
        return this;
    }
    public boolean isHasNext() {
        return hasNext;
    }

    public PageModel setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public PageModel setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        return this;
    }

    public PAGE getPageData() {
        return page;
    }

    public PageModel setPageData(PAGE page) {
        this.page = page;
        return this;
    }
}
