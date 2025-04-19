package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.Lambda.Field;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.Lambda.TableNameResolver;
import io.github.ldhai99.easyOrm.SQL;

public class GroupHandler <T extends GroupHandler<T>> extends OrderHandler<T> {

    //分组-----groupBy---

    public T groupBy(String clause) {
        this.builder.groupBy(clause);
        return self();
    }

    public <E> T groupBy(PropertyGetter<E> getter) {
        return groupBy(getter, false);
    }

    public <E> T groupByfullCol(PropertyGetter<E> getter) {
        return groupBy(getter, true);
    }

    public <E> T groupBy(PropertyGetter<E> getter, boolean usefullCol) {
        if (usefullCol)
            return groupBy(Field.fullField(getter));
        else
            return groupBy(Field.field(getter));
    }

}
