package com.tedu.psyche.service;

import com.tedu.psyche.dto.StockAnaly;
import com.tedu.psyche.utils.HDFSUtil;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private Configuration conf;
    public Configuration build(){
        if (conf == null){
            conf = new Configuration();
            conf.set("fs.defaultFS",hdfsDomain);
        }
        return conf;
    }

    public List<StockAnaly> area(){
        String filePath = hdfsDomain + location +"/area"+ "/part-r-00000";
        List<StockAnaly> list = baseCount(filePath);
        return list;
    }

    public List<StockAnaly> industry(){
         String filePath = hdfsDomain + location +"/industry"+ "/part-r-00000";
        List<StockAnaly> list = baseCount(filePath);
        return list;
    }



    public List<StockAnaly> baseCount(String filePath){
        Configuration configuration =  build();
        List<StockAnaly> datas = new ArrayList<>();
        List<String> lines = HDFSUtil.readLine(configuration,filePath);
        long end = System.currentTimeMillis();
        if (lines.isEmpty()){
            return new ArrayList<>();
        }
        for (String line:lines){
            String []stock = line.split("\\s+");
            log.info(">>>>name = {}, value = {}",stock[0],stock[1]);
            String name = stock[0];
            StockAnaly area = new StockAnaly(name,Double.parseDouble(stock[1]));
            datas.add(area);
        }
        return datas;
    }





}
