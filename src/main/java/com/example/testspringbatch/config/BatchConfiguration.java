package com.example.testspringbatch.config;

import com.example.testspringbatch.model.Data;
import com.example.testspringbatch.service.CustomItemWriter;
import com.example.testspringbatch.service.Processor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Value("Settlements/DATA_*.txt")
    private Resource[] inputResource;

    private final Resource outputResource =  new FileSystemResource("output/outputData.txt");

    @Bean
    public FlatFileItemReader<Data> reader(){
        FlatFileItemReader<Data> reader = new FlatFileItemReader<Data>();
        reader.setLineMapper(new DefaultLineMapper<Data>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames(new String[] {"name" , "amount" , "transDate" , "processTime" , "location"});
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<Data>(){
                    {
                        setTargetType(Data.class);
                    }

                });
            }
        });

        return reader;
    }

    @Bean
    public MultiResourceItemReader<Data> multiResourceItemReader() {
        MultiResourceItemReader<Data> multiResourceItemReader = new MultiResourceItemReader<Data>();
        multiResourceItemReader.setResources(inputResource);
        multiResourceItemReader.setDelegate(reader());
        return multiResourceItemReader;

    }

    @Bean
    public Processor processor(){
        return  new Processor();
    }

    @Bean
    public CustomItemWriter customItemWriter(){
        return new CustomItemWriter();
    }

//    @Bean
//    public FlatFileItemWriter<Data> headerWriter() {
//        return new FlatFileItemWriter<Data>();
//    }

    @Bean
    public Step step1(){


        return stepBuilderFactory
                .get("step1")
                .<Data, Data>chunk(10)
                .reader(multiResourceItemReader())
                .processor(processor())
                .writer(customItemWriter())
                .build();

    }

    @Bean
    public Job readTXTFilesJob(){
        return jobBuilderFactory
                .get("readTXTFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

}
