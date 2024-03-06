package com.yomahub.liteflow.thread;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 并行多线程执行器构造器接口
 *
 * @author Bryan.Zhang
 * @since 2.6.6
 */
public interface ExecutorBuilder {

    ExecutorService buildExecutor();

    //构建默认的线程池对象
    default ExecutorService buildDefaultExecutor(int corePoolSize, int maximumPoolSize, int queueCapacity, String threadName) {
        return TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadFactory() {
                    private final AtomicLong number = new AtomicLong();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread newThread = Executors.defaultThreadFactory().newThread(r);
                        newThread.setName(threadName + number.getAndIncrement());
                        newThread.setDaemon(false);
                        return newThread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()) {
            @Override
            public void execute(Runnable command) {
                Map<String, String> context = MDC.getCopyOfContextMap();
                super.execute(() -> {
                    MDC.clear();
                    if (context != null) {
                        MDC.setContextMap(context);
                    }
                    command.run();
                });
            }
        });
    }
}
