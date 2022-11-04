package com.sockets.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sockets.demo.models.ChatMessageModel;
@RestController
public class ChatConroller {
	 @Autowired private SimpMessagingTemplate messagingTemplate;
	 @MessageMapping("/chat")
	    public void processMessage(@Payload ChatMessageModel chatModel) {
//		     save to db and sends as notification -> if required
//			 The convertAndSendToUser will append recipient id to /queue/messages, 
//			 and also it will append the configured user destination prefix /user at the beginning.
//			 The final destination will look like /user/{recipientId}/queue/messages -> subscribed at frontend
//		     retrieve chatId if exists or else create new
	        messagingTemplate.convertAndSendToUser(chatModel.getRecipientId(),"/queue/messages",chatModel);
	    }
	 
}
