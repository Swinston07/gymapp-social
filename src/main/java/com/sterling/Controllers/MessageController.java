package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.Message;
import com.sterling.Services.MessageService;

import io.javalin.http.Context;

public class MessageController {
    MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public void sendMessage(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int receiverId = Integer.parseInt(ctx.pathParam("receiverId"));
        Message message = ctx.bodyAsClass(Message.class);

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to send message for another user");
            return;
        }

        message.setSenderId(userId);
        message.setReceiverId(receiverId);
        messageService.sendMessage(message);
        ctx.status(201).result("Message sent");
    }

    public void getMessagesBetweenUsers(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int otherUserId = Integer.parseInt(ctx.pathParam("otherUserId"));

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to view messages for other user");
            return;
        }

        List<Message> messages = messageService.getMessagesBetweenUsers(userId, otherUserId);
        ctx.status(200).json(messages);
    }

    public void getMessagesForUser(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to view messages for another user");
            return;
        }

        List<Message> messages = messageService.getMessagesForUser(userId);

        ctx.status(200).json(messages);
    }

    public void deleteMessage(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int messageId = Integer.parseInt(ctx.pathParam("messageId"));

        boolean success = messageService.deleteMessage(messageId, requesterId);

        if(success) {
            ctx.status(200).result("Message deleted successfully");
        }
        else {
            ctx.result("Unauthorized or message not found").status(403);
        }
    }
}
