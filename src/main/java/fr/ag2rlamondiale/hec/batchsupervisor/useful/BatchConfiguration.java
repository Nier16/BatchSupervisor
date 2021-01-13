package fr.ag2rlamondiale.hec.batchsupervisor.useful;

import fr.ag2rlamondiale.hec.batchsupervisor.Launcher;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration extends DefaultBatchConfigurer {
    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public Launcher processor() {
        return new Launcher();
    }

    @Bean
    public Job importSupervisorJob(Step step1) {
        return jobBuilderFactory.get("importSupervisorJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(processor())
                .build();
    }
}
