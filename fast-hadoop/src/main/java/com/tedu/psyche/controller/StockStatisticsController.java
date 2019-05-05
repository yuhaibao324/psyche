package com.tedu.psyche.controller;

import com.liangliang.fastbase.rest.RestResult;
import com.liangliang.fastbase.rest.RestResultBuilder;
import com.tedu.psyche.dto.StockArea;
import com.tedu.psyche.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/05
 */
@RestController
public class StockStatisticsController {
    @Autowired
    private StockService stockService;
    @GetMapping("/count-map")
    public RestResult countMap(){
            List<StockArea> areaList = stockService.count();
        return RestResultBuilder.builder().data(areaList).success().build();
    }

}
