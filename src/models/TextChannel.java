package models;

import utils.ConsoleUtil;

/**
 * TextChannel class demonstrating Inheritance and Polymorphism
 * Extends Channel and implements text-specific behavior
 */
public class TextChannel extends Channel {
    private int maxMessageLength;
    
    public TextChannel(String channelName, String serverId) {
        super(channelName, serverId);
        this.maxMessageLength = 2000; // Discord-like message limit
    }
    
    @Override
    public void sendMessage(String content, User sender) {
        // Check if user is muted
        if (isUserMuted(sender.getUserId())) {
            ConsoleUtil.printError("You are muted in this channel and cannot send messages.");
            return;
        }
        
        // Check message length
        if (content.length() > maxMessageLength) {
            ConsoleUtil.printError("Message too long. Maximum length is " + maxMessageLength + " characters.");
            return;
        }
        
        // Create and add message
        Message message = new Message(content, sender.getUserId(), sender.getUsername(), channelId);
        messageHistory.add(message);
        
        ConsoleUtil.printSuccess("Message sent to #" + channelName);
    }
    
    @Override
    public void displayMessages() {
        ConsoleUtil.printHeader("Messages in #" + channelName);
        
        if (messageHistory.isEmpty()) {
            ConsoleUtil.printInfo("No messages in this channel yet.");
            return;
        }
        
        // Display last 20 messages
        int startIndex = Math.max(0, messageHistory.size() - 20);
        for (int i = startIndex; i < messageHistory.size(); i++) {
            System.out.println(messageHistory.get(i).getFormattedMessage());
        }
        
        if (messageHistory.size() > 20) {
            ConsoleUtil.printInfo("Showing last 20 messages. Total: " + messageHistory.size());
        }
    }
    
    @Override
    public String getChannelType() {
        return "TEXT";
    }
    
    public void editMessage(String messageId, String newContent, User editor) {
        Message message = findMessage(messageId);
        if (message == null) {
            ConsoleUtil.printError("Message not found.");
            return;
        }
        
        // Check if user can edit this message
        if (!message.getSenderId().equals(editor.getUserId()) && !editor.hasPermission("delete_messages")) {
            ConsoleUtil.printError("You can only edit your own messages.");
            return;
        }
        
        if (newContent.length() > maxMessageLength) {
            ConsoleUtil.printError("Message too long. Maximum length is " + maxMessageLength + " characters.");
            return;
        }
        
        message.setContent(newContent);
        ConsoleUtil.printSuccess("Message edited successfully.");
    }
    
    public int getMaxMessageLength() {
        return maxMessageLength;
    }
    
    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }
}
