package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.dto.ChangePasswordDto;
import kr.co.hugetraffic.dto.TokenInfoDto;
import kr.co.hugetraffic.service.FileService;
import kr.co.hugetraffic.service.UserService;
import kr.co.hugetraffic.web.form.UserForm;
import kr.co.hugetraffic.web.form.UserProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/")
    public String home(@RequestHeader HttpHeaders headers) {
        return "hello world";
    }

    @PostMapping("/register")
    public ResponseEntity<UserForm> register(@RequestBody UserForm user) {
        userService.join(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/mail/confirm")
    public ResponseEntity<String> confirm(@RequestParam("email") String email,
                                          @RequestParam("code") String code) {

        String result = String.valueOf(userService.emailConfirm(email, code));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfoDto> login(@RequestBody Map<String, String> user) {
        TokenInfoDto tokenInfoDto = userService.login(user);
        log.info("login success");
        return ResponseEntity.ok().body(tokenInfoDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader HttpHeaders headers) {
        String bearerToken = headers.get("Authorization").get(0);
        log.info("token : {}", bearerToken);
        boolean result = userService.logout(bearerToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePw(@RequestHeader HttpHeaders headers,
                                           @RequestBody ChangePasswordDto changePasswordDto) {
        String bearerToken = headers.get("Authorization").get(0);
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = userService.changePassword(bearerToken, userId, changePasswordDto);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/change-profile")
    public ResponseEntity<UserProfileForm> changeProfile(@RequestHeader HttpHeaders headers,
                                                         @RequestBody UserProfileForm userProfileDto) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        userService.changeMyProfile(userId, userProfileDto);
        return ResponseEntity.ok().body(userProfileDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        String name = fileService.upload(multipartFile);
        return ResponseEntity.ok().body(name);
    }

    @GetMapping("/feign/get")
    public ResponseEntity<String> getNickname(@RequestParam Long userId){
        String nickname = userService.getNickname(userId);
        return ResponseEntity.ok(nickname);
    }

}
