package com.jinsite.config;

import com.jinsite.domain.User;
import com.jinsite.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class jinSiteMockSecurityContext implements WithSecurityContextFactory<jinSiteMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(jinSiteMockUser annotation) {

        var user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();

        userRepository.save(user);

        var principal = new UserPrincipal(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
/**
 * principal: 인증된 사용자 정보 (UserPrincipal)
 * credentials: 사용자의 비밀번호 (user.getPassword())
 * authorities: 사용자의 권한 목록 (List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
 * //인증 되고 나서와 안되고 나서의 토큰의 매개변수가 변한다. 지금여기선 인증이 된 상태이기 때문에 3개가 들어간다.
 */
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal,
                user.getPassword(),
                List.of(role));

        SecurityContext context = SecurityContextHolder.createEmptyContext(); //빈 SecurityContext 객체를 생성하는 메서드 , SecurityContext는 요청마다 스레드 로컬(thread-local)에 저장되어, 요청이 끝날 때까지 해당 요청과 연관된 사용자 정보가 유지됩니다.


        context.setAuthentication(authenticationToken); //SecurityContext에 인증된 사용자 정보를 설정합니다.

        return context;

    }
}
