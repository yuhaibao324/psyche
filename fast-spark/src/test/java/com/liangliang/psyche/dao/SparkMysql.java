package com.liangliang.psyche.dao;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/15
 */
public class SparkMysql {
    private static Logger log = LoggerFactory.getLogger(SparkMysql.class);
    private static String url = "jdbc:mysql://192.168.221.143:3306/psyche";
    private static String username="liangliang";
    private static String pwd = "12597758";
    public static void main(String[] args) {
        JavaSparkContext sparkContext = new JavaSparkContext(new SparkConf().setAppName("SparkMysql").setMaster("local"));
        SQLContext sqlContext = new SQLContext(sparkContext);
        read(sqlContext);
    }


    public static void read(SQLContext sqlContext){
        Properties connectionProperties = new Properties();
        connectionProperties.put("user",username);
        connectionProperties.put("password",pwd);
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");
        Dataset<Row> jdbcDF = sqlContext.read().jdbc(url,"statistics_records",connectionProperties).select("*");
        jdbcDF.show();
    }

}
