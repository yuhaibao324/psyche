package com.tedu.psyche.model;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple2;

import java.io.StringReader;
import java.util.Iterator;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/15
 */
public class CSVParseLine implements FlatMapFunction<Tuple2<String,String>,String[]> {
    @Override
    public Iterator<String[]> call(Tuple2<String, String> file) throws Exception {
        CSVReader reader = new CSVReader(new StringReader(file._2));
        return reader.readAll().iterator();
    }
}
