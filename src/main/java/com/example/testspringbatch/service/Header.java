package com.example.testspringbatch.service;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.IOException;
import java.io.Writer;

public class Header implements FlatFileHeaderCallback {
    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write("testing dulu aja bos santui");
        System.out.println("masuk ga dimari");
    }
}
