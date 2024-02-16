package kr.co.hugetraffic.web.controller;

import jakarta.ws.rs.Path;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pay")
@Slf4j
public class PayController {

    private final PayService payService;

    /*
    결제 화면 진입
    주문 정보를 생성하고, 재고를 감소시킨다.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<OrderDto> readyToOrder(@PathVariable Long productId,
                                                 HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        // 주문정보 생성
        OrderDto dto = payService.create(userId, productId);
        return ResponseEntity.ok(dto);
    }
}
