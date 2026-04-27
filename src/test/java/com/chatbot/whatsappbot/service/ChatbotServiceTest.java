package com.chatbot.whatsappbot.service;

import com.chatbot.whatsappbot.model.MessageRequest;
import com.chatbot.whatsappbot.model.MessageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ChatbotService.
 * No Spring context is loaded — tests are fast and isolated.
 */
class ChatbotServiceTest {

    /** System Under Test */
    private final ChatbotService chatbotService = new ChatbotService();

    private MessageRequest buildRequest(String message) {
        MessageRequest req = new MessageRequest();
        req.setMessage(message);
        return req;
    }

    @Test
    @DisplayName("'Hi' should return 'Hello'")
    void testHiReturnsHello() {
        MessageResponse response = chatbotService.processMessage(buildRequest("Hi"));
        assertThat(response.getReply()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("'Bye' should return 'Goodbye'")
    void testByeReturnsGoodbye() {
        MessageResponse response = chatbotService.processMessage(buildRequest("Bye"));
        assertThat(response.getReply()).isEqualTo("Goodbye");
    }

    @Test
    @DisplayName("Unknown message should return \"I don't understand\"")
    void testUnknownReturnsDefault() {
        MessageResponse response = chatbotService.processMessage(buildRequest("What is the weather?"));
        assertThat(response.getReply()).isEqualTo("I don't understand");
    }

    @Test
    @DisplayName("Message matching should be case-insensitive")
    void testCaseInsensitive() {
        assertThat(chatbotService.processMessage(buildRequest("HI")).getReply()).isEqualTo("Hello");
        assertThat(chatbotService.processMessage(buildRequest("hi")).getReply()).isEqualTo("Hello");
        assertThat(chatbotService.processMessage(buildRequest("BYE")).getReply()).isEqualTo("Goodbye");
    }
}
