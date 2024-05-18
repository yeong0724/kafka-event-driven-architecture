package com.fastcampus.kafkahandson.ugc.postsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "post_v1")
public class PostDocument {
    /**
     * 유저정보는 검색에 필요한 정보가 아니라서 제외(Post 에 대한 정보중 명시적인 부분만 포함)
     */
    @Id
    private Long id;

    private String title;

    private String content;

    private String categoryName;

    private List<String> tags;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime indexedAt;
}
