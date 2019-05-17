package com.liangliang.psyche;


import au.com.bytecode.opencsv.CSVReader;
import com.liangliang.fastbase.exception.ExceptionLog;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.io.IOException;
import java.io.StringReader;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/15
 */
public class ParseLine implements Function<String,String[]>{

    public String[] apply(String line) {
        // TODO 自动生成的方法存根
        CSVReader reader = new CSVReader(new StringReader(line));
        String[] lineData = null;
        try {
            lineData = reader.readNext();
        } catch (IOException e) {
            ExceptionLog.getErrorStack(e);
        }

        return lineData;
    }

    @Override
    public String[] call(String line){
        CSVReader reader = new CSVReader(new StringReader(line));
        String[] lineData = null;
        try {
            lineData = reader.readNext();
        } catch (IOException e) {
            ExceptionLog.getErrorStack(e);
        }
        return lineData;
    }

}
