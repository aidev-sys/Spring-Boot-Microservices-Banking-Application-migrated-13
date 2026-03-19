package org.training.user.service.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.training.user.service.config.RabbitMQConfig;
import org.training.user.service.service.PostgresService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostgresServiceImpl implements PostgresService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Creates a new user in the Postgres database.
     *
     * @param  userRepresentation  the user representation object containing the user details
     * @return                     the status code indicating the success or failure of the user creation
     */
    @Override
    public Integer createUser(Object userRepresentation) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_CREATE_ROUTING_KEY, userRepresentation);
        return 201;
    }

    /**
     * Retrieves a list of UserRepresentation objects based on the provided email ID.
     *
     * @param  emailId  the email ID of the user
     * @return          a list of UserRepresentation objects
     */
    @Override
    public List<Object> readUserByEmail(String emailId) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_READ_BY_EMAIL_ROUTING_KEY, emailId);
        // Simulate response from Postgres
        return List.of();
    }

    /**
     * Retrieves a list of user representations based on the provided authentication IDs.
     *
     * @param  authIds  a list of authentication IDs
     * @return          a list of user representations
     */
    @Override
    public List<Object> readUsers(List<String> authIds) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_READ_BATCH_ROUTING_KEY, authIds);
        // Simulate response from Postgres
        return List.of();
    }

    /**
     * Retrieves a user representation based on the provided authentication ID.
     *
     * @param  authId  the authentication ID of the user
     * @return         the user representation object
     */
    @Override
    public Object readUser(String authId) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_READ_BY_ID_ROUTING_KEY, authId);
        // Simulate response from Postgres
        return new Object();
    }

    /**
     * Updates the user with the provided user representation.
     *
     * @param  userRepresentation  the user representation to update the user with
     */
    @Override
    public void updateUser(Object userRepresentation) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_UPDATE_ROUTING_KEY, userRepresentation);
    }
}