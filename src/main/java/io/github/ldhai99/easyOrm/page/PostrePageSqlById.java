package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;

public class PostrePageSqlById implements  PageSql{
    @Override
    public SQL getPageSql(PageModel pageModel, SQL sql) {
        return sql.clone().setWhere("").in(pageModel.getCountId(),
                SQL.SELECT(sql.clone().setColumn(pageModel.getCountId())
                                        .last("limit :records offset :start")
                                        .setValue$("start", pageModel.getPageStartRow())
                                        .setValue$("records", pageModel.getSize())
                                , "a")
                        .column("id"));
    }

}
