package com.jinsite.repository;

import com.jinsite.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 괄호 안에는 처음엔 엔티티 그다음 id의 타입이 들어간다.
 * 인증 컨트롤러에서 바디로 넘어온 값이 login으로 저장이 되고 여기에 저장이된다
 * AuthController -> AuthService -> UserRepository
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

}
