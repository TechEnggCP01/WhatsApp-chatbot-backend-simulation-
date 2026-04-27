package com.chatbot.whatsappbot.model;

public class MessageResponse {

    private final String reply;

    public MessageResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }
}
