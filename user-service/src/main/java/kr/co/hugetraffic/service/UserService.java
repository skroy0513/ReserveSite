package kr.co.hugetraffic.service;

import kr.co.hugetraffic.dto.ChangePasswordDto;
import kr.co.hugetraffic.dto.TokenInfoDto;
import kr.co.hugetraffic.entity.User;
import kr.co.hugetraffic.entity.UserAuth;
import kr.co.hugetraffic.exception.DuplicatedEmailException;
import kr.co.hugetraffic.exception.PasswordNotMatches;
import kr.co.hugetraffic.exception.UserNameNotFoundException;
import kr.co.hugetraffic.jwt.TokenProvider;
import kr.co.hugetraffic.repository.UserAuthRepository;
import kr.co.hugetraffic.repository.UserRepository;
import kr.co.hugetraffic.web.form.UserForm;
import kr.co.hugetraffic.web.form.UserProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    public User join(UserForm user) {
        Optional<User> optUser = userRepository.findByEmail(user.getEmail());
        if (optUser.isPresent()) {
            throw new DuplicatedEmailException(user.getEmail());
        }

        String code = mailService.sendMail(user.getEmail());
        userAuthRepository.save(UserAuth.builder().email(user.getEmail())
                .code(code)
                .build());

        return userRepository.save(User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .name(user.getName())
                .disabled(true)
                .build());
    }

    public String emailConfirm(String email, String code) {
        Optional<UserAuth> optUserAuth = userAuthRepository.findById(email);
        UserAuth userAuth = optUserAuth.get();
        if(code.equals(userAuth.getCode())) {
            Optional<User> optUser = userRepository.findByEmail(email);
            User user = optUser.get();
            user.setDisabled(false);
            userRepository.save(user);
            return "활성";
        };

        return "인증실패";
    }

    public TokenInfoDto login(Map<String, String> user) {
        User loginUser = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new UserNameNotFoundException("아이디를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(user.get("password"), loginUser.getPassword())) {
            throw new PasswordNotMatches("잘못된 비밀번호입니다.");
        }

        if (loginUser.isDisabled()) {
            throw new RuntimeException("정지당한 계정입니다.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.get("email"), user.get("password"));
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = tokenProvider.createAccessToken(authentication, loginUser.getId());
        String refreshToken = tokenProvider.createRefreshToken(authentication, loginUser.getId());

        Long expiration = tokenProvider.getExpiration(refreshToken);

        redisTemplate.opsForValue().set(accessToken, refreshToken, expiration, TimeUnit.MILLISECONDS);

        return TokenInfoDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean logout(String bearerToken) {
        // 토큰의 유효성 검사
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            String accessToken = bearerToken.substring(7);
            // redis에 key : "현재토큰" value : "logout" 데이터로 저장
            // 만료기간은 현재토큰의 만료일
            Long expiration = tokenProvider.getExpiration(accessToken);

            redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    public String changePassword(String bearerToken, Long userId, ChangePasswordDto changePasswordDto) {
        // 토큰의 유효성 검사
//        String accessToken = null;
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
//            accessToken = bearerToken.substring(7);
//        }
//        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new UserNameNotFoundException("아이디를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatches("잘못된 비밀번호입니다.");
        }

        // 새로운 비밀번호가 일치하는 지 확인
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getCheckPassword())) {
            throw new PasswordNotMatches("새 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호를 User정보에 업데이트
        user.updatePassword(changePasswordDto.getNewPassword());
        String nPwd = passwordEncoder.encode(changePasswordDto.getNewPassword());
        user.setPassword(nPwd);
        userRepository.save(user);

        // 로그아웃
        logout(bearerToken);

        return "Success";
    }

    public void changeMyProfile(Long userId, UserProfileForm userProfileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNameNotFoundException("아이디를 찾을 수 없습니다."));
        user.setNickname(userProfileDto.getNickname());
        user.setProfileImg(userProfileDto.getProfileImg());
        user.setGreetings(userProfileDto.getGreetings());
        userRepository.save(user);
    }

    public String getNickname(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNameNotFoundException("사용자를 찾을 수 없습니다."));
        return user.getNickname();
    }
}