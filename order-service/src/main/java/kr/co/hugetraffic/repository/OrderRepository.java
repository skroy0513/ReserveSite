package kr.co.hugetraffic.repository;

import kr.co.hugetraffic.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
    Optional<Order> findByUserIdAndProductIdAndType(Long userId, Long productId, String type);
}
