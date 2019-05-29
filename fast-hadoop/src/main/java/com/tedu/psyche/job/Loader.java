package com.tedu.psyche.job;

import com.tedu.psyche.dao.StatisticsRecordsMapper;
import com.tedu.psyche.entity.IndustryModel;
import com.tedu.psyche.entity.StatisticsRecord;
import com.tedu.psyche.enums.DimenoEnum;
import com.tedu.psyche.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/22
 */
@Slf4j
@Component
public class Loader {
    @Autowired
    private transient MongoTemplate template;
    @Autowired
    private StockService stockService;
    @Value("${hadoop.hdfs.domain}")
    private String hdfsDomain;
    @Value("${hadoop.hdfs.location}")
    private String location;
    @Resource
    private StatisticsRecordsMapper mapper;
    @Scheduled(cron = "0/50 * * * * ? ")
    public void load(){
        List<StatisticsRecord> list = mapper.list(DimenoEnum.INDUSTRY.getType());
        if (!list.isEmpty()){
            log.info(">>>>exists<<<");
            return;
        }
        String filePath = hdfsDomain + location +"/industry"+ "/part-r-00000";
        List<StatisticsRecord>  records = stockService.baseCount(filePath, DimenoEnum.INDUSTRY);
        mapper.batchInsert(records);
    }
}
