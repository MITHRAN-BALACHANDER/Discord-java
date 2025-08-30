package models;

import java.util.*;

/**
 * Base User class demonstrating Encapsulation
 * All fields are private with public getters/setters
 */
public class User {
    private String userId;
    private String username;
    private String hashedPassword;
    private boolean isOnline;
    private Date lastSeen;
    private List<String> friendList;
    private List<String> joinedServers;
    private String currentServer;
    
    public User(String username, String hashedPassword) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isOnline = false;
        this.lastSeen = new Date();
        this.friendList = new ArrayList<>();
        this.joinedServers = new ArrayList<>();
        this.currentServer = null;
    }
    
    // Getters and Setters (Encapsulation)
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getHashedPassword() {
        return hashedPassword;
    }
    
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    public void setOnline(boolean online) {
        this.isOnline = online;
        if (!online) {
            this.lastSeen = new Date();
        }
    }
    
    public Date getLastSeen() {
        return lastSeen;
    }
    
    public List<String> getFriendList() {
        return new ArrayList<>(friendList);
    }
    
    public void addFriend(String friendId) {
        if (!friendList.contains(friendId)) {
            friendList.add(friendId);
        }
    }
    
    public void removeFriend(String friendId) {
        friendList.remove(friendId);
    }
    
    public List<String> getJoinedServers() {
        return new ArrayList<>(joinedServers);
    }
    
    public void joinServer(String serverId) {
        if (!joinedServers.contains(serverId)) {
            joinedServers.add(serverId);
        }
    }
    
    public void leaveServer(String serverId) {
        joinedServers.remove(serverId);
    }
    
    public String getCurrentServer() {
        return currentServer;
    }
    
    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }
    
    // Virtual method that can be overridden by subclasses (Polymorphism)
    public boolean hasPermission(String permission) {
        return false; // Base users have no special permissions
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isOnline=" + isOnline +
                ", friendCount=" + friendList.size() +
                ", serverCount=" + joinedServers.size() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userId, user.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
