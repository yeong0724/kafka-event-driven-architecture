package com.fastcampus.kafkahandson.ugc.coupon;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.coupon.model.CouponEvent;
import com.fastcampus.kafkahandson.ugc.port.CouponEventCachePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.fastcampus.kafkahandson.ugc.KeyPrefix.COUPON_EVENT_KEY_PREFIX;

@Component
@RequiredArgsConstructor
public class CouponEventCacheAdapter implements CouponEventCachePort {
    private final RedisTemplate<String, String> redisTemplate;

    private static final Long EXPIRE_SECONDS = 60 * 2L;  // 2분
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @Override
    public void set(CouponEvent couponEvent) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(couponEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
                this.generateCacheKey(couponEvent.getId()),
                jsonString,
                Duration.ofSeconds(EXPIRE_SECONDS) // 유효기간
        );
    }

    @Override
    public CouponEvent get(Long couponEventId) {
        String jsonString = redisTemplate.opsForValue().get(this.generateCacheKey(couponEventId));

        if (jsonString == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, CouponEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateCacheKey(Long couponEventId) {
        return COUPON_EVENT_KEY_PREFIX + couponEventId;
    }
}
