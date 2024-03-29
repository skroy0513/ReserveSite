package kr.co.hugetraffic.client;

import kr.co.hugetraffic.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "localhost:80/orders/feign")
public interface OrderClient {

    @PostMapping("/create/{productId}")
    OrderDto createOrder(@PathVariable("productId") Long productId, @RequestParam("userId") Long userId, @RequestParam("type") String type);

    @PostMapping("/success/{productId}")
    OrderDto successOrder(@PathVariable("productId") Long productId, @RequestParam("userId") Long userId, @RequestParam("type") String type);

    @PostMapping("/fail/{productId}")
    OrderDto failOrder(@PathVariable("productId") Long productId, @RequestParam("userId") Long userId, @RequestParam("type") String type);
}
