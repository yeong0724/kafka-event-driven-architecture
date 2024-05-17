package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.Data;

import java.util.List;

public interface SubscribingPostListUsecase {
    // 구독자의 인박스에 있는 컨텐츠 목록을 조회한다.
    List<ResolvedPost> listSubscribingInboxPosts(Request request);

    @Data
    class Request {
        private final int pageNumber;
        private final Long followerUserId;
    }
}
