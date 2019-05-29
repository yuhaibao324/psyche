package com.tedu.psyche.controller;

import com.liangliang.fastbase.entity.Company;
import com.liangliang.fastbase.rest.RestResult;
import com.liangliang.fastbase.rest.RestResultBuilder;
import com.tedu.psyche.dao.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Since 2019/05/22
 */
@Slf4j
@RestController
@RequestMapping("/stock-data")
public class CompanyController {
    @Autowired
    private CompanyRepository repository;
    @GetMapping("/companies")
    public RestResult list(HttpServletResponse response, @RequestParam(name = "code",required = false)String code){
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<Company> companies = repository.listAll();
        return RestResultBuilder.builder().data(companies).success().build();
    }



}
