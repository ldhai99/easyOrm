package io.github.ldhai99.easyOrm.join;

import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;

public class JoinOn {
    StringBuilder on=new StringBuilder();
    private JoinOn()
    {
    }
    public static <T> JoinOn on(PropertyGetter<T> getter)
    {
        JoinOn joinOn=new JoinOn();
        joinOn.append(FieldResolver.fullField(getter));
        return joinOn;
    }

    protected JoinOn append(String expr){
        on.append(expr);
        return this;

    }
    public  <T> JoinOn append(PropertyGetter<T> getter){
        on.append(FieldResolver.fullField(getter));
        return this;

    }
    public  <T> JoinOn eq(PropertyGetter<T> getter)
    {
        this.append(" = ");
        this.append(FieldResolver.fullField(getter));

        return this;
    }
    public JoinOn and(PropertyGetter<?> leftField) {
        on.append(" AND ");
        return append(leftField);
    }

    public JoinOn or(PropertyGetter<?> leftField) {
        on.append(" OR ");
        return append(leftField);
    }
    public String toString()
    {
        return on.toString();
    }
}
