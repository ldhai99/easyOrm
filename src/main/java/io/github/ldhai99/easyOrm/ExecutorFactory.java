package io.github.ldhai99.easyOrm;

import io.github.ldhai99.easyOrm.executor.Executor;

public class ExecutorFactory {
    private static Executor executor=null ;

    public  static Executor getExecutor(){
        return executor;
    }
    public static void   setExecutor(Executor executor){
        ExecutorFactory.executor= executor;
    }
}
