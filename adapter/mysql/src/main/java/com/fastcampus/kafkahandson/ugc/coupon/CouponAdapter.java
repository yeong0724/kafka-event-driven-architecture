package com.fastcampus.kafkahandson.ugc.coupon;

import com.fastcampus.kafkahandson.ugc.coupon.model.Coupon;
import com.fastcampus.kafkahandson.ugc.coupon.model.ResolvedCoupon;
import com.fastcampus.kafkahandson.ugc.port.CouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fastcampus.kafkahandson.ugc.coupon.CouponEntityConverter.toCouponEntity;
import static com.fastcampus.kafkahandson.ugc.coupon.CouponEntityConverter.toCouponModel;

@RequiredArgsConstructor
@Component
public class CouponAdapter implements CouponPort {
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity couponEntity = couponJpaRepository.save(toCouponEntity(coupon));
        return toCouponModel(couponEntity);
    }

    @Override
    public List<ResolvedCoupon> listByUserId(Long userId) {
        List<CouponEntity> couponEntities = couponJpaRepository.findAllByUserId(userId);
        return couponEntities.stream().map(CouponEntityConverter::toResolvedCouponModel).toList();
    }
}
