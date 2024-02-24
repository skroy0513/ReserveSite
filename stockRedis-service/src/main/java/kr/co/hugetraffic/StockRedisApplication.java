package kr.co.hugetraffic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StockRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockRedisApplication.class, args);
    }
}
