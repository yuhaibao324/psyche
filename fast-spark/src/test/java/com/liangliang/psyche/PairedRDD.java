package com.liangliang.psyche;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.mortbay.util.ajax.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/14
 */
public class PairedRDD {
    private static Logger log = LoggerFactory.getLogger(PairedRDD.class);
    private static JavaSparkContext sc;

    static {
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
        sc = new JavaSparkContext(conf);
    }

    public void toPairRDD(){
        JavaRDD<String> datas = sc.parallelize(Arrays.asList("1 语文", "2 数学", "3 英语", "4 政治"));
        /**
         * 将JavaRDD转换为JavaPairRDD
         */
        JavaPairRDD<String,String> prdd = datas.mapToPair(new PairFunction<String, String, String>() {
            public Tuple2<String, String> call(String s) throws Exception {
                return new Tuple2<String, String>(s.split(" ")[0], s.split(" ")[1]);//将第一个单词作为键创建pairRdd
            }
        });

        //输出信息
        prdd.foreach(new VoidFunction<Tuple2<String, String>>() {
            public void call(Tuple2<String, String> t) throws Exception {
                System.out.println(t);
            }
        });
    }


    /***
     * 通过reduce方式实现单词统计
     */
    public static void reduce(){

        //分布式单词计数问题,flatMap() 来生成以单词为键、以数字 1 为值的 pair RDD
        JavaRDD<String> input = sc.textFile("/Users/sunliangliang/Documents/personal/numbers.txt");
        JavaRDD<String> words = input.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
        System.out.println(Arrays.toString(words.collect().toArray()));
        JavaPairRDD<String,Integer> counts = words.mapToPair(
                s -> new Tuple2<String, Integer>(s,1));
        JavaPairRDD<String, Integer> results =  counts.reduceByKey((x,y)->{
            return x+y; });
        List<Tuple2<String, Integer>> results1 = results.collect();
        System.out.println(results1.toString());
    }

    public static void countByKey(){
        JavaRDD<String> input = sc.textFile("/Users/sunliangliang/Documents/personal/numbers.txt");
        JavaRDD<String> words = input.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
        Map<String,Long> countMAp = words.countByValue();
        System.out.println(JSON.toString(countMAp));
    }

    public static void read(){
        JavaRDD<String> input = sc.textFile("/Users/sunliangliang/Documents/personal/stocks.csv");

        input.collect();


    }


    public static void main(String[] args) {
        read();
    }

}
