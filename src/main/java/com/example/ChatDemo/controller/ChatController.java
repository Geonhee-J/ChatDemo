package com.example.ChatDemo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.ChatDemo.dto.ChatDto;
import com.example.ChatDemo.service.ChatService;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatDto chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        chatService.saveMessage(chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        // chatMessage.setContent("User joined: " + chatMessage.getSender());
        chatService.saveMessage(chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
    }

    @GetMapping("/chat/history/{roomId}")
    public List<ChatDto> getChatHistory(@PathVariable String roomId) {
        return chatService.getMessagesByRoomId(roomId);
    }
	
	@GetMapping("/chat")
	public String chat() {
		return "chat";
	}
}
