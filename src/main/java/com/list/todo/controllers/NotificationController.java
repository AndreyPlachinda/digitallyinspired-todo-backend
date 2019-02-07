package com.list.todo.controllers;

import com.list.todo.entity.Notification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notification")
public class NotificationController {

    @MessageMapping("/sendMessage")
    @SendTo("/queue/notify")
    public Notification sendNotification(String message) {
        return new Notification(message);
    }
}
