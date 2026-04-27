package com.chatbot.whatsappbot.service;

import com.chatbot.whatsappbot.model.MessageRequest;
import com.chatbot.whatsappbot.model.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ChatbotService {

    private static final Logger log = LoggerFactory.getLogger(ChatbotService.class);

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageResponse processMessage(MessageRequest request) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        log.info("[{}] Received message: '{}'", timestamp, request.getMessage());

        String reply = generateReply(request.getMessage().trim());

        log.info("[{}] Sending reply  : '{}'", timestamp, reply);

        return new MessageResponse(reply);
    }

    private String generateReply(String message) {
        return switch (message.toLowerCase()) {
            case "hi"  -> "Hello";
            case "bye" -> "Goodbye";
            default    -> "I don't understand";
        };
    }
}
