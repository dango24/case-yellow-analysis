package com.icarusrises.caseyellowanalysis.queues.services;

import com.amazon.sqs.javamessaging.SQSMessagingClientConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icarusrises.caseyellowanalysis.queues.model.MessageType;
import com.icarusrises.caseyellowanalysis.queues.model.QueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Slf4j
@Service
public class ImageAnalysisResultProducer implements MessageProducerService {

    @Value("${sqs.central.queue}")
    private String queueName;

    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public ImageAnalysisResultProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Retryable(value =  JmsException.class , maxAttempts = 10, backoff = @Backoff(delay = 5000))
    public <T extends Object> void send(MessageType type, T payload) throws JsonProcessingException {
        QueueMessage message = new QueueMessage(type, objectMapper.writeValueAsString(payload));
        jmsTemplate.send(queueName, (session) -> createMessage(session, message));
    }

    private Message createMessage(Session session, QueueMessage message) throws JMSException {
        try {
            Message createMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
            createMessage.setStringProperty(SQSMessagingClientConstants.JMSX_GROUP_ID, "messageGroup1");
            createMessage.setStringProperty(SQSMessagingClientConstants.JMS_SQS_DEDUPLICATION_ID, "1" + System.currentTimeMillis());
            createMessage.setStringProperty("documentType", message.getClass().getName());

            return createMessage;

        } catch (Exception | Error e) {
            log.error(String.format("Fail to send message %s", e.getMessage()));

            throw new JMSException(e.getMessage());
        }
    }
}
