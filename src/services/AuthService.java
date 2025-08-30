package services;

import models.*;
import utils.HashUtil;
import utils.ConsoleUtil;
import java.util.*;

/**
 * AuthService class for handling user authentication
 * Manages user registration, login, and session management
 */
public class AuthService {
    private Map<String, User> users; // username -> User
    private Map<String, User> userSessions; // userId -> User (logged in users)
    private User currentUser;
    
    public AuthService() {
        this.users = new HashMap<>();
        this.userSessions = new HashMap<>();
        this.currentUser = null;
        
        // Create some demo users for testing
        createDemoUsers();
    }
    
    private void createDemoUsers() {
        // Create demo admin user
        AdminUser admin = new AdminUser("admin", HashUtil.hashPassword("admin123"));
        users.put(admin.getUsername(), admin);
        
        // Create demo moderator user
        ModeratorUser moderator = new ModeratorUser("moderator", HashUtil.hashPassword("mod123"));
        users.put(moderator.getUsername(), moderator);
        
        // Create demo regular user
        User user = new User("user", HashUtil.hashPassword("user123"));
        users.put(user.getUsername(), user);
    }
    
    /**
     * Register a new user
     */
    public boolean registerUser(String username, String password, String userType) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            ConsoleUtil.printError("Username cannot be empty.");
            return false;
        }
        
        if (password == null || password.length() < 3) {
            ConsoleUtil.printError("Password must be at least 3 characters long.");
            return false;
        }
        
        // Check if username already exists
        if (users.containsKey(username.toLowerCase())) {
            ConsoleUtil.printError("Username already exists.");
            return false;
        }
        
        // Hash password
        String hashedPassword = HashUtil.hashPassword(password);
        
        // Create user based on type
        User newUser;
        switch (userType.toLowerCase()) {
            case "admin":
                newUser = new AdminUser(username, hashedPassword);
                break;
            case "moderator":
                newUser = new ModeratorUser(username, hashedPassword);
                break;
            default:
                newUser = new User(username, hashedPassword);
                break;
        }
        
        // Store user
        users.put(username.toLowerCase(), newUser);
        
        ConsoleUtil.printSuccess("User '" + username + "' registered successfully!");
        return true;
    }
    
    /**
     * Login user
     */
    public boolean loginUser(String username, String password) {
        if (currentUser != null) {
            ConsoleUtil.printWarning("Already logged in as " + currentUser.getUsername());
            return false;
        }
        
        // Find user
        User user = users.get(username.toLowerCase());
        if (user == null) {
            ConsoleUtil.printError("User not found.");
            return false;
        }
        
        // Verify password
        if (!HashUtil.verifyPassword(password, user.getHashedPassword())) {
            ConsoleUtil.printError("Invalid password.");
            return false;
        }
        
        // Set user as online and current
        user.setOnline(true);
        currentUser = user;
        userSessions.put(user.getUserId(), user);
        
        ConsoleUtil.printSuccess("Welcome back, " + user.getUsername() + "!");
        return true;
    }
    
    /**
     * Logout current user
     */
    public boolean logoutUser() {
        if (currentUser == null) {
            ConsoleUtil.printError("No user is currently logged in.");
            return false;
        }
        
        // Set user as offline
        currentUser.setOnline(false);
        userSessions.remove(currentUser.getUserId());
        
        ConsoleUtil.printSuccess("Goodbye, " + currentUser.getUsername() + "!");
        currentUser = null;
        return true;
    }
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Find user by username
     */
    public User findUserByUsername(String username) {
        return users.get(username.toLowerCase());
    }
    
    /**
     * Find user by ID
     */
    public User findUserById(String userId) {
        return users.values().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all registered users
     */
    public Collection<User> getAllUsers() {
        return users.values();
    }
    
    /**
     * Get all online users
     */
    public Collection<User> getOnlineUsers() {
        return userSessions.values();
    }
    
    /**
     * Add friend to current user
     */
    public boolean addFriend(String friendUsername) {
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to add friends.");
            return false;
        }
        
        User friend = findUserByUsername(friendUsername);
        if (friend == null) {
            ConsoleUtil.printError("User '" + friendUsername + "' not found.");
            return false;
        }
        
        if (friend.getUserId().equals(currentUser.getUserId())) {
            ConsoleUtil.printError("You cannot add yourself as a friend.");
            return false;
        }
        
        // Add friend to both users
        currentUser.addFriend(friend.getUserId());
        friend.addFriend(currentUser.getUserId());
        
        ConsoleUtil.printSuccess("Added " + friendUsername + " as a friend!");
        return true;
    }
    
    /**
     * Remove friend from current user
     */
    public boolean removeFriend(String friendUsername) {
        if (currentUser == null) {
            ConsoleUtil.printError("You must be logged in to remove friends.");
            return false;
        }
        
        User friend = findUserByUsername(friendUsername);
        if (friend == null) {
            ConsoleUtil.printError("User '" + friendUsername + "' not found.");
            return false;
        }
        
        // Remove friend from both users
        currentUser.removeFriend(friend.getUserId());
        friend.removeFriend(currentUser.getUserId());
        
        ConsoleUtil.printSuccess("Removed " + friendUsername + " from friends.");
        return true;
    }
    
    /**
     * Get friends of current user
     */
    public List<User> getCurrentUserFriends() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        
        List<User> friends = new ArrayList<>();
        for (String friendId : currentUser.getFriendList()) {
            User friend = findUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }
    
    /**
     * Display user statistics
     */
    public void displayUserStats() {
        if (currentUser == null) {
            ConsoleUtil.printError("No user logged in.");
            return;
        }
        
        ConsoleUtil.printHeader("User Statistics");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("User ID: " + currentUser.getUserId());
        System.out.println("User Type: " + currentUser.getClass().getSimpleName());
        System.out.println("Friends: " + currentUser.getFriendList().size());
        System.out.println("Servers: " + currentUser.getJoinedServers().size());
        System.out.println("Online: " + (currentUser.isOnline() ? "Yes" : "No"));
        
        if (!currentUser.getFriendList().isEmpty()) {
            System.out.println("\nFriends List:");
            List<User> friends = getCurrentUserFriends();
            for (User friend : friends) {
                String status = friend.isOnline() ? "🟢 Online" : "🔴 Offline";
                System.out.println("  - " + friend.getUsername() + " " + status);
            }
        }
    }
}
