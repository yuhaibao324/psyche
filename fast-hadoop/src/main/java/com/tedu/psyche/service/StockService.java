package com.tedu.psyche.service;

import com.liangliang.fastbase.util.DoubleUtils;
import com.tedu.psyche.dao.StatisticsRecordsMapper;
import com.tedu.psyche.dto.StockAnaly;
import com.tedu.psyche.entity.IndustryData;
import com.tedu.psyche.entity.StatisticsRecord;
import com.tedu.psyche.enums.DimenoEnum;
import com.tedu.psyche.utils.HDFSUtil;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/05
 */
@Service
public class StockService {
    private static Logger log = LoggerFactory.getLogger(StockService.class);
    @Value("${hadoop.hdfs.domain}")
    private String hdfsDomain;
    @Value("${hadoop.hdfs.location}")
    private String location;
    @Resource
    private StatisticsRecordsMapper mapper;
    @Autowired
    private MongoTemplate template;
    private Configuration conf;
    public Configuration build(){
        if (conf == null){
            conf = new Configuration();
            conf.set("fs.defaultFS",hdfsDomain);
        }
        return conf;
    }

    public List<StatisticsRecord> area(){
        String filePath = hdfsDomain + location +"/area"+ "/part-r-00000";
        List<StatisticsRecord> list = mapper.list(DimenoEnum.LOCATION.getType());
        if (list==null || list.isEmpty()){
            list = baseCount(filePath,DimenoEnum.LOCATION);
            mapper.batchInsert(list);
        }

        return list;
    }

    public List<IndustryData> industry(){
        List<StatisticsRecord> list = mapper.listByLimit(DimenoEnum.INDUSTRY.getType());
        double sum = 0;
        double totalPercent = 0;
        double total = mapper.total(DimenoEnum.INDUSTRY.getType());
        List<IndustryData> industryDatas = new ArrayList<>();
        for (StatisticsRecord record:list){
            double percent = DoubleUtils.format(record.getValue()/total,2);
            totalPercent+=percent;
            sum+=record.getValue();
            IndustryData industry = new IndustryData(record.getName(),record.getValue(),percent);
            industryDatas.add(industry);
        }
        List<StatisticsRecord> totalRecords = mapper.list(DimenoEnum.INDUSTRY.getType());
        IndustryData industryData = new IndustryData("其他",total-sum, DoubleUtils.format(1-totalPercent,2));
        industryDatas.add(industryData);
        return industryDatas;
    }


    public List<StatisticsRecord> baseCount(String filePath, DimenoEnum dimeno){
        Configuration configuration =  build();
        List<StatisticsRecord> datas = new ArrayList<>();
        List<String> lines = HDFSUtil.readLine(configuration,filePath);
        if (lines.isEmpty()){
            return new ArrayList<>();
        }
        for (String line:lines){
            String []stock = line.split("\\s+");
            String name = stock[0];
            StatisticsRecord record = new StatisticsRecord();
            record.setType(dimeno.getType());
            record.setName(name);
            record.setValue(Double.parseDouble(stock[1]));
            datas.add(record);
        }
        return datas;
    }




    public List<StatisticsRecord> stockPrice(){
        String filePath = hdfsDomain + location + "/price-amount-r-00000";
        List<StatisticsRecord> list = mapper.list(DimenoEnum.PRICE_AMOUNT.getType());
        if (list==null || list.isEmpty()){
            list = baseCount(filePath,DimenoEnum.PRICE_AMOUNT);
            mapper.batchInsert(list);
        }
        return list;
    }



}
