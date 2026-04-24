package com.phucitdev.pickleball_backend.modules.auth.security;
import com.phucitdev.pickleball_backend.commo.exception.auth.*;
import com.phucitdev.pickleball_backend.modules.auth.repository.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtils jwtUtils;
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AccountRepository accountRepository, JwtUtils jwtUtils, RedisTemplate<String, Object> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.redisTemplate = redisTemplate;
    }
    private void handleAuthError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String json = """
            {
                "code": 401,
                "message": "%s",
                "data": null
            }
            """.formatted(message);
        response.getWriter().write(json);
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.startsWith("/ws");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            if (isPublicEndpoint(path)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = jwtUtils.resolveToken(request);
            if (token == null) {
                throw new MissingTokenException();
            }
            jwtTokenProvider.validateAccessToken(token);
            UUID accountId = jwtTokenProvider.getAccountId(token);

            var account = accountRepository.findById(accountId)
                    .orElseThrow(AccountNotFoundException::new);

            CustomUserDetails userDetails = new CustomUserDetails(account);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (MissingTokenException ex) {
            handleAuthError(response, "Token is missing");

        } catch (TokenExpiredException ex) {
            handleAuthError(response, "Access token expired");

        } catch (InvalidTokenTypeException ex) {
            handleAuthError(response, "Invalid token type");

        } catch (InvalidTokenException ex) {
            handleAuthError(response, "Invalid token");

        } catch (Exception ex) {
            handleAuthError(response, "Unauthorized");
        }
    }
}