package com.fastcampus.kafkahandson.ugc.port;

import com.fastcampus.kafkahandson.ugc.inspectedpost.model.InspectedPost;

import java.util.List;

public interface PostSearchPort {
    // Save (ElasticSearch 에서 저장은 Indexing 을 의미)
    void indexPost(InspectedPost post);

    void deletePost(Long id);

    List<Long> searchPostIdsByKeyword(String keyword, int pageNumber, int pageSize);
}
