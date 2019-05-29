package com.tedu.psyche;

import com.tedu.psyche.data.es.model.Stock;
import com.tedu.psyche.data.es.service.StockDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/23
 */

@Slf4j
@RestController
@SpringBootApplication
public class FastFlinkApplication implements CommandLineRunner {
    @Resource
    private StockDataService stockDataService;
    public static void main(String[] args) {
        SpringApplication.run(FastFlinkApplication.class);
        log.info(">>>>FastFlinkApplication started<<<---");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("<<<<<<<<<<<<<<<<<<started"+stockDataService.count());
        log.info(">>>>>>stocks.count = {}",stockDataService.count());
        Stock stock = Stock.builder().ts_code("000001.SZ").symbol(1).name("平安银行").area("深圳").industry("银行").list_date("19910403").build();
        stockDataService.save(stock);
        log.info(">>>>>>after save, stocks.count = {}",stockDataService.count());
    }

    @GetMapping("/")
    public String hello(){
        return "hello,world!";
    }
}
