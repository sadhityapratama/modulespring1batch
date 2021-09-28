package com.example.testspringbatch.model;

public class Data {
//    Kimberley,35127,2020-09-16,13:04:33,Delosperma
    private String name;
    private String amount;
    private String transDate;
    private String processTime;
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", transDate='" + transDate + '\'' +
                ", processTime='" + processTime + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
