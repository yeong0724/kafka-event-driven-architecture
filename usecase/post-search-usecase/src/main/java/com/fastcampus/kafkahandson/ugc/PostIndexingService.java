package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.inspectedpost.model.InspectedPost;
import com.fastcampus.kafkahandson.ugc.port.PostSearchPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostIndexingService implements PostIndexingUsecase {
    private final PostSearchPort postSearchPort;

    @Autowired
    public PostIndexingService(PostSearchPort postSearchPort) {
        this.postSearchPort = postSearchPort;
    }

    @Override
    public void save(InspectedPost inspectedPost) {
        postSearchPort.indexPost(inspectedPost);
    }

    @Override
    public void delete(Long postId) {
        postSearchPort.deletePost(postId);
    }
}
