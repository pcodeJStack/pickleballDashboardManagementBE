package com.phucitdev.pickleball_backend.modules.auth.security;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;
public class SecurityUtils {
    public static Account getCurrentAccount() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getAccount();
        }

        throw new RuntimeException("Account not authenticated");
    }
}