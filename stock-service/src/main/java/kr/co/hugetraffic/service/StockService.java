package kr.co.hugetraffic.service;

import kr.co.hugetraffic.entity.Product;
import kr.co.hugetraffic.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final ProductRepository productRepository;

    public int getStockById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));
        return  product.getStock();
    }
}
