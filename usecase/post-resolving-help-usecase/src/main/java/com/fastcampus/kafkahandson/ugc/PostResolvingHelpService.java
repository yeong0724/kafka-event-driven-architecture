package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.port.MetadataPort;
import com.fastcampus.kafkahandson.ugc.port.PostPort;
import com.fastcampus.kafkahandson.ugc.port.ResolvedPostCachePort;
import com.fastcampus.kafkahandson.ugc.post.model.Post;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostResolvingHelpService implements PostResolvingHelpUsecase {
    private final PostPort postPort; // 핵심 원천 데이터
    private final MetadataPort metadataPort; // 부가 데이터
    private final ResolvedPostCachePort resolvedPostCachePort; // 캐시 데이터

    @Autowired
    public PostResolvingHelpService(PostPort postPort, MetadataPort metadataPort, ResolvedPostCachePort resolvedPostCachePort) {
        this.postPort = postPort;
        this.metadataPort = metadataPort;
        this.resolvedPostCachePort = resolvedPostCachePort;
    }

    @Override
    public ResolvedPost resolvePostById(Long postId) {
        ResolvedPost resolvedPost = resolvedPostCachePort.get(postId);

        System.out.println("[PostResolvingHelpService] resolvePostById :: resolvedPost = " + resolvedPost);
        if (resolvedPost != null) {
            return resolvedPost;
        }

        Post post = postPort.findById(postId);
        if (post != null) {
            resolvedPost = this.resolvePost(post);
        }
        return resolvedPost;
    }

    @Override
    public List<ResolvedPost> resolvePostsByIds(List<Long> postIds) {
        // TODO: 임시이므로 수정 필요
        return postIds.stream().map(this::resolvePostById).toList();
    }

    @Override
    public void resolvePostAndSave(Post post) {
        ResolvedPost resolvedPost = this.resolvePost(post);
        if (resolvedPost != null) {
            resolvedPostCachePort.set(resolvedPost);
        }
    }

    @Override
    public void deleteResolvedPost(Long postId) {
        resolvedPostCachePort.delete(postId);
    }

    private ResolvedPost resolvePost(Post post) {
        if (post == null) return null;
        ResolvedPost resolvedPost = null;
        String userName = metadataPort.getUserNameByUserId(post.getUserId());
        String categoryName = metadataPort.getCategoryNameByCategoryId(post.getCategoryId());
        if (userName != null && categoryName != null) {
            resolvedPost = ResolvedPost.generate(
                    post,
                    userName,
                    categoryName
            );
            resolvedPostCachePort.set(resolvedPost);
        }
        return resolvedPost;
    }
}
