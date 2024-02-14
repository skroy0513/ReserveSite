package kr.co.hugetraffic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "user_auth")
@Data
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {

    @Id
    @Column(name = "user_email", updatable = false)
    private String email;

    @Column(name = "user_code")
    private String code;
}
