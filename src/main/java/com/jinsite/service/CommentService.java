package com.jinsite.service;

import com.jinsite.domain.Comment;
import com.jinsite.domain.Post;
import com.jinsite.exception.CommentNotFound;
import com.jinsite.exception.InvalidPassword;
import com.jinsite.exception.PostNotFound;
import com.jinsite.repository.comment.CommentRepository;
import com.jinsite.repository.post.PostRepository;
import com.jinsite.request.comment.CommentCreate;
import com.jinsite.request.comment.CommentDelete;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, @Valid CommentCreate request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        //일반 엔티티 형식으로 변환
        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);
    }

    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), encryptedPassword)){
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
