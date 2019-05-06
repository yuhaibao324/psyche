package com.tedu.psyche.controller;

import com.alibaba.fastjson.JSON;
import com.liangliang.fastbase.rest.RestResult;
import com.liangliang.fastbase.rest.RestResultBuilder;
import com.tedu.psyche.dto.StockAnaly;
import com.tedu.psyche.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/05
 */
@RequestMapping("/stock-statistics")
@RestController
public class StockStatisticsController {
    private static Logger log = LoggerFactory.getLogger(StockStatisticsController.class);
    @Autowired
    private StockService stockService;
    @GetMapping("/area")
    public RestResult area(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<StockAnaly> areaList = stockService.area();
            log.info(">>>>> areas = {}", JSON.toJSONString(areaList));
        return RestResultBuilder.builder().data(areaList).success().build();
    }

    @GetMapping("/industry")
    public RestResult industry(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<StockAnaly> areaList = stockService.industry();
        log.info(">>>>> areas = {}", JSON.toJSONString(areaList));
        return RestResultBuilder.builder().data(areaList).success().build();
    }




}
