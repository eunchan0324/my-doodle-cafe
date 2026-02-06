package com.cafe.order.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j // 로그 출력을 위해 사용 (sout 대신 log.info 등을 씀)
@Component // 스프링 빈으로 등록
public class JwtTokenProvider { ;

    // 토큰 유효 시간: 1시간 (밀리초 단위: 1000 * 60 * 60)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60L;
    private Key key;

    // 생성자 : 클래스가 만들어질 때 비밀키를 세팅
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 토큰 생성 (Create)
     * - 로그인 성공 시 호출되어 "출입증" 을 발급
     */
    public String createToken(String loginId, String role) {
        Date now = new Date(); // 현재 시간
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME); // 만료 시간 계산

        return Jwts.builder()
                .setSubject(loginId)                     // 1. 토근 주인 (sub): 아이디
                .claim("role", role)                   // 2. 사용자 권한 (커스텀 데이터): "ADMIN", "CUSTOMER" 등)
                .setIssuedAt(now)                        // 3. 발급 시간(iat)
                .setExpiration(expiryDate)               // 4. 만료 시간 (exp)
                .signWith(key, SignatureAlgorithm.HS256) // 5. 서명 (도장 찍기): 비밀키 + HS256 알고리즘
                .compact();                              // 문자열로 변환하여 반환
    }

    /**
     * 토큰 검증 (Validate)
     * - 사용자가 가져온 토큰이 위조되지 않았는지, 만료되지 않았는지 확인
     */
    public boolean validateToken(String token) {
        try {
            // 비밀키로 토큰 열기 (파싱)
            Jwts.parserBuilder()
                    .setSigningKey(key) // 비밀키 세팅
                    .build()
                    .parseClaimsJws(token); // 여기서 위조/만료시 예외 터짐
            return true; // 아무 예외도 없으면 유효한 토큰
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 예외가 하나라도 터지면 false
    }

    /**
     * 토큰에서 아이디 추출 (Get LoginId)
     * - 검증된 토큰에서 "이게 누구 건지" 정보를 꺼낼 때 사용
     */
    public String getLoginId(String token) {
        // 토큰을 비밀키로 열어서(parse) 안의 내용(Body)을 꺼내고 -> 주인(Subject)을 가져옴
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰의 남은 유효 시간(ms)을 계산하여 반환
     * - 로그아웃 시 블랙리스트에 저장할 유효 시간을 결정하기 위해 사용합니다.
     */
    public Long getExpiration(String token) {
        // 1. 토큰을 열어서 만료 시간(Expiration) 정보를 가져옴
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        
        // 2. 현재 시간 가져오기
        long now = new Date().getTime();
        
        // 3. (만료 시간 - 현재 시간) = 남은 시간
        return (expiration.getTime() - now);
    }
}
