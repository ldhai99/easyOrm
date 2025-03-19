package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.tools.DbTools;

import java.util.List;
import java.util.Map;

public class PAGE {
    protected SQL sql = null;
    protected SQL pageSql = null;

    protected PageSql pageSqlGenerator = null;
    protected PageModel pageModel = null;

    //构造方法
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

    //静态方法
    public static PAGE of(PageModel pageModel, SQL sql) {
        return new PAGE(pageModel, sql);
    }

    public static PAGE of(PageModel pageModel) {
        return new PAGE(pageModel);

    }

    public static PAGE of(SQL sql) {
        return new PAGE(sql);

    }
    //页面信息PageModel的门面模式--------------------
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
    //---------------------------------------------------------------
    public PAGE buildPage() {

        //获取总记录数量
        long count = countRows();
        //保存总记录数量
        pageModel.setTotal(count);
        //生成翻页sql
        initPageSqlGenerator();
        this.pageSql = pageSqlGenerator.getPageSql(pageModel, sql);
        return this;

    }

    //提供默认翻页sql生成器
    public void initPageSqlGenerator() {
        if (this.pageSqlGenerator == null)
            pageSqlGenerator = DbTools.getPageSql();
    }


    //获取总记录数量
    public long countRows() {

        return sql.getCount().longValue();
    }


    public List<Map<String, Object>> pageMaps() {
        buildPage();
        pageModel.setRecordsMaps(pageSql.getMaps());
        return pageModel.getRecordsMaps();
    }



    public PAGE pageMapsAnd() {
        buildPage();
        pageModel.setRecordsMaps(pageSql.getMaps());
        return this;
    }
    public PageModel pageMapsWithModel() {
        buildPage();
        pageModel.setRecordsMaps(pageSql.getMaps());
        return pageModel;
    }

    public <T> List<T> pageBeans(Class<T> T) {
        buildPage();
        pageModel.setRecords(pageSql.getBeans(T));
        return pageModel.getRecords();
    }

    public PAGE pageBeansAnd(Class T) {
        buildPage();
        pageModel.setRecords(pageSql.getBeans(T));
        return this;
    }
    public PageModel pageBeansWithModel(Class T) {
        buildPage();
        pageModel.setRecords(pageSql.getBeans(T));
        return pageModel;
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

    public SQL getPageSql() {
        //生成翻页sql
        initPageSqlGenerator();
        this.pageSql = pageSqlGenerator.getPageSql(pageModel, sql);
        return pageSql;
    }

    public PAGE setPageSql(SQL pageSql) {

        this.pageSql = pageSql;
        return this;
    }

    public PageSql getPageSqlGenerator() {
        return pageSqlGenerator;
    }

    public PAGE setPageSqlGenerator(PageSql pageSqlGenerator) {
        this.pageSqlGenerator = pageSqlGenerator;
        return this;
    }
}