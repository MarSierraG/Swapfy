package com.swapfy.backend.services;

import com.swapfy.backend.models.Message;
import com.swapfy.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesBySender(Long senderId) {
        return messageRepository.findBySender_UserId(senderId);
    }

    public List<Message> getMessagesByReceiver(Long receiverId) {
        return messageRepository.findByReceiver_UserId(receiverId);
    }

    public List<Message> getConversation(Long user1, Long user2) {
        return messageRepository.findBySender_UserIdAndReceiver_UserIdOrReceiver_UserIdAndSender_UserId(
                user1, user2, user1, user2
        );
    }
}
