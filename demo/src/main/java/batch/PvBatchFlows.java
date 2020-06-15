package batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * Prevision batch PvBatchFlows
 *
 * @author SungTae, Kang
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PvBatchFlows {

    private final JobLauncher jobLauncher;
    private final JobBuilderFactory jobBuilderFactory;

    private void execParallelFlow(Step step) {
        String jobName = "Task1_parallel_flow";
        try {
            jobLauncher.run(
                    jobBuilderFactory.get("Job_Task" + "_job")
                            .start(parallelFlow("task1", 12, step))
                            .next(parallelFlow("task2", 12, step))
                            .next(parallelFlow("task3", 12, step))
                            .build()
                            .build(),
                    new JobParametersBuilder()
                            .addString("date", new Date().toString())
                            .addString("uuid", UUID.randomUUID().toString())
                            .addString("jobName",jobName)
                            .toJobParameters());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Flow parallelFlow(String task, @NonNull final Integer thread_max_size, Step step) {
        List<Integer> seqList = Arrays.asList(1, 2, 3, 4, 5);
        List<Flow> flows = seqList
                .stream()
                .map(seq -> new FlowBuilder<SimpleFlow>("flowName_" + seq)
                        .start(step)
                        .build())
                .collect(toList());
        return new FlowBuilder<SimpleFlow>(task + "parallel_flow")
                .split(getSimpleAsyncTaskExecutor(thread_max_size))
                .add(flows.toArray(new Flow[flows.size()]))
                .build();
    }

    public static SimpleAsyncTaskExecutor getSimpleAsyncTaskExecutor(int size) {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("SimpleAsync");
        taskExecutor.setConcurrencyLimit(size);
        return taskExecutor;
    }
}