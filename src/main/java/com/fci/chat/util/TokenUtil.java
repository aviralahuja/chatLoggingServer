package com.fci.chat.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fci.chat.dto.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {

    @Autowired
    ObjectMapper mapper;

    String JWT_SECRET = "secret";
    int expiry = 60 * 60 * 1000;


    public String generateToken(String claims) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        String token = JWT.create().withIssuer("FCI").withIssuedAt(new Date()).withExpiresAt(getExpiryDate(expiry)).
                withClaim("claims", claims).sign(algorithm);
        return token;
    }

    private Date getExpiryDate(int minutes) {
        return new Date(Calendar.getInstance().getTimeInMillis() + minutes);
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("FCI").build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        }catch (Exception e){
            //e.printStackTrace();
        }
        return null;
    }

    public Reply getReply(String token) {
        if (token == null)
            return null;
        try {
            Map<String, Object> session =getSession(token);
            return new Reply(session);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> getSession(String token) throws JsonProcessingException {
        DecodedJWT jwt = validateToken(token);
        if (jwt == null)
            return new HashMap<>();
        return mapper.readValue(jwt.getClaim("claims").asString(), HashMap.class);
    }
}
