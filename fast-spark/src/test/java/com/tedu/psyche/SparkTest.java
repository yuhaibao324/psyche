package com.tedu.psyche;


import com.liangliang.fastbase.model.StockChange;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.seqdoop.hadoop_bam.FastqInputFormat;
import org.seqdoop.hadoop_bam.SequencedFragment;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.spark.sql.functions.*;


/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/14
 */
public class SparkTest {
    private static JavaSparkContext sc;
    private static SparkConf conf;
    static {
         conf = new SparkConf()
                .setAppName("WordCountLocal")
                .setMaster("local");
         sc = new JavaSparkContext(conf);
    }
    public static void  map() {

        JavaRDD<String> datas = sc.textFile("/Users/sunliangliang/Documents/personal/spark.txt");


        JavaRDD<String> flatMap = datas.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split("\\s")).iterator();
            }
        });


        JavaRDD<List<String>> map = datas.map(new Function<String, List<String>>() {
            @Override
            public List<String> call(String s) throws Exception {
                return Arrays.asList(s.split("\\s"));
            }
        });

        System.out.println(StringUtils.join(map.collect(),","));
        System.out.println(StringUtils.join(flatMap.collect(),","));

    }


    public static void  csvTest(){

        JavaRDD<String> datas = sc.textFile("/Users/sunliangliang/Documents/personal/csv/*.csv");
        datas.collect();

    }




    public static void reduce() {
        JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        int sum = rdd.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer x, Integer y) throws Exception {
                return x + y;
            }
        });
        rdd.persist(StorageLevel.DISK_ONLY());
        rdd.cache();
        System.out.println(sum);
    }


    public static void batchCaculate(){
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv("/Users/sunliangliang/Documents/personal/new-csv/*.csv");

        Dataset<Row> result = df.groupBy("ts_code").agg(min("price_change").as("min"),max("price_change").as("max"), avg("price_change").as("avg"));
        List<Row> rows= result.collectAsList();
        df.show();
        for (Row row : rows){
            double avgChange = row.getDouble(2);
            String maxChange = row.getString(1);
            String minChange = row.getString(0);
            StockChange change = StockChange.builder().avgChange(avgChange).maxChange(Double.parseDouble(maxChange)).minChange(Double.parseDouble(minChange)).build();
            System.out.println(">>>>>>change = {}"+change.toString());
        }
    }

    public static void priceAndAmountMap(){
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv("/Users/sunliangliang/Documents/datas/live-data/new-csv/*.csv");

        Dataset<Row> result = df.groupBy("ts_code","ma5").agg(sum("v_ma5"));
        Dataset<Row> filters = result.filter(new FilterFunction<Row>() {
            @Override
            public boolean call(Row row) throws Exception {
                return row.get(0).equals("000002.csv");
            }
        });
        result.show(1000);
        System.out.println("------------------------");
        filters.show();
    }



    public static void main(String[] args) {
        priceAndAmountMap();

    }



}
