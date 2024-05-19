package com.fastcampus.kafkahandson.ugc;

public interface CouponIssueHistoryUsecase {
    // 쿠폰 신청자가 해당 쿠폰에 대해 최초 요청인지 확인
    boolean isFirstRequestFromUser(Long couponEventId, Long userId);

    // 발급 받을수 있는지 (선착순 내 요청인지 확인)
    boolean hasRemainingCoupon(Long couponEventId);
}
