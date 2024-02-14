package kr.co.hugetraffic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String oldPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String checkPassword;
}
