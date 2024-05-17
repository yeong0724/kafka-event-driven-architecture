package com.fastcampus.kafkahandson.ugc.postsearch;

import com.fastcampus.kafkahandson.ugc.inspectedpost.model.InspectedPost;
import com.fastcampus.kafkahandson.ugc.post.model.Post;

import java.time.LocalDateTime;

public class PostDocumentConverter {
    public static PostDocument toDocument(InspectedPost inspectedPost) {
        Post post = inspectedPost.getPost();
        return new PostDocument(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                inspectedPost.getCategoryName(),
                inspectedPost.getAutoGeneratedTags(),
                LocalDateTime.now()
        );
    }
}
