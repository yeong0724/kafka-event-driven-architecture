package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.port.PostSearchPort;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchService implements PostSearchUsecase {
    private final PostSearchPort postSearchPort;
    private final PostResolvingHelpUsecase postResolvingHelpUsecase;

    private static final int PAGE_SIZE = 5;

    @Override
    public List<ResolvedPost> getSearchResultByKeyword(String keyword, int pageNumber) {
        List<Long> postIds = postSearchPort.searchPostIdsByKeyword(keyword, pageNumber, PAGE_SIZE);
        return postResolvingHelpUsecase.resolvePostsByIds(postIds);
    }
}
