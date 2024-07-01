package com.example.ChatDemo.service;

import java.util.List;

import com.example.ChatDemo.dto.ChatDto;

public interface ChatServiceImpl {

	void saveMessage(ChatDto message);
	
	List<ChatDto> getMessagesByRoomId(String roomId);
}
