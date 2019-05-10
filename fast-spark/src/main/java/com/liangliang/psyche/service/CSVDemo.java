package com.liangliang.psyche.service;

import com.liangliang.psyche.Record;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SQLContext;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/07
 */
public class CSVDemo {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Record> records = sc.textFile("/Users/sunliangliang/Documents/personal/csv/000002.csv").map(
                new Function<String, Record>() {
                    public Record call(String line) throws Exception {
                        String[] fields = line.split(",");
                        Record sd = new Record(fields[0], fields[1], fields[2].trim(), fields[3]);
                        return sd;
                    }
                });



    }
}
