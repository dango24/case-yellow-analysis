package com.icarusrises.caseyellowanalysis.queues.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.icarusrises.caseyellowanalysis.queues.model.MessageType;

import javax.jms.JMSException;

public interface MessageProducerService {
    <T extends Object> void send(MessageType type, T payload) throws JMSException, JsonProcessingException;
}
