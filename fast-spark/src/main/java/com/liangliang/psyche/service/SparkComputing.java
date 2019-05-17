package com.liangliang.psyche.service;

import com.liangliang.fastbase.entity.Company;
import com.liangliang.fastbase.model.StockChange;

import com.liangliang.fastbase.util.FileUtils;
import com.liangliang.psyche.dao.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Slf4j
@Service
public class SparkComputing {
    @Autowired
    private SparkSession spark;
    @Value("${spark.local.file.csv.location}")
    private String path;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MongoTemplate template;
    public StockChange change(String path){
        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv(path);

        Dataset<Row> result = df.agg(min("price_change").as("min"),max("price_change").as("max"), avg("price_change").as("avg"));

        List<Row> rows= result.collectAsList();
        Row row = rows.get(0);
        double avgChange = row.getDouble(2);
        String maxChange = row.getString(1);
        String minChange = row.getString(0);
        StockChange change = StockChange.builder().avgChange(avgChange).maxChange(Double.parseDouble(maxChange)).minChange(Double.parseDouble(minChange)).build();
        return change;
    }
    public void caculate(){
        long start = System.currentTimeMillis();
        List<String> fileNames = FileUtils.listFiles(path);
        List<Company> companies = companyRepository.listAll();
        Map<String,Company> companyMap = companies.stream().collect(Collectors.toMap(Company::getCode, company -> company));
        List<StockChange> changes = new ArrayList<>();
        for (String fileName : fileNames){
            Company company = companyMap.get(fileName.replace("csv","SZ"));
            if (company==null){
                continue;
            }
            StockChange change = change(path+fileName);
            change.setCode(company.getCode());
            change.setName(company.getName());
            changes.add(change);
        }
        long end = System.currentTimeMillis();
        log.info(">>>>cost = {}",(end-start));
        template.insertAll(changes);
    }
}
