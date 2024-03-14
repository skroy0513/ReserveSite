package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.OrderClient;
import kr.co.hugetraffic.client.PreOrderProductClient;
import kr.co.hugetraffic.client.StockDbClient;
import kr.co.hugetraffic.client.StockRedisClient;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.exception.NotEnoughStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final PreOrderProductClient preOrderProductClient;
    private final StockDbClient stockDbClient;
    private final StockRedisClient stockRedisClientClient;
    private final OrderClient orderClient;
//    private final RedissonClient redissonClient;
//
//    private static final int WAIT_TIME = 1;
//    private static final int LEASE_TIME = 2;
//    private static final String STOCK_LOCK = "stock_lock";

    /*
    결제 화면 진입
     */
    @Transactional
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
    @Transactional
    public OrderDto preCreate(Long userId, Long productId) {
        // 서버의 시간대를 가져옴
        ZoneId serverZoneId = ZoneId.systemDefault();
        ZonedDateTime serverNow = ZonedDateTime.now();

        LocalDateTime opentime =preOrderProductClient.getOpenTime(productId);
        ZonedDateTime serverOpentime = opentime.atZone(serverZoneId);
        if (serverNow.isBefore(serverOpentime)) {
            log.info("현재 시각 : {}", serverNow);
            log.info("오픈 시각 : {}", serverOpentime);
            throw new RuntimeException("아직 상품을 구매할 수 없습니다.");
        }

        // Stock 모듈과 통신해서 재고가 있는지 확인, 없으면 에러
        int stock = stockDbClient.getPreStock(productId);
        if (stock <= 0) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        // Order 모듈과 통신하여 Order를 생성하고, dto를 받아올 것
        return orderClient.createOrder(productId, userId, "PREORDER");
    }

    /*
    결제 시도
     */
    @Transactional
    public OrderDto pay(Long userId, Long productId) {
        // 성공한 경우 db에서 재고를 줄일 것
        stockDbClient.decreaseStock(productId);
        // 주문 상태 성공으로 바꾸기
        return orderClient.successOrder(productId, userId, "GENERAL");
    }

    /*
    예약 상품 결제 시도
     */
    @Transactional
    public OrderDto prePay(Long userId, Long productId) {
        // 성공한 경우 redis에서 재고를 줄일 것
        stockRedisClientClient.decreaseStock(productId);
        // 주문상태 성공으로 바꾸기
        OrderDto dto = orderClient.successOrder(productId, userId, "PREORDER");
        return dto;
    }

//    public void decreaseStock(Long productId) {
//        RLock lock = redissonClient.getLock(STOCK_LOCK);
//
//        try {
//            if (!lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
//                log.error("lock획득 시도 실패");
//                throw new RuntimeException();
//            }
//            log.debug("lock 획득");
//            stockDbClient.decreasePreStock(productId);
//        } catch (InterruptedException e) {
//
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                log.info("lock 반납");
//            } else {
//                log.error("lock 소유하지 않은 스레드");
//                throw new RuntimeException();
//            }
//        }
//    }
}
