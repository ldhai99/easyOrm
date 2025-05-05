package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;

import java.util.List;

public class OrderHandler<T extends OrderHandler<T>> extends WhereHandler<T> {

    //-----------------------排序----------------------------------------------------
    //---多组排序
    public T orderByAsc(List<String> clauses) {
        for (String clause : clauses) {
            orderByAsc(clause);
        }

        return self();
    }

    public T orderByDesc(List<String> clauses) {
        for (String clause : clauses) {
            orderByDesc(clause);
        }

        return self();
    }

    //---升序排序
    public T orderByAsc(String clause) {

        this.orderBy(clause, true);
        return self();
    }

    public <E> T orderByAsc(PropertyGetter<E> getter) {
        return orderByAsc(getter, false);
    }

    public <E> T orderByAscfull(PropertyGetter<E> getter) {
        return orderByAsc(getter, true);
    }

    public <E> T orderByAsc(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return orderByAsc(FieldResolver.fullField(getter));
        else
            return orderByAsc(FieldResolver.field(getter));
    }

    //---降序排序
    public T orderByDesc(String clause) {
        this.orderBy(clause, false);
        return self();
    }


    public <E> T orderByDesc(PropertyGetter<E> getter) {
        return orderByDesc(getter, false);
    }

    public <E> T orderByDescfull(PropertyGetter<E> getter) {
        return orderByDesc(getter, true);
    }

    public <E> T orderByDesc(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return orderByDesc(FieldResolver.fullField(getter));
        else
            return orderByDesc(FieldResolver.field(getter));
    }

    //---默认升序排序
//    public T orderBy(String clause) {
//        this.orderBy(clause, true);
//        return self();
//    }
    /**
     * 添加多个排序字段，支持直接传入完整的排序字符串。
     *
     * @param orderByString 排序字符串，例如 "item.sort_order ASC, item.create_time DESC"
     * @return 当前 SqlModel 实例
     */
    public T orderBy(String orderByString) {
        if (orderByString == null || orderByString.trim().isEmpty()) {
            throw new IllegalArgumentException("Order by string cannot be null or empty");
        }

        // 按逗号分割多个排序字段
        String[] orderByParts = orderByString.split(",");
        for (String part : orderByParts) {
            part = part.trim();
            if (!part.isEmpty()) {
                // 使用正则表达式匹配 ASC 或 DESC
                if (part.toUpperCase().matches(".*\\bASC\\b.*")) {
                    String fieldName = part.replaceAll("(?i)\\bASC\\b", "").trim();
                    this.orderBy(fieldName, true);
                } else if (part.toUpperCase().matches(".*\\bDESC\\b.*")) {
                    String fieldName = part.replaceAll("(?i)\\bDESC\\b", "").trim();
                    this.orderBy(fieldName, false);
                } else {
                    // 如果没有明确指定排序方向，默认为升序
                    this.orderBy(part, true);
                }
            }
        }
        return self();
    }

    public <E> T orderBy(PropertyGetter<E> getter) {
        return orderBy(getter, false);
    }

    public <E> T orderByfull(PropertyGetter<E> getter) {
        return orderBy(getter, true);
    }

    public <E> T orderBy(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return orderBy(FieldResolver.fullField(getter));
        else
            return orderBy(FieldResolver.field(getter));
    }

    //---排序
    public T orderBy(String clause, boolean ascending) {
        this.builder.orderBy(clause, ascending);
        return self();
    }


    public <E> T orderByColumn(PropertyGetter<E> getter, boolean ascending) {
        return orderBy(getter, ascending, false);
    }

    public <E> T orderByfull(PropertyGetter<E> getter, boolean ascending) {
        return orderBy(getter, ascending, true);
    }

    public <E> T orderBy(PropertyGetter<E> getter, boolean ascending, boolean usefull) {
        if (usefull)
            return orderBy(FieldResolver.fullField(getter), ascending);
        else
            return orderBy(FieldResolver.field(getter), ascending);
    }

}
