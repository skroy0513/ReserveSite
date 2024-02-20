package kr.co.hugetraffic.service;

import jakarta.ws.rs.NotFoundException;
import kr.co.hugetraffic.entity.Stock;
import kr.co.hugetraffic.exception.NotEnoughStock;
import kr.co.hugetraffic.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockRedisService {

    private final StockRepository stockRepository;
    private final RedisTemplate redisTemplate;
    @Value("${inventory.stock}")
    private int maxStock;

    public int getStockById(Long productId) {
        Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        return  stock.getStock();
    }

    public int decreaseStock(Long productId) {
        String productIdstr = String.valueOf(productId);
        Long stock = redisTemplate.opsForValue().decrement(productIdstr);
        int stockInt = stock.intValue();
        if (stockInt < 0) {
            redisTemplate.opsForValue().set(productIdstr, "0");
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        Stock dbstock = stockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        if (dbstock.getStock() <= 0 ) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        dbstock.setStock(dbstock.getStock() - 1);
        stockRepository.save(dbstock);
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
}
