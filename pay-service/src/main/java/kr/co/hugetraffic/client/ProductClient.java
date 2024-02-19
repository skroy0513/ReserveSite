package kr.co.hugetraffic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@FeignClient(name = "product-service", url = "localhost:80/product/feign")
public interface ProductClient {

    @GetMapping("/isPreOrder/{productId}")
    Boolean isPreOrder(@PathVariable Long productId);

    @GetMapping("/getOpenTime/{productId}")
    LocalDateTime getOpenTime(@PathVariable Long productId);
}
