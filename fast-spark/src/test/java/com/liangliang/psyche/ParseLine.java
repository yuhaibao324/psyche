package com.liangliang.psyche;


import au.com.bytecode.opencsv.CSVReader;
import org.apache.spark.api.java.function.Function;

import java.io.StringReader;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/15
 */
public class ParseLine implements Function<String,String[]> {
    @Override
    public String[] call(String v1) throws Exception {
        CSVReader reader = new CSVReader(new StringReader(v1));
        return reader.readNext();
    }
}
