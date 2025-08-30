package services;

import models.*;
import utils.ConsoleUtil;
import java.util.*;

/**
 * ServerService class for handling server operations
 * Manages server creation, deletion, member management, and invites
 */
public class ServerService {
    private Map<String, Server> servers; // serverId -> Server
    private Map<String, String> inviteCodes; // inviteCode -> serverId
    private AuthService authService;
    
    public ServerService(AuthService authService) {
        this.servers = new HashMap<>();
        this.inviteCodes = new HashMap<>();
        this.authService = authService;
        
        // Create a demo server for testing
        createDemoServer();
    }
    
    private void createDemoServer() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            // Create with admin user
            User admin = authService.findUserByUsername("admin");
            if (admin != null) {
                Server demoServer = new Server("Demo Server", admin.getUserId(), admin.getUsername());
                demoServer.setDescription("A demo server for testing the Discord clone");
                servers.put(demoServer.getServerId(), demoServer);
                inviteCodes.put(demoServer.getInviteCode(), demoServer.getServerId());
                admin.joinServer(demoServer.getServerId());
            }
        }
    }
    
    /**
     * Create a new server
     */
    public boolean createServer(String serverName, String description) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to create a server.");
            return false;
        }
        
        if (serverName == null || serverName.trim().isEmpty()) {
            ConsoleUtil.printError("Server name cannot be empty.");
            return false;
        }
        
        // Create new server
        Server server = new Server(serverName.trim(), currentUser.getUserId(), currentUser.getUsername());
        if (description != null && !description.trim().isEmpty()) {
            server.setDescription(description.trim());
        }
        
        // Store server and invite code
        servers.put(server.getServerId(), server);
        inviteCodes.put(server.getInviteCode(), server.getServerId());
        
        // Add user to server
        currentUser.joinServer(server.getServerId());
        
        ConsoleUtil.printSuccess("Server '" + serverName + "' created successfully!");
        ConsoleUtil.printInfo("Invite code: " + server.getInviteCode());
        
        return true;
    }
    
    /**
     * Delete a server
     */
    public boolean deleteServer(String serverId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to delete a server.");
            return false;
        }
        
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check if user is the owner
        if (!server.getOwnerId().equals(currentUser.getUserId())) {
            ConsoleUtil.printError("Only the server owner can delete the server.");
            return false;
        }
        
        // Remove server from all members
        for (String memberId : server.getMembers().keySet()) {
            User member = authService.findUserById(memberId);
            if (member != null) {
                member.leaveServer(serverId);
            }
        }
        
        // Remove server and invite code
        inviteCodes.remove(server.getInviteCode());
        servers.remove(serverId);
        
        ConsoleUtil.printSuccess("Server '" + server.getServerName() + "' has been deleted.");
        return true;
    }
    
    /**
     * Join a server using invite code
     */
    public boolean joinServerByInvite(String inviteCode) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to join a server.");
            return false;
        }
        
        String serverId = inviteCodes.get(inviteCode.toUpperCase());
        if (serverId == null) {
            ConsoleUtil.printError("Invalid invite code.");
            return false;
        }
        
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server no longer exists.");
            return false;
        }
        
        // Check if user is banned
        if (server.isUserBanned(currentUser.getUserId())) {
            ConsoleUtil.printError("You are banned from this server.");
            return false;
        }
        
        // Add user to server
        if (server.addMember(currentUser.getUserId(), currentUser.getUsername())) {
            currentUser.joinServer(serverId);
            ConsoleUtil.printSuccess("Successfully joined '" + server.getServerName() + "'!");
            return true;
        } else {
            ConsoleUtil.printWarning("You are already a member of this server.");
            return false;
        }
    }
    
    /**
     * Leave a server
     */
    public boolean leaveServer(String serverId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to leave a server.");
            return false;
        }
        
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check if user is the owner
        if (server.getOwnerId().equals(currentUser.getUserId())) {
            ConsoleUtil.printError("Server owner cannot leave the server. Delete the server instead.");
            return false;
        }
        
        // Remove user from server
        if (server.removeMember(currentUser.getUserId())) {
            currentUser.leaveServer(serverId);
            ConsoleUtil.printSuccess("Left '" + server.getServerName() + "'.");
            return true;
        } else {
            ConsoleUtil.printError("You are not a member of this server.");
            return false;
        }
    }
    
    /**
     * Get servers that current user has joined
     */
    public List<Server> getCurrentUserServers() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return new ArrayList<>();
        }
        
        List<Server> userServers = new ArrayList<>();
        for (String serverId : currentUser.getJoinedServers()) {
            Server server = servers.get(serverId);
            if (server != null) {
                userServers.add(server);
            }
        }
        return userServers;
    }
    
    /**
     * Get server by ID
     */
    public Server getServer(String serverId) {
        return servers.get(serverId);
    }
    
    /**
     * Get all servers
     */
    public Collection<Server> getAllServers() {
        return servers.values();
    }
    
    /**
     * Kick a user from server
     */
    public boolean kickUser(String serverId, String targetUsername) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to kick users.");
            return false;
        }
        
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check permissions
        String userRole = server.getMemberRole(currentUser.getUserId());
        if (!currentUser.hasPermission("kick_users") && !"ADMIN".equals(userRole)) {
            ConsoleUtil.printError("You don't have permission to kick users.");
            return false;
        }
        
        User targetUser = authService.findUserByUsername(targetUsername);
        if (targetUser == null) {
            ConsoleUtil.printError("User '" + targetUsername + "' not found.");
            return false;
        }
        
        if (!server.isMember(targetUser.getUserId())) {
            ConsoleUtil.printError("User is not a member of this server.");
            return false;
        }
        
        if (targetUser.getUserId().equals(server.getOwnerId())) {
            ConsoleUtil.printError("Cannot kick the server owner.");
            return false;
        }
        
        // Remove user from server
        server.removeMember(targetUser.getUserId());
        targetUser.leaveServer(serverId);
        
        ConsoleUtil.printSuccess("Kicked " + targetUsername + " from the server.");
        return true;
    }
    
    /**
     * Ban a user from server
     */
    public boolean banUser(String serverId, String targetUsername) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to ban users.");
            return false;
        }
        
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check permissions
        if (!currentUser.hasPermission("ban_users")) {
            ConsoleUtil.printError("You don't have permission to ban users.");
            return false;
        }
        
        User targetUser = authService.findUserByUsername(targetUsername);
        if (targetUser == null) {
            ConsoleUtil.printError("User '" + targetUsername + "' not found.");
            return false;
        }
        
        if (targetUser.getUserId().equals(server.getOwnerId())) {
            ConsoleUtil.printError("Cannot ban the server owner.");
            return false;
        }
        
        // Ban user
        server.banUser(targetUser.getUserId());
        targetUser.leaveServer(serverId);
        
        ConsoleUtil.printSuccess("Banned " + targetUsername + " from the server.");
        return true;
    }
    
    /**
     * Set user role in server
     */
    public boolean setUserRole(String serverId, String targetUsername, String role) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to manage roles.");
            return false;
        }
        
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return false;
        }
        
        // Check if user is server owner
        if (!server.getOwnerId().equals(currentUser.getUserId())) {
            ConsoleUtil.printError("Only the server owner can change user roles.");
            return false;
        }
        
        User targetUser = authService.findUserByUsername(targetUsername);
        if (targetUser == null) {
            ConsoleUtil.printError("User '" + targetUsername + "' not found.");
            return false;
        }
        
        if (!server.isMember(targetUser.getUserId())) {
            ConsoleUtil.printError("User is not a member of this server.");
            return false;
        }
        
        // Validate role
        String upperRole = role.toUpperCase();
        if (!upperRole.equals("ADMIN") && !upperRole.equals("MODERATOR") && !upperRole.equals("MEMBER")) {
            ConsoleUtil.printError("Invalid role. Valid roles: ADMIN, MODERATOR, MEMBER");
            return false;
        }
        
        server.setMemberRole(targetUser.getUserId(), upperRole);
        ConsoleUtil.printSuccess("Set " + targetUsername + "'s role to " + upperRole + ".");
        return true;
    }
    
    /**
     * Display server information
     */
    public void displayServerInfo(String serverId) {
        Server server = servers.get(serverId);
        if (server == null) {
            ConsoleUtil.printError("Server not found.");
            return;
        }
        
        ConsoleUtil.printHeader("Server: " + server.getServerName());
        System.out.println("Description: " + (server.getDescription().isEmpty() ? "No description" : server.getDescription()));
        System.out.println("Owner: " + server.getOwnerUsername());
        System.out.println("Created: " + server.getCreatedDate());
        System.out.println("Members: " + server.getMemberCount());
        System.out.println("Channels: " + server.getChannelCount());
        System.out.println("Invite Code: " + server.getInviteCode());
        
        // Display members
        System.out.println("\nMembers:");
        Map<String, String> members = server.getMembers();
        Map<String, String> memberUsernames = server.getMemberUsernames();
        
        for (Map.Entry<String, String> entry : members.entrySet()) {
            String userId = entry.getKey();
            String role = entry.getValue();
            String username = memberUsernames.get(userId);
            User user = authService.findUserById(userId);
            String status = (user != null && user.isOnline()) ? "🟢" : "🔴";
            
            System.out.println("  " + status + " " + username + " (" + role + ")");
        }
        
        // Display channels
        System.out.println("\nChannels:");
        for (Channel channel : server.getChannels()) {
            System.out.println("  " + channel.toString());
        }
    }
}
