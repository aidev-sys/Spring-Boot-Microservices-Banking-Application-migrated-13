package org.training.sequence.generator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.training.sequence.generator.model.entity.Sequence;
import org.training.sequence.generator.reporitory.SequenceRepository;
import org.training.sequence.generator.service.SequenceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {

    private final SequenceRepository sequenceRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Create a new account number.
     *
     * @return The newly created account number.
     */
    @Override
    public Sequence create() {

        log.info("creating a account number");
        return sequenceRepository.findById(1L)
                .map(sequence -> {
                    sequence.setAccountNumber(sequence.getAccountNumber() + 1);
                    Sequence savedSequence = sequenceRepository.save(sequence);
                    rabbitTemplate.convertAndSend("sequence.generated", savedSequence);
                    return savedSequence;
                }).orElseGet(() -> {
                    Sequence newSequence = sequenceRepository.save(Sequence.builder().accountNumber(1L).build());
                    rabbitTemplate.convertAndSend("sequence.generated", newSequence);
                    return newSequence;
                });
    }
}