package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.StockClient;
import kr.co.hugetraffic.entity.PreOrderProduct;
import kr.co.hugetraffic.exception.NotFoundException;
import kr.co.hugetraffic.repository.PreOrderProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreOrderProductService {

    private final PreOrderProductRepository preOrderProductRepository;
    private final StockClient stockClient;

    public List<PreOrderProduct> getAllProducts() {
        return preOrderProductRepository.findAll();
    }

    public PreOrderProduct getProductById(Long productId) {
        return preOrderProductRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
    }

    public int getStockById(Long productId) {
        preOrderProductRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));

        return stockClient.getStock(productId);
    }

    public LocalDateTime getOpenTime(Long productId) {
        PreOrderProduct product = preOrderProductRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        return product.getOpenTime();
    }
}
