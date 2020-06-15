package batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.slf4j.MDC;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    @Value("${pvBatch.page_size}")
    private int PAGE_SIZE;
    @Value("${pvBatch.chunk_size}")
    private int CHUNK_SIZE;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    public <T, U> Job job(String jobName, Step step) {
        return jobBuilderFactory.get(jobName)
                .start(step)
                .build();
    }

    public <T, U> Step step(String jobName, JpaPagingItemReader<T> r, ItemWriter<U> w) {
        return stepBuilderFactory.get(jobName + "Step")
                .<T, U>chunk(CHUNK_SIZE)
                .reader(r)
                .writer(w)
                .build();
    }

    public Step step(String jobName, Tasklet tasklet) {
        return stepBuilderFactory.get(jobName + "Step")
                .tasklet(tasklet)
                .build();
    }

    /**
     * 공통으로 사용 하는 JpaPagingItemReaderBuilder build 하는 함수
     *
     * @param readerName
     * @param queryStr   native query String
     * @param clazz      return Class
     * @param <T>        return type
     * @return
     */
    public <T> JpaPagingItemReader<T> reader(final String readerName, final String queryStr, Class<T> clazz) {
        JpaNativeQueryProvider<T> queryProvider = new JpaNativeQueryProvider<T>();// for native query usage
        queryProvider.setEntityClass(clazz);
        queryProvider.setSqlQuery(queryStr);
        JpaPagingItemReader<T> reader = new JpaPagingItemReader<T>() {
            // 완료 된 이후 에  page 를  이동하게 되면 1페이지를 건너 뛰기 때문에 재정의 함
            @Override
            public int getPage() {
                return 0;
            }
        };
        reader.setName(readerName+"_reader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(PAGE_SIZE);
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 공통으로 사용 하는 writer 함수 (calculation, predictCondition)
     *
     * @param queryStr  native query String
     * @param processor processor
     * @param jobParam  processor 에 필요한 정보 객체
     * @param <T>       inputType
     * @return
     */
    public <T> ItemWriter writer(String queryStr, TriConsumer processor, T jobParam) {
        return list -> {
            MDC.put("logFileName", (String) jobParam);    // log file name 설정
            long startTime = System.currentTimeMillis();
            String commitStatus = "";
            T param = jobParam;
            Connection conn = dataSource.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(queryStr)) {
                conn.setAutoCommit(false);
                list.forEach(item -> processor.accept(stmt, item, jobParam));
                stmt.executeBatch();
                conn.commit();
                commitStatus = "commit";
            } catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
                commitStatus = "rollback";
            } finally {
                log.info(""+commitStatus);
            }
        };
    }

}