package kr.co.hugetraffic.dto;

import kr.co.hugetraffic.entity.Order;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long userId;
    private Long productId;
    private String status;

    public static OrderDto convert(Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .status(order.getStatus())
                .build();
    }
}

