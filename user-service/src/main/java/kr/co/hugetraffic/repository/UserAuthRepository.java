package kr.co.hugetraffic.repository;

import kr.co.hugetraffic.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
}
