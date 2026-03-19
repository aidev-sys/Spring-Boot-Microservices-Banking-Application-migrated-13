package org.training.sequence.generator.service;

import org.training.sequence.generator.model.entity.Sequence;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Sequence create() {
        Sequence sequence = new Sequence();
        sequence.setId(1L);
        sequence.setNextValue(1L);
        rabbitTemplate.convertAndSend("sequence.exchange", "sequence.routing.key", sequence);
        return sequence;
    }
}