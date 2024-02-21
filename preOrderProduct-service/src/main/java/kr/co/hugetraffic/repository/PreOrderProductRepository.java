package kr.co.hugetraffic.repository;

import kr.co.hugetraffic.entity.PreOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreOrderProductRepository extends JpaRepository<PreOrderProduct, Long> {
}
