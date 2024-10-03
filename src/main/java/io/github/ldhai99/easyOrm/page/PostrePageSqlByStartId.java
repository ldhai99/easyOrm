package io.github.ldhai99.easyOrm.page;

import io.github.ldhai99.easyOrm.SQL;

public class PostrePageSqlByStartId implements  PageSql {
    @Override
    public SQL getPageSql(PageModel pageModel, SQL sql) {
        return  sql.clone()
                .gt(pageModel.getCountId(),pageModel.getPageStartId())
                .last(" limit :records")
                .setValue$("records",pageModel.getSize());
    }
}
