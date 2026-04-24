package com.phucitdev.pickleball_backend.modules.auth.service;

import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.repository.AccountRepository;
import com.phucitdev.pickleball_backend.modules.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        return new CustomUserDetails(account);
    }

}