package com.example.testspringbatch.service;

import com.example.testspringbatch.model.Data;
import com.sun.istack.internal.NotNull;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Processor implements ItemProcessor<Data, Data> {


    @Override
    public Data process(Data data) throws Exception {

        if (data.getName().length() < 25){
            data.setName(String.format("%-25s", data.getName()));
        }

        if (data.getTransDate().length() < 19){
            data.setTransDate(String.format("%-19s" , data.getTransDate() + " 00:00:00"));
        }


        if (data.getProcessTime().length() < 19){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            data.setProcessTime(String.format("%-19s" , timeStamp + " " + data.getProcessTime()));
        }

        if (data.getLocation().length() < 35) {
            data.setLocation(String.format("%-35s" ,  data.getLocation()));
        }



        return data;
    }


}
