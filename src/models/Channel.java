package models;

import java.util.*;

/**
 * Abstract Channel class demonstrating Abstraction
 * Defines common channel behaviors to be implemented by subclasses
 */
public abstract class Channel {
    protected String channelId;
    protected String channelName;
    protected String serverId;
    protected Date createdDate;
    protected List<Message> messageHistory;
    protected Set<String> mutedUsers;
    
    public Channel(String channelName, String serverId) {
        this.channelId = UUID.randomUUID().toString();
        this.channelName = channelName;
        this.serverId = serverId;
        this.createdDate = new Date();
        this.messageHistory = new ArrayList<>();
        this.mutedUsers = new HashSet<>();
    }
    
    // Getters
    public String getChannelId() {
        return channelId;
    }
    
    public String getChannelName() {
        return channelName;
    }
    
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    
    public String getServerId() {
        return serverId;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public List<Message> getMessageHistory() {
        return new ArrayList<>(messageHistory);
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract void sendMessage(String content, User sender);
    public abstract void displayMessages();
    public abstract String getChannelType();
    
    // Common methods
    public void muteUser(String userId) {
        mutedUsers.add(userId);
    }
    
    public void unmuteUser(String userId) {
        mutedUsers.remove(userId);
    }
    
    public boolean isUserMuted(String userId) {
        return mutedUsers.contains(userId);
    }
    
    public void deleteMessage(String messageId) {
        messageHistory.removeIf(message -> message.getMessageId().equals(messageId));
    }
    
    public Message findMessage(String messageId) {
        return messageHistory.stream()
                .filter(message -> message.getMessageId().equals(messageId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Message> searchMessages(String keyword) {
        List<Message> results = new ArrayList<>();
        for (Message message : messageHistory) {
            if (message.getContent().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(message);
            }
        }
        return results;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s (%d messages)", 
            getChannelType(), channelName, messageHistory.size());
    }
}
