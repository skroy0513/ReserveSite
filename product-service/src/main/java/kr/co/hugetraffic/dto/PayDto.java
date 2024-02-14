package kr.co.hugetraffic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayDto {
    private String userName;
    private Long productId;
    private String productName;
    private int price;
    private int stock;

}
