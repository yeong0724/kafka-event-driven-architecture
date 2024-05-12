package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.model.PostListDto;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/list")
public class PostListController {
    @GetMapping("/inbox/{userId}")
    ResponseEntity<List<PostListDto>> listSubscribingPosts(@PathVariable("userId") Long userId) {

        return ResponseEntity.internalServerError().build(); // 500 Error
    }

    @GetMapping("/search")
    ResponseEntity<List<PostListDto>> searchPosts(@RequestParam("query") String query) {

        return ResponseEntity.internalServerError().build(); // 500 Error
    }

    private PostListDto toDto(ResolvedPost resolvedPost) {
        return new PostListDto(
                resolvedPost.getId(),
                resolvedPost.getTitle(),
                resolvedPost.getUserName(),
                resolvedPost.getCreatedAt()
        );
    }
}
