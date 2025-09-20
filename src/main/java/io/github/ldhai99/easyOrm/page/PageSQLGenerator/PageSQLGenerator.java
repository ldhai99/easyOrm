package io.github.ldhai99.easyOrm.page.PageSQLGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface PageSQLGenerator {

    public SQL generatePageSQL(PageModel pageModel, SQL sql);
    /**
            * 判断分页主键（countId）的排序方向（优先级：PageModel {@literal >}SQL解析 {@literal >} 默认升序）
            * @param pageModel 分页模型，必须包含 countId（分页主键字段）
            * @param originalSql 原始 SQL 对象
 * @return 是否为升序（true=ASC, false=DESC）
            */
    default boolean getAnchorFieldSortDirection(PageModel pageModel, SQL originalSql) {
        final String countId = pageModel.getCountId();
        Objects.requireNonNull(countId, "PageModel.countId 不能为空");

        // ================== 1. 优先从 PageModel 获取显式指定的排序方向 ==================
        if (pageModel.getOrderBy() != null) {
            return SqlTools.parseOrderDirection(countId, pageModel.getOrderBy());
        }

        // ================== 2. 从 SQL 解析 ORDER BY 子句 ==================
        String orderByClause = originalSql.toOrderBy();
        if (orderByClause != null) {
            return SqlTools.parseOrderDirection(countId, orderByClause);
        }

        // ================== 3. 默认按主键升序 ==================
        return true;
    }

}
