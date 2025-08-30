package services;

import models.*;
import utils.ConsoleUtil;
import java.util.*;

/**
 * ChannelService class for handling messaging and channel operations
 * Manages channel creation, messaging, and user interactions
 */
public class ChannelService {
    private AuthService authService;
    private ServerService serverService;
    private Map<String, List<Message>> directMessages; // userId1:userId2 -> messages
    
    public ChannelService(AuthService authService, ServerService serverService) {
        this.authService = authService;
        this.serverService = serverService;
        this.directMessages = new HashMap<>();
    }
    
    /**
     * Create a new text channel
     */
    public boolean createTextChannel(String serverId, String channelName) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to create channels.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check permissions
        String userRole = server.getMemberRole(currentUser.getUserId());
        if (!currentUser.hasPermission("create_channels") && !"ADMIN".equals(userRole)) {
            ConsoleUtil.printError("You don't have permission to create channels.");
            return false;
        }
        
        // Check if channel name already exists
        if (server.findChannelByName(channelName) != null) {
            ConsoleUtil.printError("A channel with that name already exists.");
            return false;
        }
        
        // Create channel
        TextChannel textChannel = new TextChannel(channelName, serverId);
        server.addChannel(textChannel);
        
        ConsoleUtil.printSuccess("Text channel '#" + channelName + "' created successfully!");
        return true;
    }
    
    /**
     * Create a new voice channel
     */
    public boolean createVoiceChannel(String serverId, String channelName) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to create channels.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check permissions
        String userRole = server.getMemberRole(currentUser.getUserId());
        if (!currentUser.hasPermission("create_channels") && !"ADMIN".equals(userRole)) {
            ConsoleUtil.printError("You don't have permission to create channels.");
            return false;
        }
        
        // Check if channel name already exists
        if (server.findChannelByName(channelName) != null) {
            ConsoleUtil.printError("A channel with that name already exists.");
            return false;
        }
        
        // Create channel
        VoiceChannel voiceChannel = new VoiceChannel(channelName, serverId);
        server.addChannel(voiceChannel);
        
        ConsoleUtil.printSuccess("Voice channel '🔊" + channelName + "' created successfully!");
        return true;
    }
    
    /**
     * Delete a channel
     */
    public boolean deleteChannel(String serverId, String channelId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to delete channels.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        // Check permissions
        String userRole = server.getMemberRole(currentUser.getUserId());
        if (!currentUser.hasPermission("delete_channels") && !"ADMIN".equals(userRole)) {
            ConsoleUtil.printError("You don't have permission to delete channels.");
            return false;
        }
        
        // Remove channel
        if (server.removeChannel(channelId)) {
            ConsoleUtil.printSuccess("Channel '" + channel.getChannelName() + "' has been deleted.");
            return true;
        } else {
            ConsoleUtil.printError("Failed to delete channel.");
            return false;
        }
    }
    
    /**
     * Send message to a text channel
     */
    public boolean sendMessageToChannel(String serverId, String channelId, String content) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to send messages.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        if (!server.isMember(currentUser.getUserId())) {
            ConsoleUtil.printError("You are not a member of this server.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        // Send message
        channel.sendMessage(content, currentUser);
        return true;
    }
    
    /**
     * Edit a message
     */
    public boolean editMessage(String serverId, String channelId, String messageId, String newContent) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to edit messages.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        if (channel instanceof TextChannel) {
            ((TextChannel) channel).editMessage(messageId, newContent, currentUser);
            return true;
        } else {
            ConsoleUtil.printError("Cannot edit messages in voice channels.");
            return false;
        }
    }
    
    /**
     * Delete a message
     */
    public boolean deleteMessage(String serverId, String channelId, String messageId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to delete messages.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        Message message = channel.findMessage(messageId);
        if (message == null) {
            ConsoleUtil.printError("Message not found.");
            return false;
        }
        
        // Check if user can delete this message
        if (!message.getSenderId().equals(currentUser.getUserId()) && !currentUser.hasPermission("delete_messages")) {
            ConsoleUtil.printError("You can only delete your own messages.");
            return false;
        }
        
        channel.deleteMessage(messageId);
        ConsoleUtil.printSuccess("Message deleted successfully.");
        return true;
    }
    
    /**
     * Display channel messages
     */
    public void displayChannelMessages(String serverId, String channelId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to view messages.");
            return;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return;
        }
        
        if (!server.isMember(currentUser.getUserId())) {
            ConsoleUtil.printError("You are not a member of this server.");
            return;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return;
        }
        
        channel.displayMessages();
    }
    
    /**
     * Search messages in a channel
     */
    public void searchMessages(String serverId, String channelId, String keyword) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to search messages.");
            return;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return;
        }
        
        List<Message> results = channel.searchMessages(keyword);
        
        ConsoleUtil.printHeader("Search Results for '" + keyword + "' in #" + channel.getChannelName());
        if (results.isEmpty()) {
            ConsoleUtil.printInfo("No messages found containing '" + keyword + "'.");
        } else {
            for (Message message : results) {
                System.out.println(message.getFormattedMessage());
            }
            ConsoleUtil.printInfo("Found " + results.size() + " message(s).");
        }
    }
    
    /**
     * Join voice channel
     */
    public boolean joinVoiceChannel(String serverId, String channelId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to join voice channels.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        if (!(channel instanceof VoiceChannel)) {
            ConsoleUtil.printError("This is not a voice channel.");
            return false;
        }
        
        VoiceChannel voiceChannel = (VoiceChannel) channel;
        return voiceChannel.connectUser(currentUser.getUserId(), currentUser.getUsername());
    }
    
    /**
     * Leave voice channel
     */
    public boolean leaveVoiceChannel(String serverId, String channelId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to leave voice channels.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        if (!(channel instanceof VoiceChannel)) {
            ConsoleUtil.printError("This is not a voice channel.");
            return false;
        }
        
        VoiceChannel voiceChannel = (VoiceChannel) channel;
        return voiceChannel.disconnectUser(currentUser.getUserId(), currentUser.getUsername());
    }
    
    /**
     * Simulate voice action
     */
    public void simulateVoiceAction(String serverId, String channelId, String action) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to perform voice actions.");
            return;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return;
        }
        
        if (!(channel instanceof VoiceChannel)) {
            ConsoleUtil.printError("This is not a voice channel.");
            return;
        }
        
        VoiceChannel voiceChannel = (VoiceChannel) channel;
        voiceChannel.simulateVoiceChat(currentUser, action);
    }
    
    /**
     * Send direct message to another user
     */
    public boolean sendDirectMessage(String recipientUsername, String content) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to send direct messages.");
            return false;
        }
        
        User recipient = authService.findUserByUsername(recipientUsername);
        if (recipient == null) {
            ConsoleUtil.printError("User '" + recipientUsername + "' not found.");
            return false;
        }
        
        if (recipient.getUserId().equals(currentUser.getUserId())) {
            ConsoleUtil.printError("You cannot send a direct message to yourself.");
            return false;
        }
        
        // Create DM key (always use smaller userId first for consistency)
        String dmKey = currentUser.getUserId().compareTo(recipient.getUserId()) < 0 
            ? currentUser.getUserId() + ":" + recipient.getUserId()
            : recipient.getUserId() + ":" + currentUser.getUserId();
        
        // Get or create DM history
        List<Message> dmHistory = directMessages.computeIfAbsent(dmKey, k -> new ArrayList<>());
        
        // Create message
        Message message = new Message(content, currentUser.getUserId(), currentUser.getUsername(), "DM");
        dmHistory.add(message);
        
        ConsoleUtil.printSuccess("Direct message sent to " + recipientUsername + ".");
        return true;
    }
    
    /**
     * Display direct message history with another user
     */
    public void displayDirectMessages(String otherUsername) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to view direct messages.");
            return;
        }
        
        User otherUser = authService.findUserByUsername(otherUsername);
        if (otherUser == null) {
            ConsoleUtil.printError("User '" + otherUsername + "' not found.");
            return;
        }
        
        // Create DM key
        String dmKey = currentUser.getUserId().compareTo(otherUser.getUserId()) < 0 
            ? currentUser.getUserId() + ":" + otherUser.getUserId()
            : otherUser.getUserId() + ":" + currentUser.getUserId();
        
        List<Message> dmHistory = directMessages.get(dmKey);
        
        ConsoleUtil.printHeader("Direct Messages with " + otherUsername);
        if (dmHistory == null || dmHistory.isEmpty()) {
            ConsoleUtil.printInfo("No messages yet. Start the conversation!");
        } else {
            for (Message message : dmHistory) {
                System.out.println(message.getFormattedMessage());
            }
        }
    }
    
    /**
     * Mute user in channel
     */
    public boolean muteUser(String serverId, String channelId, String targetUsername) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to mute users.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check permissions
        String userRole = server.getMemberRole(currentUser.getUserId());
        if (!currentUser.hasPermission("mute_users") && !"ADMIN".equals(userRole) && !"MODERATOR".equals(userRole)) {
            ConsoleUtil.printError("You don't have permission to mute users.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        User targetUser = authService.findUserByUsername(targetUsername);
        if (targetUser == null) {
            ConsoleUtil.printError("User '" + targetUsername + "' not found.");
            return false;
        }
        
        channel.muteUser(targetUser.getUserId());
        ConsoleUtil.printSuccess("Muted " + targetUsername + " in " + channel.getChannelName() + ".");
        return true;
    }
    
    /**
     * Unmute user in channel
     */
    public boolean unmuteUser(String serverId, String channelId, String targetUsername) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to unmute users.");
            return false;
        }
        
        Server server = serverService.getServer(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check permissions
        String userRole = server.getMemberRole(currentUser.getUserId());
        if (!currentUser.hasPermission("mute_users") && !"ADMIN".equals(userRole) && !"MODERATOR".equals(userRole)) {
            ConsoleUtil.printError("You don't have permission to unmute users.");
            return false;
        }
        
        Channel channel = server.findChannel(channelId);
        if (channel == null) {
            ConsoleUtil.printError("Channel not found.");
            return false;
        }
        
        User targetUser = authService.findUserByUsername(targetUsername);
        if (targetUser == null) {
            ConsoleUtil.printError("User '" + targetUsername + "' not found.");
            return false;
        }
        
        channel.unmuteUser(targetUser.getUserId());
        ConsoleUtil.printSuccess("Unmuted " + targetUsername + " in " + channel.getChannelName() + ".");
        return true;
    }
}
