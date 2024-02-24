package kr.co.hugetraffic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stock-service", url = "localhost:80/db/stock")
public interface StockClient {

    @PostMapping("/pre/update/{productId}")
    Integer updateStock(@PathVariable Long productId, @RequestParam("stock") int stock);
}
