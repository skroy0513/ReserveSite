package kr.co.hugetraffic.web.controller;

import jakarta.ws.rs.Path;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /*
    userId로 주문정보 가져오기
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDto>> getOrder(HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        List<OrderDto> dto = orderService.getOrder(userId);
        return ResponseEntity.ok(dto);
    }

    /*
    해당 상품 주문정보 생성하기(status = pending)
     */
    @GetMapping("/feign/create/{productId}")
    public ResponseEntity<OrderDto> create(@RequestParam("userId") Long userId,
                                           @PathVariable Long productId) {
        return null;
    }

    /*
    해당 상품 주문정보 성공하기(status = success)
     */
    @GetMapping("/feign/success/{productId}")
    public ResponseEntity<OrderDto> success(@RequestParam("userId") Long userId,
                                            @PathVariable Long productId) {
        return null;
    }

    /*
    해당 상품 주문정보 실패하기(status = fail)
     */
    @GetMapping("/feign/fail/{productId}")
    public ResponseEntity<OrderDto> fail(@RequestParam("userId") Long userId,
                                         @PathVariable Long productId) {
        return null;
    }
}
