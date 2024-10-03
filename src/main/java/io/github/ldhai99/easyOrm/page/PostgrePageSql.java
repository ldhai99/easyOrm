package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;

public class PostgrePageSql implements PageSql{
    @Override
    public SQL getPageSql(PageModel pageModel, SQL sql) {
        return  sql.clone().last("limit :records offset :start ")
                .setValue$("start",pageModel.getPageStartRow())
                .setValue$("records",pageModel.getSize());
    }



}
