package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;

public class MysqlPageSqlById implements PageSql{
    @Override
    public SQL getPageSql(PageModel pageModel, SQL sql) {
        return sql.clone().in(pageModel.getCountId(),
                SQL.SELECT(sql.clone().setColumn(pageModel.getCountId())
                                        .last("limit :start, :records")
                                        .setValue$("start",pageModel.getPageStartRow())
                                        .setValue$("records",pageModel.getSize())
                                ,"a")
                        .column("id"));
    }

}
