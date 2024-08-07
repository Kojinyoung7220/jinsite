package com.jinsite.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * 우리가 엔티티로 만든 user를 반환 하면 안되고 시큐리티에서 제공해주는 UserDetails 라는
 * 것으로 반환을 해줘야 한다. 하지만 UserDetails 인터페이스고 구현하는 User를 상속받아서 만든
 * 클래스를 만들어서 반환 해주자. ,UserDetails를 구현해도 된다. 귀찮으니 user를만들자.
 */
public class UserPrincipal extends User {

    private final Long userId;

    //role : 역활 -> 관리자, 사용자 , 매니저
    //authority: 권한 -> 글쓰기, 글읽기, 사용자 정지 시키기

    public UserPrincipal(com.jinsite.domain.User user){
        super(user.getEmail(), user.getPassword(),
                List.of(    //회원가입시
                        new SimpleGrantedAuthority("ROLE_ADMIN") //ROLE_~~~ 이렇게 해야 역활
                ));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
