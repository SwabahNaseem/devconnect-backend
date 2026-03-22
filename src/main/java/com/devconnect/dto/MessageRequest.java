package com.devconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank(message = "Message text cannot be empty")
    private String text;

    public MessageRequest() {}

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}