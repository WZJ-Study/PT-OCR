package cc.wangzijie.ocr.component;


import cc.wangzijie.ocr.task.OcrProcessTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TaskExecutor {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 定时任务执行的线程池
     */
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    /**
     * 单线程的线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(CORE_POOL_SIZE);

    static {
        // 清理代码，例如关闭数据库连接、停止线程等
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("==== Runtime ShutdownHook Triggered! ==== 清理代码，例如关闭数据库连接、停止线程等");
            if (!SCHEDULED_EXECUTOR_SERVICE.isShutdown()) {
                SCHEDULED_EXECUTOR_SERVICE.shutdown();
                log.info("==== Runtime ShutdownHook Triggered! ==== 关闭定时任务执行的线程池：SCHEDULED_EXECUTOR_SERVICE");
            }
            if (!EXECUTOR_SERVICE.isShutdown()) {
                EXECUTOR_SERVICE.shutdown();
                log.info("==== Runtime ShutdownHook Triggered! ==== 关闭线程池：EXECUTOR_SERVICE");
            }
        }));
    }

    /**
     * 运行任务
     *
     * @param task 任务
     */
    public static void execute(Runnable task) {
        if (EXECUTOR_SERVICE.isShutdown() || EXECUTOR_SERVICE.isTerminated()) {
            log.info("==== execute ==== 线程池 EXECUTOR_SERVICE 已关闭或已终止");
            throw new RuntimeException("线程池 EXECUTOR_SERVICE 已关闭或已终止!");
        }
        EXECUTOR_SERVICE.submit(task);
    }

    /**
     * 运行任务
     *
     * @param task 任务
     * @return Future结果
     */
    public static Future<?> run(Runnable task) {
        if (EXECUTOR_SERVICE.isShutdown() || EXECUTOR_SERVICE.isTerminated()) {
            log.info("==== run ==== 线程池 EXECUTOR_SERVICE 已关闭或已终止");
            throw new RuntimeException("线程池 EXECUTOR_SERVICE 已关闭或已终止!");
        }
        return EXECUTOR_SERVICE.submit(task);
    }


    /**
     * 运行定时任务
     *
     * @param task 定时任务
     * @return ScheduledFuture结果
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, int initialDelay, int delay, TimeUnit timeUnit) {
        if (SCHEDULED_EXECUTOR_SERVICE.isShutdown() || SCHEDULED_EXECUTOR_SERVICE.isTerminated()) {
            log.info("==== scheduleWithFixedDelay ==== 线程池 SCHEDULED_EXECUTOR_SERVICE 已关闭或已终止");
            throw new RuntimeException("线程池 SCHEDULED_EXECUTOR_SERVICE 已关闭或已终止!");
        }
        return SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(task, initialDelay, delay, timeUnit);
    }

    /**
     * 运行定时任务
     *
     * @param task 定时任务
     * @return ScheduledFuture结果
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, int initialDelay, int period, TimeUnit timeUnit) {
        if (SCHEDULED_EXECUTOR_SERVICE.isShutdown() || SCHEDULED_EXECUTOR_SERVICE.isTerminated()) {
            log.info("==== scheduleAtFixedRate ==== 线程池 SCHEDULED_EXECUTOR_SERVICE 已关闭或已终止");
            throw new RuntimeException("线程池 SCHEDULED_EXECUTOR_SERVICE 已关闭或已终止!");
        }
        return SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(task, initialDelay, period, timeUnit);
    }

    /**
     * 运行定时任务
     *
     * @param task 定时任务
     * @return ScheduledFuture结果
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, int intervalSeconds) {
        return SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(task, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }
}
