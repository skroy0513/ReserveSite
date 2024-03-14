package kr.co.hugetraffic.repository;

import jakarta.persistence.LockModeType;
import kr.co.hugetraffic.entity.PreOrderStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PreOrderStockRepository extends JpaRepository<PreOrderStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ps FROM PreOrderStock ps WHERE ps.id = :productId")
    Optional<PreOrderStock> findByIdWithPessimisticLock(Long productId);
}
