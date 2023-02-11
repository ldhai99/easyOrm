package io.github.ldhai99.easyOrm.Dialect;

import io.github.ldhai99.easyOrm.PageModel;
import io.github.ldhai99.easyOrm.SQL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MysqlDialect implements Dialect {

    SQL sql=null;
    PageModel page=null;

    public SQL getSql() {
        return sql;
    }

    public Dialect setSql(SQL sql) {
        this.sql = sql;
        return  this;
    }

    public PageModel getPage() {
        return page;
    }

    public Dialect setPage(PageModel page) {
        this.page = page;
        return this;
    }

    public static void main(String[] args) {


    }


    public   int getRowCount() throws SQLException {

        return getRowCount(sql).intValue();
    }
    public  List <Map<String, Object>> getPageData() throws SQLException{
        //具体哪一种可以翻页可以更换
       return  getPageData(sql,page.getPageStartRow(),page.getPageRecorders());

    }

    //获取总记录数量
    public   Number getRowCount(SQL sql) throws SQLException {
        return sql.getCount();
    }
    //通用获取页数据
    public  List <Map<String, Object>> getPageData(SQL sql,int pageStartRow,int pageRecorders) throws SQLException {
        return  sql.clone().last("limit :start, :records")
                .setValue$("start",pageStartRow)
                .setValue$("records",pageRecorders) .getMaps();
    }
    //有id的获取页数据
    public  List <Map<String, Object>> getPageData(SQL sql,int pageStartRow,int pageRecorders,String id) throws SQLException {
        return  sql.clone().setWhere("").in(id,
                SQL.SELECT(sql.clone().setColumn(id)
                        .last("limit :start, :records")
                        .setValue$("start",pageStartRow)
                        .setValue$("records",pageRecorders)
                        ,"a")
                        .column("id")).getMaps();
    }
    //有起始id的获取页数据
    public  List <Map<String, Object>> getPageData1(SQL sql,int pageStartId,int pageRecorders,String id) throws SQLException {
        return    sql.clone().gt("id",pageStartId)
                .last(" limit :records")
                .setValue$("records",pageRecorders)
                .getMaps();
    }


}
