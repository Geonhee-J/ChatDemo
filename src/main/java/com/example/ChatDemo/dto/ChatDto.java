package com.example.ChatDemo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatDto {
	private Long id;
	private String roomId;
	private String sender;
	private String content;
	private LocalDateTime timestamp;
}
