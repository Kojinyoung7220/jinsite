package com.jinsite.config;

import com.jinsite.exception.PostNotFound;
import com.jinsite.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class JinSitePermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var post = postRepository.findById((Long) targetId)
                .orElseThrow(PostNotFound::new);

        if(!post.getUserId().equals(userPrincipal.getUserId())){
            log.info("[인가실패] 해당 사용자가 작성한 글이 아닙니다. targetId={} ", targetId);
            return false;
        }
        return true;
    }
}
