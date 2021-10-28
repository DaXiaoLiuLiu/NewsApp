package com.example.viewnews.logic;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();//返回可用的计算资源
    private static final int corePoolSize =  Math.max(2, Math.min(CPU_COUNT - 1, 4));//最大4，最小2
    private static final int maximumPoolSize = CPU_COUNT * 2 +1;//最大线程数

    private static final Executor executor =new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(16));
    //队列容量为16，线程存活时间为1minute

    public static final Executor getExecutor(){
        return executor;
    }
}
