package org.training.user.service.service;

import org.training.user.service.model.User;
import org.training.user.service.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Integer createUser(User user) {
        User savedUser = userRepository.save(user);
        rabbitTemplate.convertAndSend("user.created", savedUser);
        return savedUser.getId();
    }

    public List<User> readUserByEmail(String emailId) {
        return userRepository.findByEmail(emailId);
    }

    public List<User> readUsers(List<String> authIds) {
        return userRepository.findByIdIn(authIds);
    }

    public User readUser(String authId) {
        Optional<User> user = userRepository.findById(authId);
        return user.orElse(null);
    }

    public void updateUser(User user) {
        userRepository.save(user);
        rabbitTemplate.convertAndSend("user.updated", user);
    }
}