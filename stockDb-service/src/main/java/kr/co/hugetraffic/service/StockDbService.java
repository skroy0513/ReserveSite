package kr.co.hugetraffic.service;

import kr.co.hugetraffic.entity.PreOrderStock;
import kr.co.hugetraffic.entity.Stock;
import kr.co.hugetraffic.exception.NotEnoughStock;
import kr.co.hugetraffic.exception.NotFoundException;
import kr.co.hugetraffic.repository.PreOrderStockRepository;
import kr.co.hugetraffic.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockDbService {

    private final StockRepository stockRepository;
    private final PreOrderStockRepository preOrderStockRepository;
    @Value("${inventory.stock}")
    private int maxValue;

    /*
    일반 상품 재고 불러오기
     */
    public int getStockById(Long productId) {
        Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        return  stock.getStock();
    }

    /*
    예약 상품 재고 불러오기
     */
    public int getPreOrderStockById(Long productId) {
        PreOrderStock stock = preOrderStockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        return  stock.getStock();
    }

    /*
    db에 저장된 일반 상품 재고 감소
     */
    public int decreaseStock(Long productId) {
        Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        if (stock.getStock() <= 0) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        stock.setStock(stock.getStock() - 1);
        stockRepository.save(stock);
        return stock.getStock();
    }

    /*
    db에 저장된 일반 상품 재고 증가
     */
    public int increaseStock(Long productId) {
        Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        stock.setStock(stock.getStock() + 1);
        if (stock.getStock() > maxValue) {
            stock.setStock(maxValue);
        }
        stockRepository.save(stock);
        return stock.getStock();
    }

    /*
    db에 저장된 예약 상품 재고 감소
     */
    public int decreasePreStock(Long productId) {
        PreOrderStock stock = preOrderStockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        if (stock.getStock() <= 0) {
            throw new NotEnoughStock("재고가 부족합니다.");
        }
        stock.setStock(stock.getStock() - 1);
        preOrderStockRepository.save(stock);
        return stock.getStock();
    }

    /*
    redis의 재고를 db에 저장
     */
    public int updateStock(Long productId, int stock) {
        PreOrderStock dbstock = preOrderStockRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        dbstock.setStock(stock);
        preOrderStockRepository.save(dbstock);
        return dbstock.getStock();
    }
}
