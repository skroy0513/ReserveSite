package kr.co.hugetraffic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@FeignClient(name = "preOrderProduct-service", url = "localhost:80/pre/products/feign")
public interface PreOrderProductClient {

    @GetMapping("/open-time/{productId}")
    LocalDateTime getOpenTime(@PathVariable Long productId);
}
