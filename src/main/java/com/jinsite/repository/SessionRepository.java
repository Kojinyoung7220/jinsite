package com.jinsite.repository;

import com.jinsite.domain.Session;
import com.jinsite.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 로그인시 세션이 생성되므로 세션을 저장하기 위한 저장소
 *  * AuthController -> AuthService -> UserRepository -> SessionRepository
 *                                              1     대     다
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByAccessToken(String accessToken);

}
