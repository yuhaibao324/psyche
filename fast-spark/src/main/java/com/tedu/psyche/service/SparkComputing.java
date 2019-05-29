package com.tedu.psyche.service;

import com.liangliang.fastbase.entity.Company;
import com.liangliang.fastbase.model.PriceScatter;
import com.liangliang.fastbase.model.StockChange;

import com.liangliang.fastbase.util.FileUtils;
import com.tedu.psyche.dao.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MongoTemplate template;
    public StockChange change(String path){
        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv(path);

        List<Row> rows  = df.agg(min("price_change").as("min"),max("price_change").as("max"), avg("price_change").as("avg")).collectAsList();

        Row row = rows.get(0);
        double avgChange = row.getDouble(2);
        String maxChange = row.getString(1);
        String minChange = row.getString(0);
        StockChange change = StockChange.builder().avgChange(avgChange).maxChange(Double.parseDouble(maxChange)).minChange(Double.parseDouble(minChange)).build();
        return change;
    }
    public void caculate(String path){
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
            log.info(">>>>change = {}", JSON.toString(company));
        }
        long end = System.currentTimeMillis();
        log.info(">>>>cost = {}",(end-start));
        template.insertAll(changes);
    }


    public boolean batchCaculate(String path){

        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv(path+"*.csv");
        Dataset<Row> result = df.groupBy("ts_code").agg(min("price_change").as("min"),max("price_change").as("max"), avg("price_change").as("avg"));
        List<Row> rows= result.collectAsList();
        template.dropCollection(StockChange.class);
        List<Company> companies = companyRepository.listAll();
        List<StockChange> changes = new ArrayList<>();
        Map<String,Company> companyMap = companies.stream().collect(Collectors.toMap(Company::getCode, company -> company));
        for (Row row : rows){
            String ts_code = row.getString(0);
            double avgChange = row.getDouble(3);
            String maxChange = row.getString(2);
            String minChange = row.getString(1);
            Company company = companyMap.get(ts_code);
            if (company==null){
                continue;
            }
            String comName = companyMap.get(ts_code).getName();
            StockChange change = StockChange.builder().code(ts_code).name(comName).avgChange(avgChange).maxChange(Double.parseDouble(maxChange)).minChange(Double.parseDouble(minChange)).build();
            changes.add(change);
        }
        template.dropCollection(StockChange.class);
        template.insertAll(changes);
        return true;

    }

    public void priceAndAmountMap(String path){
        Dataset<Row> df = spark.read().format("CSV").option("header","true").csv(path+"*.csv");
        Dataset<Row> result = df.groupBy("ts_code","ma5").agg(sum("v_ma5"));
        List<PriceScatter> priceScatters = new ArrayList<>();
        for (Row row:result.collectAsList()){
            PriceScatter scatter = PriceScatter.builder().code(row.getString(0)).
                    price(row.getString(1)).
                    amount(row.get(2)+"").build();
            priceScatters.add(scatter);
        }
        template.dropCollection(PriceScatter.class);
        template.insertAll(priceScatters);
    }

}
