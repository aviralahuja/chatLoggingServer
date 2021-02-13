package com.fci.chat.controller;

import com.fci.chat.dto.MessageLogReqDTO;
import com.fci.chat.dto.MessageResponseDto;
import com.fci.chat.dto.Reply;
import com.fci.chat.entity.MessageTbl;
import com.fci.chat.service.ChatService;
import com.fci.chat.util.Const;
import com.fci.chat.util.TokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chatlogs/{user}")
public class ChatController {
    private final TokenUtil tokenUtil;
    private final ChatService chatService;

    public ChatController(TokenUtil tokenUtil, ChatService chatService) {
        this.tokenUtil = tokenUtil;
        this.chatService = chatService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,},path = "")
    public Reply addMessageLogJSONEncoded(@RequestBody @NotNull @Valid MessageLogReqDTO messageLogReqDTO, @RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token,@PathVariable("user") String user){
        Reply reply=tokenUtil.getReply(token);
        reply.setData(chatService.addMessageLog(messageLogReqDTO,user));
        reply.setMessage("LOG ADDED SUCCUSSFULLY!!", Const.MessageType.INFO);
        return reply;
    }
    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},path = "")
    public Reply addMessageLogUrlEncoded(@NotNull @Valid MessageLogReqDTO messageLogReqDTO, @RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token, @PathVariable("user") String user){
        Reply reply=tokenUtil.getReply(token);
        reply.setData(chatService.addMessageLog(messageLogReqDTO,user));
        reply.setMessage("LOG ADDED SUCCUSSFULLY!!", Const.MessageType.INFO);
        return reply;
    }

    @GetMapping(path = "")
    public Reply getMessageLogs(@RequestParam("limit")Optional<Integer> optionalLimit,@RequestParam("start")Optional<Integer> optionalStart,@PathVariable("user") String user,@RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token){
        Reply reply=tokenUtil.getReply(token);
        List<MessageResponseDto> messageResponseDtos=chatService.getLogs(user,optionalLimit,optionalStart);
        reply.setData(messageResponseDtos);
        if(messageResponseDtos.isEmpty())
            reply.setMessage("NO RECORDS FOUND!!", Const.MessageType.ERROR);
        return reply;
    }
    @DeleteMapping(path = {"","/{msgId}"})
    public ResponseEntity<Reply> deleteLog(@RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token, @PathVariable("user") String user,@PathVariable("msgId") Optional<Integer> messageId){
        Reply reply=tokenUtil.getReply(token);
        int deletedRecords=chatService.deleteLogs(user,messageId);
        reply.setData(deletedRecords);
        HttpStatus status;
        if(deletedRecords==0){
            status=HttpStatus.NO_CONTENT;
            reply.setMessage("NO RECORD FOUND", Const.MessageType.ERROR);
        }else{
            status=HttpStatus.OK;
            reply.setMessage("RECORDS DELETED", Const.MessageType.INFO);
        }
        ResponseEntity<Reply> responseEntity=new ResponseEntity(reply,status);
        return responseEntity;
    }


}
