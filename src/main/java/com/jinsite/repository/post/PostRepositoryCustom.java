package com.jinsite.repository.post;

import com.jinsite.domain.Post;
import com.jinsite.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
