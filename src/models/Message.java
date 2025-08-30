package models;

import java.util.Date;
import java.util.UUID;

/**
 * Message class demonstrating Encapsulation and Composition
 * Has-a relationship with User (sender)
 */
public class Message {
    private String messageId;
    private String content;
    private String senderId;
    private String senderUsername;
    private Date timestamp;
    private boolean isEdited;
    private Date editedTimestamp;
    private String channelId;
    
    public Message(String content, String senderId, String senderUsername, String channelId) {
        this.messageId = UUID.randomUUID().toString();
        this.content = content;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.channelId = channelId;
        this.timestamp = new Date();
        this.isEdited = false;
        this.editedTimestamp = null;
    }
    
    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.isEdited = true;
        this.editedTimestamp = new Date();
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public String getSenderUsername() {
        return senderUsername;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public boolean isEdited() {
        return isEdited;
    }
    
    public Date getEditedTimestamp() {
        return editedTimestamp;
    }
    
    public String getChannelId() {
        return channelId;
    }
    
    @Override
    public String toString() {
        String editedText = isEdited ? " (edited)" : "";
        return String.format("[%s] %s: %s%s", 
            timestamp.toString(), senderUsername, content, editedText);
    }
    
    public String getFormattedMessage() {
        return toString();
    }
}
