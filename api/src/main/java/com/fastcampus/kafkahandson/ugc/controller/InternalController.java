package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.inspectedpost.model.InspectedPost;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fastcampus.kafkahandson.ugc.PostInspectUsecase;

@RestController
@RequestMapping("/internal")
public class InternalController {
    private final PostInspectUsecase postInspectUsecase;

    public InternalController(PostInspectUsecase postInspectUsecase) {
        this.postInspectUsecase = postInspectUsecase;
    }

    @GetMapping
    InspectedPost inspectionTest(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("categoryId") Long categoryId
    ) {
        return postInspectUsecase.inspectAndGetIfValid(
                Post.generate(
                    0L, // userId 는 검수 결과에 영향을 미치지 않으므로 아무거나 입력
                    title,
                    content,
                    categoryId
            )
        );
    }
}
