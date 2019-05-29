package com.tedu.psyche.conf;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Configuration
public class SparkConfiguration {
    private String appName = "sparkExp";
    private String master = "local";

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf() {
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        conf.set("es.index.auto.create","true").set("es.nodes", "10.3.162.202").set("es.port", "9200");
        return conf;
    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public JavaSparkContext javaSparkContext() {
//        return new JavaSparkContext(sparkConf());
//    }
    @Bean
    @ConditionalOnMissingBean
    public SparkSession buildSession(){
        SparkSession spark = SparkSession.builder().config(sparkConf()).getOrCreate();
        return spark;
    }
}
