package batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Prevision batch scheduler
 *
 * @author SungTae, Kang
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchScheduler implements SchedulingConfigurer {

    @Value("${pvBatch.schedule_pool_size}")
    private int schedulePoolSize;


    /**
     * REAL_TIME
     * scheduler 를 실행 하는 threadPool 정의
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(schedulePoolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("SchedulerPool-");
        threadPoolTaskScheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Scheduled(fixedDelayString = "${pvBatch.restart_interval_seconds}")
    public void runBatch() {
//        startPvBatch(TimeType.PAST, (execute_type == 'S') ? pvBatchFlows.execParallelFlow : pvBatchJobs.execParallelJob);
    }

//    private void startPvBatch(TimeType timeType, BiConsumer<TimeType, Integer> executeJob) {
//        int threadSize = 10;
//        MDC.put("logFileName", timeType.toString());    // log file name 설정
//        log.info("==============================     Batch {} Started      ============================== {} ", timeType, execute_type);
//        executeJob.accept(timeType, threadSize);
//        log.info("==============================     Batch {} Finished      ============================== {} ", timeType, execute_type);
//    }
}