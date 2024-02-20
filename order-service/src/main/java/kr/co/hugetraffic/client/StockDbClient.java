package kr.co.hugetraffic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "stockDb-service", url = "localhost:80/db/stock")
public interface StockDbClient {

    @GetMapping("/{productId}")
    Integer getStock(@PathVariable Long productId);

    @PostMapping("/decrease/{productId}")
    Integer decreaseStock(@PathVariable Long productId);

    @PostMapping("/increase/{productId}")
    Integer increaseStock(@PathVariable Long productId);
}