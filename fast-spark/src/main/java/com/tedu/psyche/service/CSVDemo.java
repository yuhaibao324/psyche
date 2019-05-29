package com.tedu.psyche.service;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.SQLContext;
import org.mortbay.util.ajax.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/07
 */
public class CSVDemo {
    private static Logger log = LoggerFactory.getLogger(CSVDemo.class);
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> records = sc.textFile("/Users/sunliangliang/Documents/personal/csv/000001.csv");
        SQLContext sqlContext = new SQLContext(sc);





        JavaRDD<String> words = records.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String record) throws Exception {
                log.info(">>>>> record = {}",JSON.toString(record));
                return Arrays.asList(record.split(",")).iterator();
            }
        });

        for (String word:words.collect()){

            log.info(">>>word = {}",word);

        }


    }
}
