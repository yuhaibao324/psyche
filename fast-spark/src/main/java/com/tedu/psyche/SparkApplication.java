package com.tedu.psyche;

import com.tedu.psyche.service.SparkComputing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@EnableScheduling
@Slf4j
@SpringBootApplication
public class SparkApplication {

    @Autowired
    private SparkComputing computing;
    public static void main(String[] args) {
        SpringApplication.run(SparkApplication.class);
    }
}
