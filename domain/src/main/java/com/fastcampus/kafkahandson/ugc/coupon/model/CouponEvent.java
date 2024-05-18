package com.fastcampus.kafkahandson.ugc.coupon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CouponEvent {
    private Long id; // coupon event 의 id
    private String displayName; // coupon 에 대한 노출 이름
    private LocalDateTime expiresAt; // coupon 만료 일시
    private Long issueLimit; // coupon 발급 제한 수

    @JsonIgnore
    public boolean isExpired() {
        return this.expiresAt.isBefore(LocalDateTime.now());
    }

    public static CouponEvent generate(
            String displayName,
            LocalDateTime expiresAt,
            Long issueLimit
    ) {
        return new CouponEvent(null, displayName, expiresAt, issueLimit);
    }
}
