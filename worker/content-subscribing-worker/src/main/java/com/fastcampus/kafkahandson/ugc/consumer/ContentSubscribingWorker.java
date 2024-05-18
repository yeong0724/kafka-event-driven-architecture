package com.fastcampus.kafkahandson.ugc.consumer;

import com.fastcampus.kafkahandson.ugc.CustomObjectMapper;
import com.fastcampus.kafkahandson.ugc.SubscribingPostAddToInboxUsecase;
import com.fastcampus.kafkahandson.ugc.SubscribingPostRemoveFromInboxUsecase;
import com.fastcampus.kafkahandson.ugc.adapter.common.OperationType;
import com.fastcampus.kafkahandson.ugc.adapter.common.Topic;
import com.fastcampus.kafkahandson.ugc.adapter.inspectedpost.InspectedPostMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContentSubscribingWorker {
    private final SubscribingPostAddToInboxUsecase subscribingPostAddToInboxUsecase;
    private final SubscribingPostRemoveFromInboxUsecase subscribingPostRemoveFromInboxUsecase;

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @KafkaListener(
            topics = { Topic.INSPECTED_POST },
            groupId = "subscribing-post-consumer-group",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> message) throws JsonProcessingException {
        InspectedPostMessage inspectedPostMessage = objectMapper.readValue(message.value(), InspectedPostMessage.class);

        OperationType operationType = inspectedPostMessage.getOperationType();

        if (operationType == OperationType.CREATE) {
            this.handleCreate(inspectedPostMessage);
        }

        if (operationType == OperationType.DELETE) {
            this.handleDelete(inspectedPostMessage);
        }

        if (operationType == OperationType.UPDATE) {
            // DO NOTHING
        }
    }

    private void handleCreate(InspectedPostMessage inspectedPostMessage) {
        subscribingPostAddToInboxUsecase.saveSubscribingInboxPost(inspectedPostMessage.getPayload().getPost());
    }

    private void handleDelete(InspectedPostMessage inspectedPostMessage) {
        subscribingPostRemoveFromInboxUsecase.deleteSubscribingInboxPost(inspectedPostMessage.getId());
    }
}
