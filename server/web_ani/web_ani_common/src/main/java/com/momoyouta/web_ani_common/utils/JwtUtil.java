package com.momoyouta.web_ani_common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    // 默认有效期为一天
    public static final Long JWT_TTL = 60 * 60 * 24 * 1000L;

    // 固定的 Base64 编码密钥
    private static final String SECRET_KEY_BASE = "momoyoutawebani";
    private static final SecretKey SECRET_KEY = generateSecretKey();

    private static SecretKey generateSecretKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(SECRET_KEY_BASE.getBytes(StandardCharsets.UTF_8));
            // 生成 SecretKey（使用 HMAC-SHA256）
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)  // 唯一id
                .setSubject(subject)  // 存储的数据
                .setIssuer("momoyouta")  // 创建者
                .setIssuedAt(now)  // 创建时间
                .signWith(SECRET_KEY)  // 签名
                .setExpiration(expDate);  // 过期时间
    }
    public static String generateToken(String subject) {
        return generateToken(subject, JWT_TTL);
    }

    public static String generateToken(String subject, Long ttlMillis) {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, ttlMillis, getUUID());
        return jwtBuilder.compact();
    }


    public static Claims parseJWT(String jwt) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
}
