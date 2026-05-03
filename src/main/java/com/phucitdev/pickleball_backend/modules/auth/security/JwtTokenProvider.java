package com.phucitdev.pickleball_backend.modules.auth.security;

import com.phucitdev.pickleball_backend.commo.exception.auth.InvalidDeviceIdException;
import com.phucitdev.pickleball_backend.commo.exception.auth.InvalidTokenException;
import com.phucitdev.pickleball_backend.commo.exception.auth.InvalidTokenTypeException;
import com.phucitdev.pickleball_backend.commo.exception.auth.TokenExpiredException;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.entity.RefreshToken;
import com.phucitdev.pickleball_backend.modules.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final long accessExp;
    private final long refreshExp;
    private static final String TOKEN_TYPE = "type";
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    private static final String ISSUER = "pickleball";
    public JwtTokenProvider(
            @Value("${jwt.access.secret}") String accessSecret,
            @Value("${jwt.refresh.secret}") String refreshSecret,
            @Value("${jwt.access.expiration}") long accessExp,
            @Value("${jwt.refresh.expiration}") long refreshExp,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
        this.accessExp = accessExp;
        this.refreshExp = refreshExp;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    @Autowired
    private HttpServletRequest request;
    private String getClientIp() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public String generateAccessToken(UUID accountId, String email) {
        return Jwts.builder()
                .setSubject(accountId.toString())
                .claim("email", email)
                .claim(TOKEN_TYPE, ACCESS)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExp))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Account account, String deviceId, String deviceInfo) {
        UUID deviceUUID;
        try {
            deviceUUID = UUID.fromString(deviceId);
        } catch (IllegalArgumentException e) {
            throw new InvalidDeviceIdException();
        }

        String refreshToken = Jwts.builder()
                .setSubject(account.getId() + "")
                .claim(TOKEN_TYPE, REFRESH)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .claim("deviceInfo", deviceInfo)
                .claim("ipAddress", getClientIp())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExp))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setAccount(account);
        entity.setCreatedAt(new Date());
        entity.setExpiredAt(new Date(System.currentTimeMillis() + refreshExp));
        entity.setRevoked(false);
        entity.setDeviceId(deviceUUID);
        entity.setDeviceInfo(deviceInfo);
        entity.setIpAddress( getClientIp());
        refreshTokenRepository.save(entity);
        return refreshToken;
    }

    private Claims parseWithKey(String token, SecretKey key) {
        return Jwts.parser()
                .requireIssuer(ISSUER)
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Claims getClaims(String token) {
        try {
            return parseWithKey(token, accessKey);
        } catch (JwtException e) {
            return parseWithKey(token, refreshKey);
        }
    }

    public UUID getAccountId(String token) {
        return UUID.fromString(getClaims(token).getSubject());
    }

    public String getTokenType(String token) {
        return getClaims(token).get(TOKEN_TYPE, String.class);
    }

    public boolean isAccessToken(String token) {
        return ACCESS.equals(getTokenType(token));
    }

    public boolean isRefreshToken(String token) {
        return REFRESH.equals(getTokenType(token));
    }

    public void validateAccessToken(String token) {
        try {
            Claims claims = getClaims(token);
            String type = claims.get(TOKEN_TYPE, String.class);
            if (!ACCESS.equals(type)) {
                throw new InvalidTokenTypeException();
            }
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    public void validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            String type = claims.get(TOKEN_TYPE, String.class);
            if(!REFRESH.equals(type)) {
                throw new InvalidTokenTypeException();
            }
        } catch (ExpiredJwtException e) {

            throw new TokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }
}