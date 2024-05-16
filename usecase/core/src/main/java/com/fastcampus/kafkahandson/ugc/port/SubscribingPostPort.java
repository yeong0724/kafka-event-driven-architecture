package com.fastcampus.kafkahandson.ugc.port;

import com.fastcampus.kafkahandson.ugc.post.model.Post;

import java.util.List;

public interface SubscribingPostPort {
    // 콘텐츠가 발행되면 콘텐츠를, 구독하고 있는 모두으 구독함에 넣어준다.
    void addPostToFollowerInboxes(Post post, List<Long> followerUserIds);

    // 특정 구독자의 구독목록 화면에서, 그 구독자가 구독하고 있는 유저가 생산한 콘텐츠 목록을 본다.
    List<Long> listPostIdsByFollowerUserIdWithPagination(Long followerUserId, int pageNumber, int pageSize);
}
