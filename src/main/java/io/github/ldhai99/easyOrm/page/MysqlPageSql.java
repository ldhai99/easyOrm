package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;

public class MysqlPageSql implements PageSql{
    @Override
    public SQL getPageSql(PageModel pageModel, SQL sql) {
        return  sql.clone().last("limit :start, :records")
                .setValue$("start",pageModel.getPageStartRow())
                .setValue$("records",pageModel.getSize());
    }

}
