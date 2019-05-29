package com.tedu.psyche.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Describe:
 *
 * @Author sunliang
 * @Since 2019/05/29
 */
@Slf4j
@Service
public class SparkEsService {
    @Autowired
    private SparkSession sparkSession;

    public void testEs(){




    }


}
