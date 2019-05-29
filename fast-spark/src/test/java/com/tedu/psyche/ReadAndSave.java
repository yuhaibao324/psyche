package com.tedu.psyche;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

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

    public static void csv() {
        JavaRDD<String> csvFile1 = sc.textFile("/Users/sunliangliang/Documents/personal/csv/000001.csv");
        JavaRDD<String[]> csvData = csvFile1.map(new ParseLine());
        csvData.reduce(new Function2<String[], String[], String[]>() {
            @Override
            public String[] call(String[] v1, String[] v2) throws Exception {
                return new String[0];
            }
        });




        csvData.foreach(x->{
                    for(String s : x){
                        System.out.println(s);
                    }
                }
        );
    }


    public static void csvPairs(){



    }


    public static void main(String[] args) {
        csv();
    }

}
