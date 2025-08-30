package models;

import utils.ConsoleUtil;
import java.util.*;

/**
 * VoiceChannel class demonstrating Inheritance and Polymorphism
 * Extends Channel and implements voice-specific behavior (simulated in console)
 */
public class VoiceChannel extends Channel {
    private Set<String> connectedUsers;
    private int maxUsers;
    private boolean isLocked;
    
    public VoiceChannel(String channelName, String serverId) {
        super(channelName, serverId);
        this.connectedUsers = new HashSet<>();
        this.maxUsers = 99; // Discord-like default
        this.isLocked = false;
    }
    
    @Override
    public void sendMessage(String content, User sender) {
        // Voice channels can have text chat too
        if (isUserMuted(sender.getUserId())) {
            ConsoleUtil.printError("You are muted in this channel and cannot send messages.");
            return;
        }
        
        if (!connectedUsers.contains(sender.getUserId())) {
            ConsoleUtil.printError("You must be connected to the voice channel to chat.");
            return;
        }
        
        Message message = new Message("[VOICE] " + content, sender.getUserId(), sender.getUsername(), channelId);
        messageHistory.add(message);
        
        ConsoleUtil.printSuccess("Voice message sent to 🔊" + channelName);
    }
    
    @Override
    public void displayMessages() {
        ConsoleUtil.printHeader("Voice Chat in 🔊" + channelName);
        
        // Show connected users
        ConsoleUtil.printInfo("Connected users (" + connectedUsers.size() + "/" + maxUsers + "):");
        if (connectedUsers.isEmpty()) {
            ConsoleUtil.printInfo("  No users connected");
        }
        
        // Show voice chat messages
        if (!messageHistory.isEmpty()) {
            ConsoleUtil.printInfo("\nVoice Chat Messages:");
            int startIndex = Math.max(0, messageHistory.size() - 10);
            for (int i = startIndex; i < messageHistory.size(); i++) {
                System.out.println(messageHistory.get(i).getFormattedMessage());
            }
        }
    }
    
    @Override
    public String getChannelType() {
        return "VOICE";
    }
    
    public boolean connectUser(String userId, String username) {
        if (isLocked) {
            ConsoleUtil.printError("Voice channel is locked.");
            return false;
        }
        
        if (connectedUsers.size() >= maxUsers) {
            ConsoleUtil.printError("Voice channel is full.");
            return false;
        }
        
        if (connectedUsers.add(userId)) {
            // Simulate voice connection
            Message connectionMessage = new Message(username + " joined the voice channel", 
                "SYSTEM", "System", channelId);
            messageHistory.add(connectionMessage);
            
            ConsoleUtil.printSuccess(username + " connected to 🔊" + channelName);
            return true;
        }
        
        ConsoleUtil.printWarning(username + " is already connected to this channel.");
        return false;
    }
    
    public boolean disconnectUser(String userId, String username) {
        if (connectedUsers.remove(userId)) {
            // Simulate voice disconnection
            Message disconnectionMessage = new Message(username + " left the voice channel", 
                "SYSTEM", "System", channelId);
            messageHistory.add(disconnectionMessage);
            
            ConsoleUtil.printInfo(username + " disconnected from 🔊" + channelName);
            return true;
        }
        
        ConsoleUtil.printWarning(username + " is not connected to this channel.");
        return false;
    }
    
    public void simulateVoiceChat(User user, String action) {
        if (!connectedUsers.contains(user.getUserId())) {
            ConsoleUtil.printError("You must be connected to the voice channel first.");
            return;
        }
        
        String voiceAction = "";
        switch (action.toLowerCase()) {
            case "speak":
                voiceAction = user.getUsername() + " is speaking...";
                break;
            case "mute":
                voiceAction = user.getUsername() + " muted their microphone";
                break;
            case "unmute":
                voiceAction = user.getUsername() + " unmuted their microphone";
                break;
            case "deafen":
                voiceAction = user.getUsername() + " deafened";
                break;
            case "undeafen":
                voiceAction = user.getUsername() + " undeafened";
                break;
            default:
                voiceAction = user.getUsername() + " " + action;
        }
        
        Message voiceMessage = new Message("[VOICE ACTION] " + voiceAction, 
            "SYSTEM", "System", channelId);
        messageHistory.add(voiceMessage);
        
        ConsoleUtil.printInfo("🔊 " + voiceAction);
    }
    
    // Getters and Setters
    public Set<String> getConnectedUsers() {
        return new HashSet<>(connectedUsers);
    }
    
    public int getMaxUsers() {
        return maxUsers;
    }
    
    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }
    
    public boolean isLocked() {
        return isLocked;
    }
    
    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }
    
    public int getConnectedUserCount() {
        return connectedUsers.size();
    }
}
