package kr.co.hugetraffic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "stock-service", url = "localhost:80/redis/stock")
public interface StockRedisClient {

    @PostMapping("/decrease/{productId}")
    Integer decreaseStock(@PathVariable Long productId);
}
