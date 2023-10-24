package com.example.ct2.config;

import com.example.ct2.admin.common.service.UserService;
import com.example.ct2.admin.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public AuthProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UsernamePasswordAuthenticationToken token;
        UserVo userVo = userService.selectUserByEmail(email);

        if (userVo != null && passwordEncoder.matches(password, userVo.getPassword())) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("USER")); // 권한 부여

            token = new UsernamePasswordAuthenticationToken(userVo, null, roles);

            return token;
        }
        throw new BadCredentialsException("No such user or wrong password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
