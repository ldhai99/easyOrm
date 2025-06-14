package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;

public abstract class GroupHandler <T extends GroupHandler<T>> extends WhereHandler<T> {

    //分组-----groupBy---

    public T groupBy(String clause) {
        this.builder.groupBy(clause);
        return self();
    }

    public <E> T groupBy(PropertyGetter<E> getter) {
        return groupBy(FieldResolver.fullField(getter));
    }



}
