package batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Prevision batch PvBatchJobs
 *
 * @author SungTae, Kang
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PvBatchJobs {

    @Value("${pvBatch.parallel_interval_seconds}")
    private long parallelIntervalSeconds;
    private final JobLauncher jobLauncher;

    private void parallelJob(@NonNull final Integer thread_max_size, Job job) {
        List<Integer> seqList = Arrays.asList(1,2,3,4,5);
        final ExecutorService executor = Executors.newFixedThreadPool(thread_max_size);
        final List<Future<JobExecution>> futures = new ArrayList<>();
        try {
            for (Integer seq : seqList) {
                Thread.sleep(parallelIntervalSeconds);  // 병목현상으로 락을 피하기 위해 1초 sleep
                futures.add(executor.submit(() -> {
                    String jobName = job.getName();
                    try {
                        return jobLauncher.run(job,
                                new JobParametersBuilder()
                                        .addString("date", new Date().toString())
                                        .addString("uuid", UUID.randomUUID().toString())
                                        .addString("jobName", jobName)
                                        .toJobParameters());
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return null;
                    }
                }));
            }
            for (Future<JobExecution> future : futures) {
                log.info(future.get() + "  end: " + LocalTime.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}