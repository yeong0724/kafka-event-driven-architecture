package com.fastcampus.kafkahandson.ugc.coupon;

import com.fastcampus.kafkahandson.ugc.port.CouponIssueRequestHistoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.fastcampus.kafkahandson.ugc.KeyPrefix.REQUEST_COUNT_HISTORY_KEY_PREFIX;
import static com.fastcampus.kafkahandson.ugc.KeyPrefix.USER_REQUEST_HISTORY_KEY_PREFIX;

@Component
@RequiredArgsConstructor
public class CouponIssueRequestHistoryAdapter implements CouponIssueRequestHistoryPort {
    private final RedisTemplate<String, String> redisTemplate;

    private static final Long EXPIRE_SECONDS = 60 * 60 * 24 * 7L;  // 일주일

    @Override
    public boolean setHistoryIfNotExists(Long couponEventId, Long userId) {
        // 발급된적없어서 정상적으로 redis 에 저장이 되면 false 반환
        return Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(
                this.generateUserRequestHistoryCacheKey(couponEventId, userId),
                "1",
                Duration.ofSeconds(EXPIRE_SECONDS)
        ));

        // return !redisTemplate.opsForValue().setIfAbsent(
        //         this.generateUserRequestHistoryCacheKey(couponEventId, userId),
        //         "1",
        //         Duration.ofSeconds(EXPIRE_SECONDS)
        // );
    }

    @Override
    public Long getRequestSequentialNumber(Long couponEventId) {
        String key = this.generateRequestCountHistoryCacheKey(couponEventId);

        /**
         * > 선착순 번호표 개념
         * 해당 key 에 대한 value 가 없다면 0L 부터 시작한다.
         * 그래 최초 요청이라면 1을 반환할 것이다.
         */
        Long requestSequentialNumber = redisTemplate.opsForValue().increment(key);

        if (requestSequentialNumber != null && requestSequentialNumber == 1) { // 만약 키가 처음 생성되었다면
            redisTemplate.expire(key, Duration.ofSeconds(EXPIRE_SECONDS)); // TTL 설정
        }

        return requestSequentialNumber;
    }

    private String generateUserRequestHistoryCacheKey(Long couponEventId, Long userId) {
        return USER_REQUEST_HISTORY_KEY_PREFIX + couponEventId + ":" + userId;
    }

    private String generateRequestCountHistoryCacheKey(Long couponEventId) {
        return REQUEST_COUNT_HISTORY_KEY_PREFIX + couponEventId;
    }
}
