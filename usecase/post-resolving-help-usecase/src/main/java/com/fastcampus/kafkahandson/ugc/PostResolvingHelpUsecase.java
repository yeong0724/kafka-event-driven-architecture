package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;

import java.util.List;

public interface PostResolvingHelpUsecase {
    // 컨텐츠 상세
    ResolvedPost resolvePostById(Long postId);

    // 컨텐츠 목록 (구독페이지, 검색페이지)
    List<ResolvedPost> resolvePostsByIds(List<Long> postIds);
}
