package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.OrderClient;
import kr.co.hugetraffic.client.PreOrderProductClient;
import kr.co.hugetraffic.client.StockDbClient;
import kr.co.hugetraffic.client.StockRedisClient;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.exception.NotEnoughStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final PreOrderProductClient preOrderProductClient;
    private final StockDbClient stockDbClient;
    private final StockRedisClient stockRedisClientClient;
    private final OrderClient orderClient;

    /*
    결제 화면 진입
     */
    public OrderDto create(Long userId, Long productId) {
        // Stock 모듈과 통신해서 재고가 있는지 확인, 없으면 에러
        int stock = stockDbClient.getStock(productId);
        if (stock <= 0) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        // Order 모듈과 통신하여 Order를 생성하고, dto를 받아올 것
        return orderClient.createOrder(productId, userId, "GENERAL");
    }

    /*
    예약 상품 결제 화면 진입
     */
    public OrderDto preCreate(Long userId, Long productId) {
        LocalDateTime opentime =preOrderProductClient.getOpenTime(productId);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(opentime)) {
            throw new RuntimeException("아직 상품을 구매할 수 없습니다.");
        }

        // Stock 모듈과 통신해서 재고가 있는지 확인, 없으면 에러
        int stock = stockDbClient.getStock(productId);
        if (stock <= 0) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        // Order 모듈과 통신하여 Order를 생성하고, dto를 받아올 것
        return orderClient.createOrder(productId, userId, "PREORDER");
    }

    /*
    결제 시도
     */
    public OrderDto pay(Long userId, Long productId) {
        // 20%의 유저는 결제 실패(랜덤 확률)
        if(Math.random() <= 0.2) {
            orderClient.failOrder(productId, userId);
            throw new RuntimeException("결제에 실패하였습니다.");
        }
        // 성공한 경우 db에서 재고를 줄일 것
        stockDbClient.decreaseStock(productId);
        OrderDto dto = orderClient.successOrder(productId, userId);
        return dto;
    }

    /*
    예약 상품 결제 시도
     */
    public OrderDto prePay(Long userId, Long productId) {
        // 20%의 유저는 결제 실패(랜덤 확률)
        if(Math.random() <= 0.2) {
            orderClient.failOrder(productId, userId);
            throw new RuntimeException("결제에 실패하였습니다.");
        }
        // 성공한 경우 redis, db에서 재고를 줄일 것
        stockRedisClientClient.decreaseStock(productId);
        OrderDto dto = orderClient.successOrder(productId, userId);
        stockDbClient.decreasePreStock(productId);
        return dto;
    }
}
