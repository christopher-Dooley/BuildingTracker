package com.example.BuildingTracker;

import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ThreadPool {

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
    private ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 10, TimeUnit.SECONDS);

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }
}
