package io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class MysqlPageSqlByStartId implements PageSQLGenerator {
    @Override
    public SQL generatePageSQL(PageModel pageModel, SQL sql) {
        if (pageModel == null || sql == null) {
            throw new IllegalArgumentException("pageModel 和 sql 不能为空");
        }
        if (pageModel.getCountId() == null) {
            throw new IllegalArgumentException("countId 必须指定");
        }

        // 克隆原始 SQL 避免污染
        SQL paginationSql = sql.clone();

        // 获取排序方向（默认升序）
        boolean isAsc = isCountIdAscOrder(pageModel, sql);

        // 根据排序方向选择比较操作符
        String countId = pageModel.getCountId();
        Object pageStartId = pageModel.getPageStartId();

        if (isAsc) {
            paginationSql.gt(countId, pageStartId);  // 升序：id > :startId
        } else {
            paginationSql.lt(countId, pageStartId);  // 降序：id < :startId
        }

        // 追加 LIMIT 子句
        return paginationSql
                .last(" LIMIT :records")
                .setParameter$("records", pageModel.getSize());
    }

}
