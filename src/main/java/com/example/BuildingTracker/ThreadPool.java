package com.example.BuildingTracker;

import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ThreadPool {

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
    private final ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 10, TimeUnit.SECONDS);

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }
}
