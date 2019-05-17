package com.liangliang.psyche.dao;

import com.liangliang.fastbase.entity.Company;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Repository
public class CompanyRepository {
    @Autowired
    private MongoTemplate template;

    public Company findByCode(String ts_code){
        Query query = new Query();
        query.addCriteria(Criteria.where("ts_code").is(ts_code));
        Company company = template.findOne(query,Company.class);
        return company;
    }

    public List<Company> listAll() {
        List<Company> companies = template.findAll(Company.class);
        return companies;
    }
}
