package kr.co.hugetraffic.service;

import kr.co.hugetraffic.client.StockDbClient;
import kr.co.hugetraffic.client.StockRedisClient;
import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.entity.Order;
import kr.co.hugetraffic.entity.OrderStatus;
import kr.co.hugetraffic.exception.NotFoundException;
import kr.co.hugetraffic.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final StockDbClient stockDbClient;
    private final StockRedisClient stockRedisClient;
    private final OrderRepository orderRepository;

    /*
    userId로 주문정보 가져오기
     */
    public List<OrderDto> getOrder(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        if (orders.size() <= 0) {
            throw new NotFoundException("주문 정보가 없습니다.");
        }
        List<OrderDto> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(OrderDto.convert(order));
        }
        return dtos;
    }

    /*
    주문정보 생성하기
     */
    public OrderDto createOrder(Long userId, Long productId, String type) {
        // 주문 정보가 있으면 기존 정보를 불러오고 없으면 생성 (orElse메서드 쓰기)
        Order order = orderRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> orderRepository.save(Order.builder()
                        .userId(userId)
                        .productId(productId)
                        .type(type)
                        .status(OrderStatus.PENDING.getOrderStatus()).build()));
        return OrderDto.convert(order);
    }

    /*
    주문 성공상태로 변환
     */
    public OrderDto successOrder(Long userId, Long productId) {
        Order order = orderRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException("주문정보가 없습니다."));
        if (order.getStatus().equals("success")) {
            if (order.getType().equals("GENERAL")) {
                stockDbClient.increaseStock(productId);
            } else if (order.getType().equals("PREORDER")) {
                stockRedisClient.increaseStock(productId);
            }
            throw new NotFoundException("이미 주문한 제품입니다.");
        }
        order.setStatus(OrderStatus.SUCCESS.getOrderStatus());
        orderRepository.save(order);
        return OrderDto.convert(order);
    }

    /*
    주문 실패상태로 변환
     */
    public OrderDto failOrder(Long userId, Long productId) {
        Order order = orderRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException("주문정보가 없습니다."));
        order.setStatus(OrderStatus.FAIL.getOrderStatus());
        orderRepository.save(order);
        return OrderDto.convert(order);
    }
}
