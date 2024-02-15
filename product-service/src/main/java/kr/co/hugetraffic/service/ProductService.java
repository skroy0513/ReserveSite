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

    /*
    결제 시도
     */
    public boolean buyProduct(Long userId, Long productId) {
        // 20%의 유저는 결제 실패(랜덤 확률)
        if(Math.random() <= 0.2) {
            // 실패시 재고 수량 증가
            stockClient.increaseStock(productId);
            throw new RuntimeException("결제에 실패하였습니다.");
        }
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
        // 해당 상품이 예약구매 상품이고 오픈 시간 전이면 접근 불가
        if (product.isPreorder()) {
            LocalDateTime now = LocalDateTime.now();
            if (now.compareTo(product.getOpenTime()) < 0) {
                throw new RuntimeException("아직 상품을 구매할 수 없습니다.");
            }
        }
        // 결제 준비 시점에 재고를 1감소 할 것
        stockClient.decreaseStock(productId);
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

    public void readyForPay(Long productId) {
        int stock = this.getStockById(productId);
        if (stock <= 0) {
            throw new NotEnoughStock("상품의 재고가 부족합니다.");
        }
    }
}
