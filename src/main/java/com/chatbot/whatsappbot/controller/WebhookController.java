package com.chatbot.whatsappbot.controller;

import com.chatbot.whatsappbot.model.MessageRequest;
import com.chatbot.whatsappbot.model.MessageResponse;
import com.chatbot.whatsappbot.service.ChatbotService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final ChatbotService chatbotService;

    public WebhookController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> handleWebhook(@Valid @RequestBody MessageRequest request) {
        log.debug("POST /webhook called with body: {}", request);
        MessageResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }
}
