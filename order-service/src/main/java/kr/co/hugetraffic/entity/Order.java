package kr.co.hugetraffic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Data
@Table(name = "order_info")
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseDateTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_status")
    private String status;

    @Column(name = "product_type")
    private String type;

}
