package kr.co.hugetraffic.client;

import kr.co.hugetraffic.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "localhost:80/order/feign")
public interface OrderClient {

    @GetMapping("/create/{productId}")
    OrderDto createOrder(@RequestParam("userId") Long userId, @PathVariable Long productId);

    @GetMapping("/success/{prouctId}")
    OrderDto successOrder(@RequestParam("userId") Long userId, @PathVariable Long productId);

    @GetMapping("/fail/{productId}")
    OrderDto failOrder(@RequestParam("userId") Long userId, @PathVariable Long productId);
}
