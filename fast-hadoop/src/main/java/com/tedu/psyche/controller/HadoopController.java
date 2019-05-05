package com.tedu.psyche.controller;

import com.liangliang.fastbase.rest.RestResult;
import com.liangliang.fastbase.rest.RestResultBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/05
 */
@RestController
public class HadoopController {

    @GetMapping("/index")
    public RestResult index(){


        return RestResultBuilder.builder().success().build();
    }
}
