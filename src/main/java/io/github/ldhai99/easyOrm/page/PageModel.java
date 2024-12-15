package io.github.ldhai99.easyOrm.page;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PageModel <T>{
    // 页面信息
    protected long current = 1; // 当前页码，默认值为 1
    protected long size = 10; // 每页显示的记录数，默认值为 10
    protected long total = 0; // 总记录数，这个值在执行完分页查询后由框架自动填充
    protected long pages = 0; // 总页数，基于 total 和 size 计算得出

    // 数据信息
    protected List<T> records; // 当前页的数据列表，类型通常为 List<T>，其中 T 是实体类类型
    protected List<Map<String, Object>> recordsMaps;

    // 查询选项
    protected boolean searchCount = true; // 是否进行 count 查询以获取总记录数，默认为 true
    protected Long maxLimit; // 单页最大限制数量，默认无限制


    protected boolean optimizeCountSql = true; // 优化 COUNT SQL，当 searchCount 为 true 时生效，默认为 true

    // 分页状态
    protected boolean hasNext = false; // 是否有下一页
    protected boolean hasPrevious = false; // 是否有前一页
    protected long pageStartRow = 0; // 每页的起始行
    protected long pageEndRow = 0; // 每页显示数据的终止行
    protected long nextPage = 0; // 下一页的页码
    protected long previousPage = 0; // 上一页的页码

    // 主键
    protected String countId = ""; // 用于分页的主键字段
    protected long pageStartId = 0; // 开始行的 ID

    // 构造函数
    public PageModel() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;

        this.optimizeCountSql = true;
        this.searchCount = true;

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
            pages = (long) (this.getTotal()/ this.getSize());
        } else {
            pages = (long) (this.getTotal()/ this.getSize() + 1);
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
            if(current==0)
                current=1;
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

    public Long getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }
    public void setSize(long size) {
        this.size = size;
    }

    public boolean isOptimizeCountSql() {
        return optimizeCountSql;
    }

    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    public void setPageStartRow(long pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public void setPageEndRow(long pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    public void setNextPage(long nextPage) {
        this.nextPage = nextPage;
    }

    public void setPreviousPage(long previousPage) {
        this.previousPage = previousPage;
    }

}
