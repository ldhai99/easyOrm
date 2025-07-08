package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

import java.util.List;
import java.util.Map;

public class PAGE {
    protected SQL sql = null;



    protected SQL pagedSql = null;

    protected PageModel pageModel = null;

    //构造方法--------
    public PAGE(PageModel pageModel, SQL sql) {
        this.pageModel = pageModel;
        this.sql = sql;
    }
    public PAGE(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public PAGE(SQL sql) {
        this.pageModel = new PageModel();
        this.sql = sql;
    }

    //静态方法----------------------------------------------
    public static PAGE of(PageModel pageModel) {
        return new PAGE(pageModel);
    }
    // 静态方法：普通分页（带分页模型）
    public static PAGE of(PageModel pageModel, SQL sql) {
        return new PAGE(pageModel, sql);
    }

    // 静态方法： 默认分页
    public static PAGE of(SQL sql) {
        return new PAGE(new PageModel(), sql);
    }
    // 静态方法：普通分页
    public static PAGE ofNormal(SQL sql) {
        PageModel pageModel = new PageModel();

        PAGE page = new PAGE(pageModel, sql);
        page.setPageSqlNormal();            // 设置分页策略为按起始 ID 分页
        return page;
    }

    // 静态方法：按起始 ID 分页
    public static PAGE ofStartId(SQL sql) {
        PageModel pageModel = new PageModel();

        PAGE page = new PAGE(pageModel, sql);
        page.setPageSqlByStartId();            // 设置分页策略为按起始 ID 分页
        return page;
    }

    // 静态方法：按 ID 分页
    public static PAGE ofById(SQL sql) {
        PageModel pageModel = new PageModel();

        PAGE page = new PAGE(pageModel, sql);
        page.setPageSqlById();                 // 设置分页策略为按 ID 分页
        return page;
    }



    //页面信息PageModel的门面模式------------------------------
    //设置当前页
    public PAGE setCurrent(long current) {
        this.pageModel.setCurrent(current);
        return this;
    }

    //设置每页行数
    public PAGE setSize(int size) {
        this.pageModel.setSize(size);
        return this;
    }

    //设置计数id
    public PAGE setCountId(String countId) {
        this.pageModel.setCountId(countId);
        return this;
    }

    //设置起始行
    public PAGE setPageStartId(long pageStartId) {
        this.pageModel.setPageStartId(pageStartId);
        return this;
    }

    //返回结果Maps
    public List<Map<String, Object>> getRecordsMaps() {
        return this.pageModel.getRecordsMaps();
    }
   //返回结果entity
    public List getRecords() {
        return this.pageModel.getRecords();
    }

    public void setTotal() {

        this.pageModel.setTotal(this.countRows());
    }

    //获取分页SQL生成器
    public PageSQLGenerator getPageSqlGenerator() {
        return  this.pageModel.getPageSqlGenerator();
    }

    //设置分页SQL生成器--动态策略
    public PAGE setPageSqlGenerator(PageSQLGenerator pageSqlGenerator) {
        this.pageModel.setPageSqlGenerator(pageSqlGenerator);
        return this;
    }
    //设置分页SQL生成器----默认策略
    public PAGE setPageSqlNormal() {
        this.pageModel.setPageSqlNormal();
        return this;
    }
    public PAGE setPageSqlById() {
        this.pageModel.setPageSqlById();
        return this;
    }
    public PAGE setPageSqlByStartId() {
        this.pageModel.setPageSqlByStartId();
        return this;
    }
    //---------------------------------------------------------------
    public PAGE buildPage() {

        //获取总记录数量
        long count = countRows();
        //保存总记录数量
        pageModel.setTotal(count);

        this.pagedSql = getPageSqlGenerator().generatePageSQL(pageModel, sql);
        return this;

    }





    //获取总记录数量
    public long countRows() {

        return sql.getCount().longValue();
    }

    //获取记录--------------输出部分---------------------
    //----maps
    public List<Map<String, Object>> pageMaps() {
        buildPage();
        pageModel.setRecordsMaps(pagedSql.getMaps());
        return pageModel.getRecordsMaps();
    }

    public PAGE pageMapsAnd() {
        buildPage();
        pageModel.setRecordsMaps(pagedSql.getMaps());
        return this;
    }
    public PageModel pageMapsWithModel() {
        buildPage();
        pageModel.setRecordsMaps(pagedSql.getMaps());
        return pageModel;
    }
    //----beans
    public <T> List<T> pageBeans(Class<T> T) {
        buildPage();
        pageModel.setRecords(pagedSql.getBeans(T));
        return pageModel.getRecords();
    }

    public PAGE pageBeansAnd(Class T) {
        buildPage();
        pageModel.setRecords(pagedSql.getBeans(T));
        return this;
    }
    public PageModel pageBeansWithModel(Class T) {
        buildPage();
        pageModel.setRecords(pagedSql.getBeans(T));
        return pageModel;
    }

    //----other
    public SQL pageSQL() {
        buildPage();
        return pagedSql;

    }

    // 获取数据

    public SQL getSql() {
        return sql;
    }

    public PAGE setSql(SQL sql) {
        this.sql = sql;
        return this;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

    public PAGE setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
        return this;
    }

    public SQL getPagedSql() {
        if (pagedSql != null) {
            return pagedSql;
        }
        this.pagedSql = getPageSqlGenerator().generatePageSQL(pageModel, sql);
        return pagedSql;
    }

    public void setPagedSql(SQL pagedSql) {
        this.pagedSql = pagedSql;
    }

}