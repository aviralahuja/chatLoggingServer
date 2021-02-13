package com.fci.chat.util;

import com.fci.chat.dto.Reply;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {

    private final TokenUtil tokenUtil;
    private final MessageSource messageSource;

    public ExceptionHandler(TokenUtil tokenUtil, MessageSource messageSource) {
        this.tokenUtil = tokenUtil;
        this.messageSource = messageSource;
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Reply processMethodArgumentValidationError(HttpServletRequest request, MethodArgumentNotValidException ex) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Reply reply;
        if (token == null || token.isEmpty())
            reply = new Reply();
        else
            reply =tokenUtil.getReply(token);
        BindingResult result=ex.getBindingResult();
        List<FieldError> errors=result.getFieldErrors();
        if(errors!=null){
            Locale locale= LocaleContextHolder.getLocale();
            String msg;
            StringBuilder messageBuilder=new StringBuilder();
            for(FieldError fieldError:errors){
                msg=messageSource.getMessage(fieldError.getCode(),null,fieldError.getDefaultMessage(),locale);
                messageBuilder.append(msg).append(" in ").append(fieldError.getField()).append("\n");
            }
            reply.setMessage(messageBuilder.toString(), Const.MessageType.ERROR);
        }
        return reply;
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Reply processConstraintViolationError(HttpServletRequest request, ConstraintViolationException ex) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Reply reply;
        if (token == null || token.isEmpty())
            reply = new Reply();
        else
            reply =tokenUtil.getReply(token);
        List<ConstraintViolation> errors=ex.getConstraintViolations().stream().collect(Collectors.toList());
        if(errors!=null){
            StringBuilder messageBuilder=new StringBuilder();
            for(ConstraintViolation fieldError:errors){
                messageBuilder.append(((PathImpl)fieldError.getPropertyPath()).getLeafNode().getName()).append(" ").append(fieldError.getMessage()).append("\n");
            }
            reply.setMessage(messageBuilder.toString(), Const.MessageType.ERROR);
        }
        return reply;
    }

}
