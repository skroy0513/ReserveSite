package kr.co.hugetraffic.web.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileForm {

    private String nickname;
    private String profileImg;
    private String greetings;
}
