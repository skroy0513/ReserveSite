package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /*
    userId로 주문정보 가져오기
     */
    @GetMapping("/my-order")
    public ResponseEntity<List<OrderDto>> getOrder(@RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        List<OrderDto> dto = orderService.getOrder(userId);
        return ResponseEntity.ok(dto);
    }

    /*
    해당 상품 주문정보 생성하기(status = pending)
     */
    @GetMapping("/feign/create/{productId}")
    public ResponseEntity<OrderDto> create(@PathVariable("productId") Long productId,
                                           @RequestParam("userId") Long userId,
                                           @RequestParam("type") String type) {
        OrderDto dto = orderService.createOrder(userId, productId, type);
        return ResponseEntity.ok(dto);
    }

    /*
    해당 상품 주문정보 성공하기(status = success)
     */
    @GetMapping("/feign/success/{productId}")
    public ResponseEntity<OrderDto> success(@PathVariable("productId") Long productId,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("type") String type) {
        OrderDto dto = orderService.successOrder(userId, productId, type);
        return ResponseEntity.ok(dto);
    }

    /*
    해당 상품 주문정보 실패하기(status = fail)
     */
    @GetMapping("/feign/fail/{productId}")
    public ResponseEntity<OrderDto> fail(@PathVariable("productId") Long productId,
                                         @RequestParam("userId") Long userId,
                                         @RequestParam("type") String type) {
        OrderDto dto = orderService.failOrder(userId, productId, type);
        return ResponseEntity.ok(dto);
    }
}
