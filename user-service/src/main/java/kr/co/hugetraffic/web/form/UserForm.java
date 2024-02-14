package kr.co.hugetraffic.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
    private String password;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일주소를 입력해주세요")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
}
