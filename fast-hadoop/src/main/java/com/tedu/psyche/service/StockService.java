package com.tedu.psyche.service;

import com.tedu.psyche.dto.StockArea;
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

    public List<StockArea> count(){
        Configuration configuration =  build();
        List<StockArea> areaList = new ArrayList<>();
        String filePath = hdfsDomain + location + "/part-r-00000";
        List<String> lines = HDFSUtil.readLine(configuration,filePath);
        if (lines.isEmpty()){
            return new ArrayList<>();
        }
        for (String line:lines){
            String []stock = line.split("\\s+");
            log.info(">>>>area = {}, count = {}",stock[0],stock[1]);
            StockArea area = new StockArea(stock[0],Integer.parseInt(stock[1]));
            areaList.add(area);
        }
        return areaList;
    }

}
