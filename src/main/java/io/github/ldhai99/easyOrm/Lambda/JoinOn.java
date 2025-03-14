package io.github.ldhai99.easyOrm.Lambda;

public class JoinOn {
    String on;
    private JoinOn()
    {
    }
    public  <T> JoinOn eq(PropertyGetter<T> getter)
    {
        if(on==null){
            on=LambdaExtractor.getFullColumnName(getter);
        }else on+=" = "+LambdaExtractor.getFullColumnName(getter);
        return this;
    }
    public static <T> JoinOn of(PropertyGetter<T> getter)
    {
        JoinOn joinOn=new JoinOn();
        joinOn.eq(getter);
        return joinOn;
    }
    public String getOn()
    {
        return on;
    }
}
