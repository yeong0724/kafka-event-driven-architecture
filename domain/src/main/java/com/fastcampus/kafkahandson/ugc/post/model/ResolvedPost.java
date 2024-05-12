package com.fastcampus.kafkahandson.ugc.post.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResolvedPost { // 서비스용 (Post + 그 외 Meta 정보 -> userName, categoryName)
    private Long id;

    private String title;

    private String content;

    private Long userId;

    private String userName;

    private Long categoryId;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean updated;

    public static ResolvedPost generate(
        Post post,
        String userName,
        String categoryName
    ) {
        return new ResolvedPost(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getUserId(),
            userName,
            post.getCategoryId(),
            categoryName,
            post.getCreatedAt(),
            post.getUpdatedAt(),
            !post.getCreatedAt().equals(post.getUpdatedAt())
        );
    }
}