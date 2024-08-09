package com.jinsite.controller;

import com.jinsite.domain.Comment;
import com.jinsite.request.comment.CommentCreate;
import com.jinsite.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate request){
        commentService.write(postId, request);
    }

}
