package com.fci.chat.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class MessageLogReqDTO {
    @NotBlank
    String message;
    @NotNull
    Long messageTime;
    boolean sent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public Date getMessageTimeAsDate() {
        return new Date(messageTime);
    }
    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
