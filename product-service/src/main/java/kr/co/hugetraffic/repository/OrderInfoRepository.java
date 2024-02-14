package kr.co.hugetraffic.repository;

import kr.co.hugetraffic.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    Optional<OrderInfo> findOrderInfoByProductIdAndUserId(Long productId, Long userId);
}
