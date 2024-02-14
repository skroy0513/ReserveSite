package kr.co.hugetraffic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoDto {

    @NotNull
    private String grantType;

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;
}
