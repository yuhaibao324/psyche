package com.liangliang.psyche;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Describe:
 * spark相关读写
 * @Author liang
 * @Since 2019/05/15
 */
public class ReadAndSave {
    private static JavaSparkContext sc;

    static {
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        sc = new JavaSparkContext(conf);
    }

    public static void readTxt(){
        JavaRDD<String> input = sc.textFile("/Users/sunliangliang/Documents/personal/numbers.txt");
        System.out.println(input.collect().toString());

    }

    public static void readCsv(){
        JavaRDD<String> records = sc.textFile("/Users/sunliangliang/Documents/personal/csv/000001.csv");

        JavaRDD<String[]> csvData = records.map(new ParseLine());


        sc.wholeTextFiles("/Users/sunliangliang/Documents/personal/csv/000001.csv");
        for (String record:records.collect()){
            System.out.println("record = "+record);
        }


    }


    public static void main(String[] args) {
        readTxt();
        readCsv();
    }

}
