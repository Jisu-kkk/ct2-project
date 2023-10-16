package com.example.ct2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig{

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/semantic/**")
                .antMatchers("/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/main/**").permitAll()
            .antMatchers("/admin/login").permitAll()
            .anyRequest().authenticated();

        // login 설정
        http.csrf().disable()
            .formLogin()
            .loginPage("/admin/login")    // GET 요청 (login form을 보여줌)
            .loginProcessingUrl("/admin/auth")    // POST 요청 (login 창에 입력한 데이터를 처리)
            .usernameParameter("email")	// login에 필요한 id 값을 email로 설정 (default는 username)
            .passwordParameter("password")	// login에 필요한 password 값을 password(default)로 설정
            .defaultSuccessUrl("/admin/index")	// login에 성공하면 /admin/index로 redirect
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                    HttpServletResponse response,
                                                    Authentication authentication) throws IOException, ServletException {
                    System.out.println("authentication:: "+ authentication.getName());
                    response.sendRedirect("/admin/index");
                }
            })
            .failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                                    HttpServletResponse response,
                                                    AuthenticationException e) throws IOException, ServletException {
                    System.out.println("exception:: "+e.getMessage());
                    response.sendRedirect("/admin/login?error=true");
                }
            })
            .failureUrl("/admin/login?error=true");

        // logout 설정
        http
            .logout()
            .logoutUrl("/admin/logout");
        return http.build();
    }
}
