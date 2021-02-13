package com.fci.chat.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "MESSAGE_TBL")
public class MessageTbl {
    @Id
    @Column(name = "MESSAGE_ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    int messageId;

    @NotNull
    @Column(name = "MESSAGE")
    String message;

    @NotNull
    @Column(name = "USER_ID")
    String userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MESSAGE_TIME")
    Date timestamp;

    @Column(name="IS_SENT")
    boolean isSent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public int getMessageId() {
        return messageId;
    }

}
