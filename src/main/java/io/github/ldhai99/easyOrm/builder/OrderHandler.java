package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;

import java.util.List;

public abstract class OrderHandler<T extends OrderHandler<T>> extends GroupHandler<T> {

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
        return orderByAsc(FieldResolver.fullField(getter));
    }


    //---降序排序
    public T orderByDesc(String clause) {
        this.orderBy(clause, false);
        return self();
    }


    public <E> T orderByDesc(PropertyGetter<E> getter) {
        return orderByDesc(FieldResolver.fullField(getter));
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
        return orderBy(FieldResolver.fullField(getter));
    }


    //---排序
    public T orderBy(String clause, boolean ascending) {
        this.builder.orderBy(clause, ascending);
        return self();
    }


    public <E> T orderByColumn(PropertyGetter<E> getter, boolean ascending) {
        return orderBy(FieldResolver.fullField(getter), ascending);
    }


}
