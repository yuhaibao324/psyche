package com.tedu.psyche;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * describe:
 *
 * @author liang
 * @date 2019/04/30
 */
@EnableScheduling
@SpringBootApplication
public class FastHadoopApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastHadoopApplication.class);
    }
}
