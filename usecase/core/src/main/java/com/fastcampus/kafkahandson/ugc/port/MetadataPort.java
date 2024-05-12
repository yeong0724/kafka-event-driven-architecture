package com.fastcampus.kafkahandson.ugc.port;

import java.util.List;

public interface MetadataPort {
    String getCategoryNameByCategoryId(Long categoryId);

    String getUserNameByUserId(Long userId);

    // 구독자 목록 정보
    List<Long> listFollowerIdsByUserId(Long userId);
}
