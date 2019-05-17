package com.liangliang.psyche.job;

import com.liangliang.fastbase.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.*;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Slf4j
@Service
public class DataLoader implements Serializable {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private SparkSession spark;
//    @Scheduled(cron = "0/50 * * * * ? ")
    public void load(){
        List<Company> companies = template.findAll(Company.class);
        if (companies ==null || companies.isEmpty()){
            log.info("empty mongo");
        }else {
            log.info("exists");
            return;
        }
        companies = loadCSV();
        template.insertAll(companies);
    }


    public  List<Company> loadCSV(){
//        JavaRDD<String> records = sc.textFile("/Users/sunliangliang/Documents/personal/stocks.csv");
//        Iterator<String> it = records.collect().iterator();
//        List<Company> companyList =new ArrayList<>();
//        while (it.hasNext()){
//            String line = it.next();
//            if (line.contains("industry")){
//                continue;
//            }
//            String[]items = line.split(",");
//            Company company = Company.builder().code(items[1]).symbol(items[2]).name(items[3])
//                                .area(items[4]).industry(items[5]).listDate(items[6]).build();
//            companyList.add(company);
//        }
        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv("/Users/sunliangliang/Documents/personal/stocks.csv");
        List<Company> companyList =new ArrayList<>();
        df.foreach(row -> {
            log.info(">>>>>>row = {}",row.toString());
            Company company = Company.builder().code(row.getString(1)).symbol(row.getString(2)).name(row.getString(3))
                    .area(row.getString(4)).industry(row.getString(5)).listDate(row.getString(6)).build();
            companyList.add(company);
        });
        return companyList;
    }



}
