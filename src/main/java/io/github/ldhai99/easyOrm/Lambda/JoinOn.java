package io.github.ldhai99.easyOrm.Lambda;

public class JoinOn {
    StringBuilder on=new StringBuilder();
    private JoinOn()
    {
    }
    public static <T> JoinOn on(PropertyGetter<T> getter)
    {
        JoinOn joinOn=new JoinOn();
        joinOn.add(Field.fullField(getter));
        return joinOn;
    }

    protected JoinOn add(String expr){
        on.append(expr);
        return this;

    }
    public  <T> JoinOn add(PropertyGetter<T> getter){
        on.append(Field.fullField(getter));
        return this;

    }
    public  <T> JoinOn eq(PropertyGetter<T> getter)
    {
        this.add(" = ");
        this.add(Field.fullField(getter));

        return this;
    }
    public JoinOn and(PropertyGetter<?> leftField) {
        on.append(" AND ");
        return add(leftField);
    }

    public JoinOn or(PropertyGetter<?> leftField) {
        on.append(" OR ");
        return add(leftField);
    }
    public String toString()
    {
        return on.toString();
    }
}
