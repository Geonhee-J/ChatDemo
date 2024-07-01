package com.example.ChatDemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.ChatDemo.dto.ChatDto;

@Mapper
public interface ChatMapper {
	
	void save(ChatDto message);

	List<ChatDto> findByRoomId(String roomId);
}
