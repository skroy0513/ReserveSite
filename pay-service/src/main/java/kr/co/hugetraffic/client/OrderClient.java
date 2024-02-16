package kr.co.hugetraffic.client;

import kr.co.hugetraffic.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service", url = "localhost:80/order/feign")
public interface OrderClient {

    @GetMapping("/create")
    OrderDto createOrder(Long userId, Long productId);
}
