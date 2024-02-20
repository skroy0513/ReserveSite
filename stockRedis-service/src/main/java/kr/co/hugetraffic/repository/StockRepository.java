package kr.co.hugetraffic.repository;

import kr.co.hugetraffic.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
