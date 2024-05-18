package com.fastcampus.kafkahandson.ugc;

import com.fastcampus.kafkahandson.ugc.port.CouponIssueRequestPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestCouponIssueService implements RequestCouponIssueUsecase {
    private final CouponIssueRequestPort couponIssueRequestPort;

    @Override
    public void queue(Long couponEventId, Long userId) {
        couponIssueRequestPort.sendMessage(userId, couponEventId);
    }
}
