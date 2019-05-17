package com.liangliang.psyche.service;

import com.liangliang.fastbase.entity.Stock;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import static org.apache.spark.sql.functions.*;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/15
 */
@Slf4j
@Service
public class SparkService {
    @Autowired
    private SparkSession sparkSession;

    public static void cscParser(){
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> records = sc.textFile("/Users/sunliangliang/Documents/personal/csv/000001.csv");

        for (String record:records.collect()){
            System.out.println(record);
        }


    }

    public static void sparkSql(){
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> records = sc.textFile("/Users/sunliangliang/Documents/personal/csv/000001.csv");
        SparkSession spark = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv("/Users/sunliangliang/Documents/personal/csv/000002.csv");
        df.show();

//        df.agg(count("price_change").as("count"),max("price_change").as("max"), avg("price_change").as("avg")).show();
        Dataset<Row> result = df.agg(count("price_change").as("count"),max("price_change").as("max"), avg("price_change").as("avg"));

        List<Row> rows= result.collectAsList();
        System.out.println(rows.get(0).toString());

    }

    public static void batchSql(){
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> df = spark.read().format("CSV").option("header","true").load("/Users/sunliangliang/Documents/personal/stocks.csv");
        df.show();
        Dataset<String> jsons=df.toJSON();
        JavaRDD<String> rdd=jsons.javaRDD();
        rdd.foreachPartition(new VoidFunction<Iterator<String>>(){
            @Override
            public void call(Iterator<String> iter) throws Exception {
                while(iter.hasNext()) {
                    String next=iter.next();
                    StringTokenizer itr = new StringTokenizer(new String(next.getBytes(),"GBK"));
                    System.out.println("获取"+itr.nextToken().toString());
                }
            }
        });




    }


    public static void main(String[] args) {
        sparkSql();
    }
}
