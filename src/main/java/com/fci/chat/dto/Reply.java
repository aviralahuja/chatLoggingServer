package com.fci.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fci.chat.util.Const;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class Reply {
        Object data;
        String token;
        String message;
        short messageType;
        @JsonIgnore
        Map<String,Object> session;
        @JsonIgnore
        @Autowired
        ObjectMapper mapper;

    public Reply(Map<String, Object> session) {
        this.session = session;
    }

    public Reply() {
        session=new HashMap<>();
    }

        public void setMessage(String message, Const.MessageType messageType){
        setMessage(message);
        setMessageType(messageType.getKey());

    }
        public void addToSession(String key,Object value){
        session.put(key,value);
    }
        public Object getFromSession(String key){

        return session.get(key);

    }

        public <T> T getFromSession(String key,Class<T> valueClass){
        try {
            return mapper.readValue(new JSONObject((Map) session.get(key)).toString(),valueClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

        public Object getData() {
        return data;
    }

        public void setData(Object data) {
        this.data = data;
    }

        public String getToken() {
        return token;
    }

        public void setToken(String token) {
        this.token = token;
    }

        public String getMessage() {
        return message;
    }

        public void setMessage(String message) {
        this.message = message;
    }

        public short getMessageType() {
        return messageType;
    }

        public void setMessageType(short messageType) {
        this.messageType = messageType;
    }

        public Map<String, Object> getSession() {
        return session;
    }

        public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}
