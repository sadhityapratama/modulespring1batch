package com.example.testspringbatch.service;

import com.example.testspringbatch.model.Data;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;



public class CustomItemWriter implements ItemStreamWriter<Data> {

    private FlatFileItemWriter<Data> delegate;
    private Integer totalAmount = 0;
    private int totalTransaction = 0;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        delegate = new FlatFileItemWriter<>();
        delegate.setResource(new FileSystemResource("output/outputData.txt"));
        delegate.setLineAggregator(lineAggregator());
        delegate.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write(String.format("%-25s" , "NAME"));
                writer.append(" | ");
                writer.write(String.format("%10s" , "AMOUNT"));
                writer.append(" | ");
                writer.write(String.format("%-19s" , "TRANS_DATETIME"));
                writer.append(" | ");
                writer.write(String.format("%-19s" , "PROCESS_DATETIME"));
                writer.append(" | ");
                writer.write(String.format("%-35s" , "LOCATION"));
                writer.write("\n==============================================================================================================================================");
            }
        });
    }

    @Bean
    public DelimitedLineAggregator<Data> lineAggregator() {
        DelimitedLineAggregator<Data> lineAggregator = new DelimitedLineAggregator<Data>();

        BeanWrapperFieldExtractor<Data> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<Data>();
        beanWrapperFieldExtractor.setNames(new String[] {"name" , "amount" , "transDate" , "processTime" , "location"});

        lineAggregator.setDelimiter(" | ");
        lineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return lineAggregator;
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    @Override
    public void open(ExecutionContext ec) throws ItemStreamException {
        delegate.open(ec);
    }

    @Override
    public void update(ExecutionContext ec) throws ItemStreamException {
        delegate.update(ec);
    }

    @Override
    public void write(List<? extends Data> list) throws Exception {
        System.out.println("sayonara kotomoni ");
        DecimalFormat df = new DecimalFormat("#,###.00");
        DecimalFormat df2 = new DecimalFormat("#,###");
        Data data = new Data();

        for (int i = 0 ; i < list.size() ; i ++) {
            data = list.get(i);
            totalTransaction++;
            totalAmount = totalAmount + Integer.parseInt(data.getAmount());
            if (data.getAmount().length() < 10){
                data.setAmount(String.format("%10s", df.format(Integer.parseInt(data.getAmount()))));
            }
        }




        delegate.write(list);

        delegate.setFooterCallback(new FlatFileFooterCallback() {
            @Override
            public void writeFooter(Writer writer) throws IOException {
                writer.write("============================================================================================================================================== \n");
                writer.write("TOTAL AMOUNT      : " + df.format(Integer.parseInt(totalAmount.toString())) + "\n");
                writer.write("TOTAL TRANSACTION : " + df2.format(Integer.parseInt(String.valueOf(totalTransaction))));

            }
        });


    }
}
