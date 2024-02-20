package kr.co.hugetraffic.web.controller;

import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Path;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pay")
@Slf4j
public class PayController {

    private final PayService payService;

    /*
    결제 화면 진입
    주문 정보를 생성한다.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<OrderDto> readyToOrder(@PathVariable Long productId,
                                                 @RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        // 주문정보 생성
        OrderDto dto = payService.create(userId, productId);
        return ResponseEntity.ok(dto);
    }

    /*
    예약구매 결제 화면 진입
    주문 정보를 생성한다.
     */
    @GetMapping("/pre/{productId}")
    public ResponseEntity<OrderDto> readyToPreOrder(@PathVariable Long productId,
                                                    @RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        OrderDto dto = payService.preCreate(userId, productId);
        return ResponseEntity.ok(dto);
    }

    /*
    결제 시도
    주문 정보를 갱신하고, 상황에 맞춰 재고를 조절한다.
     */
    @PostMapping("/{productId}")
    public ResponseEntity<OrderDto> payToOrder(@PathVariable Long productId,
                                               @RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        log.info("userId : {}", userId);
        OrderDto dto = payService.pay(userId,productId);
        return ResponseEntity.ok(dto);
    }
}
