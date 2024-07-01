package com.example.ChatDemo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ChatDemo.dto.ChatDto;
import com.example.ChatDemo.mapper.ChatMapper;

@Service
public class ChatService implements ChatServiceImpl {

	@Autowired
    private ChatMapper chatMapper;

    @Override
    public void saveMessage(ChatDto message) {
        message.setTimestamp(LocalDateTime.now());
        chatMapper.save(message);
    }

    @Override
    public List<ChatDto> getMessagesByRoomId(String roomId) {
        return chatMapper.findByRoomId(roomId);
    }
}
