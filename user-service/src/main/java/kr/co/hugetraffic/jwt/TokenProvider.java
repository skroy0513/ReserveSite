package kr.co.hugetraffic.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private final Key key;
    // 토큰 유효시간 30분
    private final long tokenValidTime = 30 * 60 * 1000L;
    // 리프레시토큰 유효시간 6시간
    private final long refreshTokenValidTime = 60 * 60 * 1000L * 6;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate redisTemplate;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService userDetailsService, RedisTemplate redisTemplate) {
        byte [] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * jwt 생성
     * 유저정보를 통해 AccessToken을 생성
     * @param authentication 인증정보 객체
     * @return TokenInfoDto객체
     */
    public String createAccessToken(Authentication authentication, Long userId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenValidTime);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())  // 유저의 email
                .claim("auth", authorities)
                .claim("id", userId)
                .setIssuedAt(now)// 유저의 권한
                .setExpiration(expireDate)  // 만료시간 (30분)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return accessToken;
    }

    /**
     * accessToken을 발급할 때 사용한 유저정보로 refreshToken 같이 발급
     * @param authentication
     * @return
     */
    public String createRefreshToken(Authentication authentication, Long userId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenValidTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .claim("auth", authorities)
                .claim("id", userId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();


        return refreshToken;
    }

    /**
     * 현재 만료시간 확인
     * @param accessToken
     * @return
     */
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();

        return expiration.getTime() - now;
    }
}
