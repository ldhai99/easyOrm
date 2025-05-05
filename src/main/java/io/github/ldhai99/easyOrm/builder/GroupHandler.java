package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;

public class GroupHandler <T extends GroupHandler<T>> extends OrderHandler<T> {

    //分组-----groupBy---

    public T groupBy(String clause) {
        this.builder.groupBy(clause);
        return self();
    }

    public <E> T groupBy(PropertyGetter<E> getter) {
        return groupBy(getter, false);
    }

    public <E> T groupByfull(PropertyGetter<E> getter) {
        return groupBy(getter, true);
    }

    public <E> T groupBy(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return groupBy(FieldResolver.fullField(getter));
        else
            return groupBy(FieldResolver.field(getter));
    }

}
