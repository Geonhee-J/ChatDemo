<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>webSocket Chat</title>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div>
    	<input type="text" id="userName" placeholder="Make a user name">
    	<button onclick="makeUserName()">Submit</button>
    </div>
    <br>
	<div id="chat">
        <input type="text" id="roomIdInput" placeholder="Enter room ID"/>
        <button onclick="joinRoom()">Join Room</button>
        <div id="messageArea"></div>
        <input type="text" id="messageInput" placeholder="Type a message..."/>
        <button onclick="sendMessage()">Send</button>
    </div>
</body>
	<script>
        var stompClient = null;
        var currentRoomId = null;
        var userName = 'User';
        
        // 직접 입력한 닉네임 적용하기
        function makeUserName() {
        	userName = $('#userName').val().trim();
        	// console.log(userName);
        }

        function connect() {
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            // console.log('stompClient -> ', stompClient);
            stompClient.connect({}, onConnected, onError);
        }

        function onConnected() {
            // console.log('Connected');
        }

        function onError(error) {
            console.error('Error:', error);
        }

        function joinRoom() {
            var roomId = $('#roomIdInput').val().trim();
            if (roomId && stompClient) {
                if (currentRoomId) {
                    stompClient.unsubscribe('/topic/' + currentRoomId);
                }
                currentRoomId = roomId;
                stompClient.subscribe('/topic/' + roomId, onMessageReceived);
                stompClient.send("/app/chat.addUser", {}, JSON.stringify({sender: userName, type: 'JOIN', roomId: roomId}));
                loadChatHistory(roomId);
            }
        }

        function loadChatHistory(roomId) {
            $.get("/chat/history/" + roomId, function(data) {
            	console.log("data:", data);
                $('#messageArea').empty();
                data.forEach(function(message) {
                    var messageElement = $('<div/>').text(message.sender + ": " + message.content);
                    $('#messageArea').append(messageElement);
                });
            });
        }

        function sendMessage() {
            var messageContent = $('#messageInput').val().trim();
            if (messageContent && stompClient) {
            	console.log('mc:', messageContent);
                var chatMessage = {
                    sender: userName,
                    content: messageContent,
                    type: 'CHAT',
                    roomId: currentRoomId
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                $('#messageInput').val('');
            }
        }

        function onMessageReceived(payload) {
            var message = JSON.parse(payload.body);
            var messageElement = $('<div/>').text(message.sender + ": " + message.content);
            $('#messageArea').append(messageElement);
        }

        $(document).ready(function() {
            connect();
        });
    </script>
</html>