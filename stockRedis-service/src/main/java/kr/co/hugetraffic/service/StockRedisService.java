package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.StockClient;
import kr.co.hugetraffic.exception.NotEnoughStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockRedisService {

    private final RedisTemplate redisTemplate;
    private final StockClient stockClient;
    @Value("${inventory.stock}")
    private int maxStock;

    public int decreaseStock(Long productId) {
        String productIdstr = String.valueOf(productId);
        Long stock = redisTemplate.opsForValue().decrement(productIdstr);
        int stockInt = stock.intValue();
        // redis의 재고가 0이 되면 db에 업데이트 하기
        if (stockInt == 0) {
            stockClient.updateStock(productId, stockInt);
        }
        if (stockInt < 0) {
            redisTemplate.opsForValue().set(productIdstr, "0");
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        return stock.intValue();
    }

    public int increaseStock(Long productId) {
        String productIdstr = String.valueOf(productId);
        Long stock = redisTemplate.opsForValue().increment(productIdstr);
        int stockInt = stock.intValue();
        if (stockInt > maxStock) {
            redisTemplate.opsForValue().set(productIdstr, String.valueOf(maxStock));
        }
        return stock.intValue();
    }

    public int getStockByProductId(Long productId) {
        String stockStr = (String) redisTemplate.opsForValue().get(String.valueOf(productId));
        int stock = Integer.valueOf(stockStr);
        return stock;
    }
}
