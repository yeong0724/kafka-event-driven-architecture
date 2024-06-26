package com.fastcampus.kafkahandson.ugc.resolvedpost;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.port.ResolvedPostCachePort;
import com.fastcampus.kafkahandson.ugc.post.model.ResolvedPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.fastcampus.kafkahandson.ugc.KeyPrefix.RESOLVED_POST_KEY_PREFIX;

@Component
@RequiredArgsConstructor
public class ResolvedPostCacheAdapter implements ResolvedPostCachePort {
    // private static final String KEY_PREFIX = "resolved_post:v2:";
    private static final Long EXPIRE_SECONDS = 60 * 60 * 24 * 7L;  // 일주일
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(ResolvedPost resolvedPost) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(resolvedPost);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
            this.generateCacheKey(resolvedPost.getId()),
            jsonString,
            Duration.ofSeconds(EXPIRE_SECONDS) // cache 의 유효기간
        );
    }

    @Override
    public ResolvedPost get(Long postId) {
        String jsonString = redisTemplate.opsForValue().get(this.generateCacheKey(postId));

        if (jsonString == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, ResolvedPost.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long postId) {
        redisTemplate.delete(this.generateCacheKey(postId));
    }

    @Override
    public List<ResolvedPost> multiGet(List<Long> postIds) {
        List<String> jsonStrings = redisTemplate.opsForValue().multiGet(postIds.stream().map(this::generateCacheKey).toList());
        if (jsonStrings == null) {
            return List.of();
        }

        return jsonStrings.stream().filter(Objects::nonNull).map(jsonString -> {
            try {
                return objectMapper.readValue(jsonString, ResolvedPost.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private String generateCacheKey(Long postId) {
        return RESOLVED_POST_KEY_PREFIX + postId;
    }
}
