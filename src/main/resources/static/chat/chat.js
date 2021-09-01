'use strict';

//定义document区域
var messageForm = document.querySelector('#messageForm');
var receiverInput = document.querySelector("#receiver");
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var stompClient = null;
var numArea = document.querySelector("#onlineUser")

//解析出用户名
var url = location.href;
var num = url.indexOf("?");
var username = url.substr(num+1);

//头像颜色
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

/**
 * 连接到socket
 */
function connect() {
    if(username) {
        //定义请求服务器端点
        var socket = new SockJS('/ws');
        //创建stomp客户端
        stompClient = Stomp.over(socket);
        //连接服务器端点
        stompClient.connect({}, onConnected);
    }
}

/**
 * 连接服务器
 */
function onConnected() {
    //订阅到服务器
    stompClient.subscribe('/topic/public', onMessageReceived);
    //发出添加用户请求
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
    connectingElement.classList.add('hidden');
}


/**
 * 发送消息
 * @param event
 */
function sendMessage(event) {
    var messageContent = messageInput.value.trim(); //trim去除头尾空格
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            receiver: receiverInput.value
        };

        stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

/**
 * 接受消息
 * 根据消息类型显示
 * @param payload
 */
function onMessageReceived(payload) {

    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' 来到聊天室!';
        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;

        numArea.innerHTML="";
        var onlineUserElement = document.createTextNode("当前在线人数：" + message.num);
        numArea.appendChild(onlineUserElement);


    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' 离开聊天室!';
        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;

        numArea.innerHTML="";
        var onlineUserElement = document.createTextNode("当前在线人数：" + message.num);
        numArea.appendChild(onlineUserElement);

    } else if(message.receiver === username || message.receiver === "" || message.sender === username) {            //私聊的信息只有自己和对方能收到，群发的都可以收到
        messageElement.classList.add('chat-message');
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;

    }
}

/**
 * 获取头像颜色
 * @param messageSender
 * @returns {string}
 */
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}


window.onload = connect();  //页面打开时运行
messageForm.addEventListener('submit', sendMessage, true)       //监听发送按钮
