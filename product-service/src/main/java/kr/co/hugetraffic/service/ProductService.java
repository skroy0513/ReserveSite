package kr.co.hugetraffic.service;

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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderInfoRepository orderInfoRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
    }

    public int getStrockById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));

        return product.getStock();
    }

    public boolean buyProduct(Long userId, Long productId) {
        // 해당 상품의 재고를 하나 줄인다.
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        if (product.getStock() <= 0 ) {
            throw new NotEnoughStock("재고가 없습니다.");
        }
        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        orderInfoRepository.save(OrderInfo.builder()
                .productId(productId)
                .userId(userId).build());
        return true;
    }

    /*
    결제 전 구매 정보
     */
    public PayDto preInfoPay(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 없습니다."));
        // userId를 사용해 유저의 이름을 가지고 와야하지만, 대용량트래픽 경험을 위해 userId를 사용
        return PayDto.builder()
                .userName(String.valueOf(userId))
                .price(product.getPrice())
                .stock(product.getStock())
                .productId(productId)
                .productName(product.getName())
                .build();
    }

    public OrderInfo getOrderInfo(Long userId, Long productId) {
        OrderInfo orderInfo = orderInfoRepository.findOrderInfoByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NotFoundException("해당 상품의 주문정보가 없습니다."));
        return orderInfo;
    }
}
