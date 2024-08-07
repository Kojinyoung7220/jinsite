package com.jinsite.config;

import com.jinsite.config.handler.Http401Handler;
import com.jinsite.config.handler.Http403Handler;
import com.jinsite.config.handler.LoginFailHandler;
import com.jinsite.domain.User;
import com.jinsite.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@Slf4j
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico", "/error")
                .requestMatchers(toH2Console());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login").permitAll() // 로그인 페이지는 누구나 접근 가능하게 설정
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/user").hasRole("USER") // "/user"로 접근했을때 유저와 어드민 둘다 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN") // /admin으로 접근했을때 ADMIN만 접근 가능함
                        .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                ).formLogin(configurer -> configurer //폼 기반 로그인 기능을 활성화
                                .loginPage("/auth/login") //사용자 정의 로그인 페이지의 URL을 지정 /기본값 = /login
                                .loginProcessingUrl("/auth/login") //사용자가 로그인 폼을 제출할 때 이 URL로 POST 요청을 보냅니다.
                                .usernameParameter("username") //로그인 폼에서 사용자 이름 필드의 이름 지정
                                .passwordParameter("password") //로그인 폼에서 비밀번호 필드의 이름을 지정
                                .defaultSuccessUrl("/") // 로그인 성공 후 사용자를 리다이렉트할 URL을 지정
                                .failureHandler(new LoginFailHandler()) //로그인실패시 여기로 가라~
                        )
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler());
                            e.authenticationEntryPoint(new Http401Handler());
                })
                //웹사이트 로그인 할 때 자동로그인 하기 이런 버튼 느낌.
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                )
                .csrf(AbstractHttpConfigurer::disable); // CSRF 보호 비활성화
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username +"을 찾을 수 없습니다."));

                //우리가 엔티티로 만든 user를 반환 하면 안되고 시큐리티에서 제공해주는 UserDetails 라는
                //것으로 반환을 해줘야 한다. 하지만 UserDetails 인터페이스고 구현하는 User를 상속받아서 만든
                //클래스를 만들어서 반환 해주자.
                return new UserPrincipal(user);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new SCryptPasswordEncoder(
                16,
                8,
                1,
                32,
                64);

    }
}
