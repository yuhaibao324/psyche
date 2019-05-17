package com.liangliang.psyche.controller;

import com.alibaba.fastjson.JSON;
import com.liangliang.fastbase.model.StockChange;
import com.liangliang.fastbase.rest.RestResult;
import com.liangliang.fastbase.rest.RestResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */

@Slf4j
@RequestMapping("/stock-data")
@RestController
public class StockChangeController {
    @Autowired
    private MongoTemplate template;
    @GetMapping("/list")
    public RestResult list(HttpServletResponse response, @RequestParam(name = "code",required = false)String code){
        response.setHeader("Access-Control-Allow-Origin", "*");
        Query query = new Query();
        if (!StringUtils.isEmpty(code)){
            query.addCriteria(Criteria.where("code").is(code));
        }
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"avgChange")));
        query.limit(10);
        List<StockChange> changes = template.find(query,StockChange.class);
        log.info(">>>>> areas = {}", JSON.toJSONString(changes));
        return RestResultBuilder.builder().data(changes).success().build();
    }
}
