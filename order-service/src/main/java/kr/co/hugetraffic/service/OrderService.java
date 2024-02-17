package kr.co.hugetraffic.service;

import kr.co.hugetraffic.dto.OrderDto;
import kr.co.hugetraffic.entity.Order;
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

    private final OrderRepository orderRepository;

    /*
    userId로 주문정보 가져오기
     */
    public List<OrderDto> getOrder(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        List<OrderDto> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(OrderDto.convert(order));
        }
        return dtos;
    }
}
