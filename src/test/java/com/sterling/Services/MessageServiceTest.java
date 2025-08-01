package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sterling.Interfaces.MessageDAOInterface;
import com.sterling.Models.Message;

public class MessageServiceTest {

    private MessageDAOInterface messageDAO;
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        messageDAO = mock(MessageDAOInterface.class);
        messageService = new MessageService(messageDAO);
    }

    @Test
    void testDeleteMessage_Success() {
        Message msg = new Message();
        msg.setMessageId(1);
        msg.setSenderId(2);
        
        when(messageDAO.getMessageById(1)).thenReturn(msg);
        when(messageDAO.deleteMessage(1, 2)).thenReturn(true);

        boolean result = messageService.deleteMessage(1, 2);

        assertTrue(result);
        verify(messageDAO, times(1)).deleteMessage(1, 2);
    }

    @Test
    void testDeleteMessage_Failure() {
        Message msg = new Message();
        msg.setMessageId(1);
        msg.setSenderId(2);
        
        when(messageDAO.getMessageById(1)).thenReturn(msg);

        boolean result = messageService.deleteMessage(1, 4);

        assertFalse(result);
        verify(messageDAO, never()).deleteMessage(anyInt(), anyInt());
    }

    @Test
    void testGetMessageById() {
        Message msg = new Message();
        msg.setMessageId(1);
        msg.setContent("Hello again");
        
        when(messageDAO.getMessageById(1)).thenReturn(msg);

        Message result = messageService.getMessageById(1);

        assertNotNull(result);
        assertEquals("Hello again", result.getContent());
        verify(messageDAO, times(1)).getMessageById(1);
    }

    @Test
    void testGetMessagesBetweenUsers() {
        Message msg1 = new Message();
        msg1.setMessageId(1);
        msg1.setContent("Hey");
        msg1.setSenderId(1);
        msg1.setReceiverId(2);

        Message msg2 = new Message();
        msg2.setMessageId(2);
        msg2.setContent("Hello");
        msg2.setSenderId(2);
        msg2.setReceiverId(1);

        List<Message> mockMessages = List.of(msg1, msg2);

        when(messageDAO.getMessagesBetweenUsers(1, 2)).thenReturn(mockMessages);

        List<Message> result = messageService.getMessagesBetweenUsers(1, 2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSenderId());
        assertEquals(2, result.get(0).getReceiverId());
        assertEquals(2, result.get(1).getSenderId());
        assertEquals(1, result.get(1).getReceiverId());
        assertEquals("Hey", result.get(0).getContent());
        assertEquals("Hello", result.get(1).getContent());
        assertEquals(1, result.get(0).getMessageId());
        assertEquals(2, result.get(1).getMessageId());
        
        verify(messageDAO, times(1)).getMessagesBetweenUsers(1, 2);
    }

    @Test
    void testGetMessagesForUser() {
        Message msg = new Message();
        
        msg.setMessageId(1);
        msg.setContent("Hi there");

        when(messageDAO.getMessagesForUser(1)).thenReturn(List.of(msg));

        List<Message> result = messageService.getMessagesForUser(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hi there", result.get(0).getContent());

        verify(messageDAO, times(1)).getMessagesForUser(1);
    }

    @Test
    void testSendMessage() {
        Message message = new Message();
        
        message.setContent("Hello!");
        
        messageService.sendMessage(message);

        verify(messageDAO, times(1)).sendMessage(message);
    }
}
