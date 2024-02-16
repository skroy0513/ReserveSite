package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.OrderClient;
import kr.co.hugetraffic.client.ProductClient;
import kr.co.hugetraffic.client.StockClient;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.exception.NotEnoughStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final ProductClient productClient;
    private final StockClient stockClient;
    private final OrderClient orderClient;

    /*
    결제 화면 진입
     */
    public OrderDto create(Long userId, Long productId) {
        // Product 모듈과 통신해서 예약구매인지 판별
        boolean isPreOrder = productClient.isPreOrder(productId);
        // 예약구매상품이라면 해당상품의 오픈시간과 현재 시간과 비교
        if (isPreOrder) {
            LocalDateTime opentime =productClient.getOpenTime(productId);
            LocalDateTime now = LocalDateTime.now();
            if (now.compareTo(opentime) < 0) {
                throw new RuntimeException("아직 상품을 구매할 수 없습니다.");
            }
        }

        // Stock 모듈과 통신해서 재고를 감소, 없다면 오류
        int stock = stockClient.decreaseStock(productId);
        if (stock < 0) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        // Order 모듈과 통신하여 Order를 생성하고, dto를 받아올 것
        OrderDto dto = orderClient.createOrder(userId, productId);
        return dto;
    }

    /*
    결제 시도
     */
    public OrderDto pay(Long userId, Long productId) {
        // 20%의 유저는 결제 실패(랜덤 확률)
        if(Math.random() <= 0.2) {
            // 실패시 재고 수량 증가
            stockClient.increaseStock(productId);
            orderClient.failOrder(userId, productId);
            throw new RuntimeException("결제에 실패하였습니다.");
        }
        OrderDto dto = orderClient.successOrder(userId, productId);
        return dto;
    }
}
