package com.tedu.psyche.job;

import com.liangliang.fastbase.entity.Company;
import com.liangliang.fastbase.util.FileUtils;
import com.tedu.psyche.dao.CompanyRepository;
import com.tedu.psyche.service.SparkComputing;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class DataLoader implements Serializable{
    @Autowired
    private transient CompanyRepository repository;
    @Autowired
    private transient MongoTemplate template;
    @Autowired
    private transient SparkSession spark;
    @Autowired
    private SparkComputing computer;
    @Value("${spark.local.file.csv.location}")
    private String path;

    @Scheduled(cron = "0/50 * * * * ? ")
    public void load(){
        log.info(">>>>> loading data begin <<<<<<<<");
        String stocksPath = path+"stocks/";
        String detailPath = path +"csv/";
        List<String> files = FileUtils.listFiles(stocksPath);
        if (files.isEmpty()){
            log.info("stocks.csv deleted");
        }else {
            companiesLoad(stocksPath);
            FileUtils.delFolders(stocksPath);
        }
        if (FileUtils.listFiles(detailPath).isEmpty()){
            log.info("stocks.csv deleted");
        }else {
            computer.batchCaculate(detailPath);
            computer.priceAndAmountMap(detailPath);
            FileUtils.delFolders(detailPath);
        }

        log.info(">>>>>loading data finished  !!!!<<<<<<");
    }


    public  boolean companiesLoad(String path){
        List<String> files = FileUtils.listFiles(path);
        if (files.isEmpty()){
            return false;
        }
        Dataset<Row> df = spark.read().format("CSV").option("header","true").load(path+"/stocks.csv");
        List<Row> rows = df.collectAsList();
        List<Company> companyList =new ArrayList<>();
        for (Row row:rows){
            Company company = Company.builder().code(row.getString(1)).symbol(row.getString(2)).name(row.getString(3))
                    .area(row.getString(4)).industry(row.getString(5)).listDate(row.getString(6)).build();
            companyList.add(company);
        }
        template.dropCollection(Company.class);
        template.insertAll(companyList);
        return true;
    }



}
