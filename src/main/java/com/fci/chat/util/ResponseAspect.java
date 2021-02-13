package com.fci.chat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fci.chat.dto.Reply;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class ResponseAspect {
    private final TokenUtil tokenUtil;
    private final ObjectMapper mapper;

    public ResponseAspect(TokenUtil tokenUtil, ObjectMapper mapper) {
        this.tokenUtil = tokenUtil;
        this.mapper = mapper;
    }

    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)", returning = "response")
    public void afterPostReturningForLogin(Object response) {
        Reply reply=null;
        if(response instanceof Reply) {
            reply = (Reply) response;
        }
        else if(response instanceof ResponseEntity){
           reply=(Reply)((ResponseEntity)response).getBody();
        }
        if(reply!=null)
            reply.setToken(generateToken(reply.getSession()));
    }

    private String generateToken(Map<String, Object> session) {
        try {
            return tokenUtil.generateToken(mapper.writeValueAsString(session));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
