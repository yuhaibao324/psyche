package com.liangliang.psyche;

import com.clearspring.analytics.util.Lists;
import org.apache.avro.TestAnnotation;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.storage.StorageLevel;
import org.junit.Test;
import scala.Serializable;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/14
 */
public class SparkTest {
    private static JavaSparkContext sc;

    static {
        SparkConf conf = new SparkConf()
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

    public static void main(String[] args) {

        JavaRDD<Integer> datas = sc.parallelize(Arrays.asList(1,2,3,4,5));
        JavaDoubleRDD result = datas.mapToDouble(new DoubleFunction<Integer>() {
            @Override
            public double call(Integer x) throws Exception {
                return (double) x+x;
            }
        });
        System.out.println(result.mean());

    }


    public static void reduce(){
        JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1,2,3,4,5));
        int sum = rdd.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer x, Integer y) throws Exception {
                return x+y;
            }
        });
        rdd.persist(StorageLevel.DISK_ONLY());
        rdd.cache();
        System.out.println(sum);
    }



class  AvgCount implements Serializable{
        public int total;
        public int num;

    public AvgCount(int total, int num) {
        this.total = total;
        this.num = num;
    }
    public double avg(){
        return total/(double)num;
    }

    Function2<AvgCount,Integer,AvgCount> addAndCount = new Function2<AvgCount, Integer, AvgCount>() {
        @Override
        public AvgCount call(AvgCount avgCount, Integer x) throws Exception {
            avgCount.total +=x;
            avgCount.num+=1;
            return avgCount;
        }
    };

    Function2<AvgCount,AvgCount,AvgCount> combine = new Function2<AvgCount, AvgCount, AvgCount>() {
        @Override
        public AvgCount call(AvgCount avgCount, AvgCount x) throws Exception {
            avgCount.total +=x.total;
            avgCount.num+=x.num;
            return avgCount;
        }
    };


}




}
