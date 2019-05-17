package com.liangliang.psyche;

import com.liangliang.psyche.service.SparkComputing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class SparkApplication implements CommandLineRunner {

    @Autowired
    private SparkComputing computing;
    public static void main(String[] args) {
        SpringApplication.run(SparkApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
