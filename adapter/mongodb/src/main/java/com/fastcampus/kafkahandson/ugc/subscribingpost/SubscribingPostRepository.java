package com.fastcampus.kafkahandson.ugc.subscribingpost;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscribingPostRepository extends SubscribingPostCustomRepository, MongoRepository<SubscribingPostDocument, String> {
}
