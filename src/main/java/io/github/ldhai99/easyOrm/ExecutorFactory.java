package io.github.ldhai99.easyOrm;

import io.github.ldhai99.easyOrm.executor.IExecutor;

public class ExecutorFactory {
    private static IExecutor executor=null ;

    public  static  IExecutor getExecutor(){
        return executor;
    }
    public static void   setExecutor(IExecutor iExecutor){
        ExecutorFactory.executor=iExecutor;
    }
}
