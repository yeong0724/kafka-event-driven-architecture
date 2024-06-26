package com.fastcampus.kafkahandson.ugc.controller;

import com.fastcampus.kafkahandson.ugc.CouponIssueHistoryUsecase;
import com.fastcampus.kafkahandson.ugc.ListUsableCouponsUsecase;
import com.fastcampus.kafkahandson.ugc.RequestCouponIssueUsecase;
import com.fastcampus.kafkahandson.ugc.coupon.model.ResolvedCoupon;
import com.fastcampus.kafkahandson.ugc.model.CouponDto;
import com.fastcampus.kafkahandson.ugc.model.CouponIssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponIssueHistoryUsecase couponIssueHistoryUsecase;
    private final RequestCouponIssueUsecase requestCouponIssueUsecase;
    private final ListUsableCouponsUsecase listUsableCouponsUsecase;

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

    @GetMapping
    ResponseEntity<List<CouponDto>> listUsableCoupons(@RequestParam(name = "userId", defaultValue = "0", required = false) Long userId) {
        List<ResolvedCoupon> resolvedCoupons = listUsableCouponsUsecase.listByUserId(userId);
        return ResponseEntity.ok().body(resolvedCoupons.stream().map(this::toDto).toList());
    }

    private CouponDto toDto(ResolvedCoupon resolvedCoupon) {
        return new CouponDto(
                resolvedCoupon.getCoupon().getId(),
                resolvedCoupon.getCouponEvent().getDisplayName(),
                resolvedCoupon.getCouponEvent().getExpiresAt()
        );
    }
}
