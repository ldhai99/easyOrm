package io.github.ldhai99.easyOrm.page.PageSQLGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
@JsonIgnoreProperties(ignoreUnknown = true)
public interface PageSQLGenerator {

    public SQL generatePageSQL(PageModel pageModel, SQL sql);
    /**
     * 判断是否为升序（优先级：PageModel > SQL 解析）
     */
    default boolean isAscOrder(PageModel pageModel, SQL originalSql) {
        // 1. 优先从 PageModel 获取显式指定的排序方向
        if (pageModel.getOrderBy() != null) {

            return pageModel.getOrderBy() .matches("(?i).*\\bASC\\b.*");
        }

        // 2. 从原始 SQL 解析 ORDER BY 子句
        String orderByClause = originalSql.toOrderBy();
        if (orderByClause != null) {
            return orderByClause.matches("(?i).*\\bASC\\b.*");
        }

        // 3. 默认升序
        return true;
    }

}
