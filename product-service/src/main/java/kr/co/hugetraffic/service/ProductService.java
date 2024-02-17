package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.StockClient;
import kr.co.hugetraffic.dto.PayDto;
import kr.co.hugetraffic.entity.OrderInfo;
import kr.co.hugetraffic.entity.Product;
import kr.co.hugetraffic.exception.NotEnoughStock;
import kr.co.hugetraffic.exception.NotFoundException;
import kr.co.hugetraffic.repository.OrderInfoRepository;
import kr.co.hugetraffic.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final StockClient stockClient;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
    }

    public int getStockById(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));

        return stockClient.getStock(productId);
    }

//    public OrderInfo getOrderInfo(Long userId, Long productId) {
//        OrderInfo orderInfo = orderInfoRepository.findOrderInfoByProductIdAndUserId(productId, userId)
//                .orElseThrow(() -> new NotFoundException("해당 상품의 주문정보가 없습니다."));
//        return orderInfo;
//    }

    public boolean isPreOrder(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        return product.isPreorder();
    }
}
