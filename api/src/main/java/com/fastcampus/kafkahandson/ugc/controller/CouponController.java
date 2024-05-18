package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.CouponIssueHistoryUsecase;
import com.fastcampus.kafkahandson.ugc.RequestCouponIssueUsecase;
import com.fastcampus.kafkahandson.ugc.model.CouponIssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponIssueHistoryUsecase couponIssueHistoryUsecase;
    private final RequestCouponIssueUsecase requestCouponIssueUsecase;

    @PostMapping
    ResponseEntity<String> issue(@RequestBody CouponIssueRequest couponIssueRequest) {
        Long couponEventId = couponIssueRequest.getCouponEventId();
        Long userId = couponIssueRequest.getUserId();

        boolean isFirstRequestFromUser = !couponIssueHistoryUsecase.isFirstRequestFromUser(couponEventId, userId);
        if (!isFirstRequestFromUser) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already tried to issue a coupon\n");
        }

        boolean hasRemainingCoupon = couponIssueHistoryUsecase.hasRemainingCoupon(couponEventId);
        if (!hasRemainingCoupon) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not enough available coupons\n");
        }

        requestCouponIssueUsecase.queue(couponEventId, userId);
        return ResponseEntity.ok().body("Successfully Issued\n");
    }
}
