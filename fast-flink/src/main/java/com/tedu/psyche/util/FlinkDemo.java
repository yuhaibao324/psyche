package com.tedu.psyche.util;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * describe:
 * 基于hdfs处理csv文件，一个简单的wordcount demo
 * @author liang
 * @date 2019/04/30
 */
public class FlinkDemo {
    static String input = "/Users/sunliangliang/Documents/workspace/psyche/fast-flink/README.md";
    static String output="hdfs://192.168.221.115:9000/opt/hadoop/output/";
    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "/Users/sunliangliang/Documents/develop-tools/hadoop-3.1.2");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream stream = env.readTextFile(input,"GBK");
        stream
                .flatMap(
                        new FlatMapFunction<String, Tuple2<String, Integer>>() {

                            public void flatMap(String input,
                                                Collector<Tuple2<String, Integer>> collector)
                                    throws Exception {
                                String[] objs = input.split(" ");
                                for (String obj : objs) {
                                    collector
                                            .collect(new Tuple2<String, Integer>(
                                                    obj, 0));// (这里很关键，表示0位置是word，1的位置是1次数)
                                }
                            }
                        })// 2:(flink 1)(storm 1)
                .keyBy(0)// 3:以第0个位置的值，做分区。
                .sum(1)// (flink:8)(storm:5)，对第1个位置的值做sum的操作。
                .printToErr();

        env.execute();//启动任务

    }
}
