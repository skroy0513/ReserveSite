package kr.co.hugetraffic.repository;

import kr.co.hugetraffic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
