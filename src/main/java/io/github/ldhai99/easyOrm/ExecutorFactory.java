package io.github.ldhai99.easyOrm;

import io.github.ldhai99.easyOrm.executor.IMapper;

public class ExecutorFactory {
    private static IMapper executor=null ;

    public  static IMapper getExecutor(){
        return executor;
    }
    public static void   setExecutor(IMapper iExecutor){
        ExecutorFactory.executor=iExecutor;
    }
}
