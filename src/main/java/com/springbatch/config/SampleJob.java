package com.springbatch.config;

import com.springbatch.service.SecondTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleJob {

    // To create the job spring batch provides one class that is
    // job builder factory
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // To build a step spring batch provides a class
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SecondTasklet secondTasklet;

    // To have the job spring batch provides one interface that is job
    // so we can define a bean
    @Bean
    public Job firstJob() {
        //configure steps in the job very first step define using start next using next()
//        return jobBuilderFactory.get("First Job")
//                .start(firstStep())
//                .next(secondStep())
//                .build();

        // we can use incrementer provided by spring batch to avoid changing job param every time
        // we have to still pass job param in arguments
        return jobBuilderFactory.get("First Job")
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    // To have step spring batch provides an interface
    private Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(firstTask()).build();
    }

    private Tasklet firstTask() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is first tasklet step");
                return RepeatStatus.FINISHED;
            }
        };
    }

    private Step secondStep() {
        return stepBuilderFactory.get("Second Step")
                .tasklet(secondTasklet).build();
    }

    /*private Tasklet secondTask() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is second tasklet step");
                return RepeatStatus.FINISHED;
            }
        };
    }*/
}
