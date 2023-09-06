package com.example.ct2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/semantic/**");
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
            .failureUrl("/admin/login");

        // logout 설정
        http
            .logout()
            .logoutUrl("/admin/logout")
            .logoutSuccessUrl("/admin/login");
        return http.build();
    }
}