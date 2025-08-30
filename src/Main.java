import models.*;
import services.*;
import utils.ConsoleUtil;
import java.util.*;

/**
 * Main class - Entry point for the Discord Clone application
 * Demonstrates all OOP principles in action
 */
public class Main {
    private static AuthService authService;
    private static ServerService serverService;
    private static ChannelService channelService;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Initialize services
        authService = new AuthService();
        serverService = new ServerService(authService);
        channelService = new ChannelService(authService, serverService);
        
        // Display welcome banner
        ConsoleUtil.clearScreen();
        ConsoleUtil.printBanner();
        ConsoleUtil.printInfo("Welcome to Discord Clone - Java Console Edition");
               
        ConsoleUtil.waitForEnter();
        
        // Main application loop
        while (true) {
            if (!authService.isLoggedIn()) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void showAuthMenu() {
        ConsoleUtil.clearScreen();
        String[] options = {
            "Login",
            "Register",
            "Demo Login (admin/admin123)",
            "View Demo Info"
        };
        
        ConsoleUtil.printMenu("Authentication Menu", options);
        
        int choice = ConsoleUtil.getIntInput("Enter your choice", 0, options.length);
        
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegister();
                break;
            case 3:
                handleDemoLogin();
                break;
            case 4:
                showDemoInfo();
                break;
            case 0:
                exitApplication();
                break;
            default:
                ConsoleUtil.printError("Invalid choice.");
        }
    }
    
    private static void showMainMenu() {
        ConsoleUtil.clearScreen();
        User currentUser = authService.getCurrentUser();
        ConsoleUtil.printHeader("Welcome, " + currentUser.getUsername() + "!");
        
        String[] options = {
            "Server Management",
            "Channel & Messaging",
            "Friends & Direct Messages",
            "User Profile",
            "Logout"
        };
        
        ConsoleUtil.printMenu("Main Menu", options);
        
        int choice = ConsoleUtil.getIntInput("Enter your choice", 0, options.length);
        
        switch (choice) {
            case 1:
                showServerMenu();
                break;
            case 2:
                showChannelMenu();
                break;
            case 3:
                showFriendsMenu();
                break;
            case 4:
                showUserProfile();
                break;
            case 5:
                authService.logoutUser();
                break;
            case 0:
                exitApplication();
                break;
            default:
                ConsoleUtil.printError("Invalid choice.");
        }
    }
    
    private static void handleLogin() {
        ConsoleUtil.printHeader("User Login");
        String username = ConsoleUtil.getInput("Username");
        String password = ConsoleUtil.getPassword("Password");
        
        if (authService.loginUser(username, password)) {
            ConsoleUtil.waitForEnter();
        } else {
            ConsoleUtil.waitForEnter();
        }
    }
    
    private static void handleRegister() {
        ConsoleUtil.printHeader("User Registration");
        String username = ConsoleUtil.getInput("Username");
        String password = ConsoleUtil.getPassword("Password");
        String confirmPassword = ConsoleUtil.getPassword("Confirm Password");
        
        if (!password.equals(confirmPassword)) {
            ConsoleUtil.printError("Passwords do not match.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        String[] userTypes = {"Member", "Moderator", "Admin"};
        ConsoleUtil.printInfo("Select user type:");
        for (int i = 0; i < userTypes.length; i++) {
            System.out.println((i + 1) + ". " + userTypes[i]);
        }
        
        int typeChoice = ConsoleUtil.getIntInput("Enter choice", 1, userTypes.length);
        String userType = userTypes[typeChoice - 1];
        
        if (authService.registerUser(username, password, userType)) {
            ConsoleUtil.printInfo("You can now login with your credentials.");
        }
        ConsoleUtil.waitForEnter();
    }
    
    private static void handleDemoLogin() {
        if (authService.loginUser("admin", "admin123")) {
            ConsoleUtil.printInfo("Logged in as demo admin user.");
            ConsoleUtil.waitForEnter();
        }
    }
    
    private static void showDemoInfo() {
        ConsoleUtil.printHeader("Demo Account Information");
        ConsoleUtil.printInfo("Demo accounts available:");
        ConsoleUtil.printInfo("• admin / admin123 (Admin User)");
        ConsoleUtil.printInfo("• moderator / mod123 (Moderator User)");
        ConsoleUtil.printInfo("• user / user123 (Regular User)");
        ConsoleUtil.printInfo("");
        ConsoleUtil.printInfo("Demo server 'Demo Server' is available with invite code.");
        ConsoleUtil.waitForEnter();
    }
    
    private static void showServerMenu() {
        while (true) {
            ConsoleUtil.clearScreen();
            String[] options = {
                "View My Servers",
                "Create New Server",
                "Join Server (Invite Code)",
                "Server Management",
                "Leave Server"
            };
            
            ConsoleUtil.printMenu("Server Management", options);
            
            int choice = ConsoleUtil.getIntInput("Enter your choice", 0, options.length);
            
            switch (choice) {
                case 1:
                    viewMyServers();
                    break;
                case 2:
                    createNewServer();
                    break;
                case 3:
                    joinServerByInvite();
                    break;
                case 4:
                    manageServer();
                    break;
                case 5:
                    leaveServer();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtil.printError("Invalid choice.");
            }
        }
    }
    
    private static void showChannelMenu() {
        // First, let user select a server
        List<Server> userServers = serverService.getCurrentUserServers();
        if (userServers.isEmpty()) {
            ConsoleUtil.printError("You are not a member of any servers. Join a server first.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        Server selectedServer = selectServer(userServers);
        if (selectedServer == null) return;
        
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("Channel Management - " + selectedServer.getServerName());
            
            String[] options = {
                "View Channels",
                "Create Text Channel",
                "Create Voice Channel",
                "Send Message",
                "View Messages",
                "Edit Message",
                "Delete Message",
                "Search Messages",
                "Join Voice Channel",
                "Leave Voice Channel",
                "Voice Actions",
                "Manage Channel Users"
            };
            
            ConsoleUtil.printMenu("Channel & Messaging", options);
            
            int choice = ConsoleUtil.getIntInput("Enter your choice", 0, options.length);
            
            switch (choice) {
                case 1:
                    viewChannels(selectedServer);
                    break;
                case 2:
                    createTextChannel(selectedServer);
                    break;
                case 3:
                    createVoiceChannel(selectedServer);
                    break;
                case 4:
                    sendMessage(selectedServer);
                    break;
                case 5:
                    viewMessages(selectedServer);
                    break;
                case 6:
                    editMessage(selectedServer);
                    break;
                case 7:
                    deleteMessage(selectedServer);
                    break;
                case 8:
                    searchMessages(selectedServer);
                    break;
                case 9:
                    joinVoiceChannel(selectedServer);
                    break;
                case 10:
                    leaveVoiceChannel(selectedServer);
                    break;
                case 11:
                    performVoiceActions(selectedServer);
                    break;
                case 12:
                    manageChannelUsers(selectedServer);
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtil.printError("Invalid choice.");
            }
        }
    }
    
    private static void showFriendsMenu() {
        while (true) {
            ConsoleUtil.clearScreen();
            String[] options = {
                "View Friends List",
                "Add Friend",
                "Remove Friend",
                "Send Direct Message",
                "View Direct Messages"
            };
            
            ConsoleUtil.printMenu("Friends & Direct Messages", options);
            
            int choice = ConsoleUtil.getIntInput("Enter your choice", 0, options.length);
            
            switch (choice) {
                case 1:
                    viewFriendsList();
                    break;
                case 2:
                    addFriend();
                    break;
                case 3:
                    removeFriend();
                    break;
                case 4:
                    sendDirectMessage();
                    break;
                case 5:
                    viewDirectMessages();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUtil.printError("Invalid choice.");
            }
        }
    }
    
    private static void showUserProfile() {
        authService.displayUserStats();
        ConsoleUtil.waitForEnter();
    }
    
    // Server Management Methods
    private static void viewMyServers() {
        List<Server> userServers = serverService.getCurrentUserServers();
        
        if (userServers.isEmpty()) {
            ConsoleUtil.printInfo("You are not a member of any servers.");
        } else {
            ConsoleUtil.printHeader("My Servers");
            for (int i = 0; i < userServers.size(); i++) {
                Server server = userServers.get(i);
                System.out.println((i + 1) + ". " + server.toString());
            }
        }
        ConsoleUtil.waitForEnter();
    }
    
    private static void createNewServer() {
        ConsoleUtil.printHeader("Create New Server");
        String serverName = ConsoleUtil.getInput("Server Name");
        String description = ConsoleUtil.getInput("Description (optional)");
        
        if (serverService.createServer(serverName, description)) {
            ConsoleUtil.printInfo("Server created successfully!");
        }
        ConsoleUtil.waitForEnter();
    }
    
    private static void joinServerByInvite() {
        ConsoleUtil.printHeader("Join Server");
        String inviteCode = ConsoleUtil.getInput("Invite Code");
        
        if (serverService.joinServerByInvite(inviteCode)) {
            ConsoleUtil.printInfo("Successfully joined server!");
        }
        ConsoleUtil.waitForEnter();
    }
    
    private static void manageServer() {
        List<Server> userServers = serverService.getCurrentUserServers();
        if (userServers.isEmpty()) {
            ConsoleUtil.printError("You are not a member of any servers.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        Server selectedServer = selectServer(userServers);
        if (selectedServer == null) return;
        
        serverService.displayServerInfo(selectedServer.getServerId());
        
        ConsoleUtil.printInfo("\nServer Management Options:");
        String[] options = {
            "Kick User",
            "Ban User",
            "Set User Role",
            "Regenerate Invite Code",
            "Delete Server"
        };
        
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        
        int choice = ConsoleUtil.getIntInput("Enter choice (0 to cancel)", 0, options.length);
        
        switch (choice) {
            case 1:
                String kickUser = ConsoleUtil.getInput("Username to kick");
                serverService.kickUser(selectedServer.getServerId(), kickUser);
                break;
            case 2:
                String banUser = ConsoleUtil.getInput("Username to ban");
                serverService.banUser(selectedServer.getServerId(), banUser);
                break;
            case 3:
                String roleUser = ConsoleUtil.getInput("Username");
                String role = ConsoleUtil.getInput("New role (ADMIN/MODERATOR/MEMBER)");
                serverService.setUserRole(selectedServer.getServerId(), roleUser, role);
                break;
            case 4:
                selectedServer.regenerateInviteCode();
                ConsoleUtil.printSuccess("New invite code: " + selectedServer.getInviteCode());
                break;
            case 5:
                if (ConsoleUtil.getConfirmation("Are you sure you want to delete this server?")) {
                    serverService.deleteServer(selectedServer.getServerId());
                }
                break;
        }
        
        ConsoleUtil.waitForEnter();
    }
    
    private static void leaveServer() {
        List<Server> userServers = serverService.getCurrentUserServers();
        if (userServers.isEmpty()) {
            ConsoleUtil.printError("You are not a member of any servers.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        Server selectedServer = selectServer(userServers);
        if (selectedServer == null) return;
        
        if (ConsoleUtil.getConfirmation("Are you sure you want to leave '" + selectedServer.getServerName() + "'?")) {
            serverService.leaveServer(selectedServer.getServerId());
        }
        ConsoleUtil.waitForEnter();
    }
    
    // Channel Management Methods
    private static void viewChannels(Server server) {
        ConsoleUtil.printHeader("Channels in " + server.getServerName());
        
        List<TextChannel> textChannels = server.getTextChannels();
        List<VoiceChannel> voiceChannels = server.getVoiceChannels();
        
        if (!textChannels.isEmpty()) {
            ConsoleUtil.printInfo("Text Channels:");
            for (int i = 0; i < textChannels.size(); i++) {
                System.out.println("  " + (i + 1) + ". #" + textChannels.get(i).getChannelName());
            }
        }
        
        if (!voiceChannels.isEmpty()) {
            ConsoleUtil.printInfo("Voice Channels:");
            for (int i = 0; i < voiceChannels.size(); i++) {
                VoiceChannel vc = voiceChannels.get(i);
                System.out.println("  " + (i + 1) + ". 🔊" + vc.getChannelName() + 
                    " (" + vc.getConnectedUserCount() + "/" + vc.getMaxUsers() + ")");
            }
        }
        
        ConsoleUtil.waitForEnter();
    }
    
    private static void createTextChannel(Server server) {
        String channelName = ConsoleUtil.getInput("Text Channel Name");
        channelService.createTextChannel(server.getServerId(), channelName);
        ConsoleUtil.waitForEnter();
    }
    
    private static void createVoiceChannel(Server server) {
        String channelName = ConsoleUtil.getInput("Voice Channel Name");
        channelService.createVoiceChannel(server.getServerId(), channelName);
        ConsoleUtil.waitForEnter();
    }
    
    private static void sendMessage(Server server) {
        Channel selectedChannel = selectChannel(server);
        if (selectedChannel == null) return;
        
        String message = ConsoleUtil.getInput("Message");
        channelService.sendMessageToChannel(server.getServerId(), selectedChannel.getChannelId(), message);
        ConsoleUtil.waitForEnter();
    }
    
    private static void viewMessages(Server server) {
        Channel selectedChannel = selectChannel(server);
        if (selectedChannel == null) return;
        
        channelService.displayChannelMessages(server.getServerId(), selectedChannel.getChannelId());
        ConsoleUtil.waitForEnter();
    }
    
    private static void editMessage(Server server) {
        Channel selectedChannel = selectChannel(server);
        if (selectedChannel == null) return;
        
        // First show messages to see IDs
        channelService.displayChannelMessages(server.getServerId(), selectedChannel.getChannelId());
        
        String messageId = ConsoleUtil.getInput("Message ID to edit");
        String newContent = ConsoleUtil.getInput("New message content");
        
        channelService.editMessage(server.getServerId(), selectedChannel.getChannelId(), messageId, newContent);
        ConsoleUtil.waitForEnter();
    }
    
    private static void deleteMessage(Server server) {
        Channel selectedChannel = selectChannel(server);
        if (selectedChannel == null) return;
        
        // First show messages to see IDs
        channelService.displayChannelMessages(server.getServerId(), selectedChannel.getChannelId());
        
        String messageId = ConsoleUtil.getInput("Message ID to delete");
        
        channelService.deleteMessage(server.getServerId(), selectedChannel.getChannelId(), messageId);
        ConsoleUtil.waitForEnter();
    }
    
    private static void searchMessages(Server server) {
        Channel selectedChannel = selectChannel(server);
        if (selectedChannel == null) return;
        
        String keyword = ConsoleUtil.getInput("Search keyword");
        channelService.searchMessages(server.getServerId(), selectedChannel.getChannelId(), keyword);
        ConsoleUtil.waitForEnter();
    }
    
    private static void joinVoiceChannel(Server server) {
        List<VoiceChannel> voiceChannels = server.getVoiceChannels();
        if (voiceChannels.isEmpty()) {
            ConsoleUtil.printError("No voice channels available.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        VoiceChannel selectedChannel = (VoiceChannel) selectVoiceChannel(server);
        if (selectedChannel == null) return;
        
        channelService.joinVoiceChannel(server.getServerId(), selectedChannel.getChannelId());
        ConsoleUtil.waitForEnter();
    }
    
    private static void leaveVoiceChannel(Server server) {
        List<VoiceChannel> voiceChannels = server.getVoiceChannels();
        if (voiceChannels.isEmpty()) {
            ConsoleUtil.printError("No voice channels available.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        VoiceChannel selectedChannel = (VoiceChannel) selectVoiceChannel(server);
        if (selectedChannel == null) return;
        
        channelService.leaveVoiceChannel(server.getServerId(), selectedChannel.getChannelId());
        ConsoleUtil.waitForEnter();
    }
    
    private static void performVoiceActions(Server server) {
        List<VoiceChannel> voiceChannels = server.getVoiceChannels();
        if (voiceChannels.isEmpty()) {
            ConsoleUtil.printError("No voice channels available.");
            ConsoleUtil.waitForEnter();
            return;
        }
        
        VoiceChannel selectedChannel = (VoiceChannel) selectVoiceChannel(server);
        if (selectedChannel == null) return;
        
        String[] actions = {"speak", "mute", "unmute", "deafen", "undeafen"};
        ConsoleUtil.printInfo("Voice Actions:");
        for (int i = 0; i < actions.length; i++) {
            System.out.println((i + 1) + ". " + actions[i]);
        }
        
        int choice = ConsoleUtil.getIntInput("Select action", 1, actions.length);
        channelService.simulateVoiceAction(server.getServerId(), selectedChannel.getChannelId(), actions[choice - 1]);
        ConsoleUtil.waitForEnter();
    }
    
    private static void manageChannelUsers(Server server) {
        Channel selectedChannel = selectChannel(server);
        if (selectedChannel == null) return;
        
        String[] options = {"Mute User", "Unmute User"};
        ConsoleUtil.printInfo("Channel User Management:");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        
        int choice = ConsoleUtil.getIntInput("Select action", 1, options.length);
        String username = ConsoleUtil.getInput("Username");
        
        switch (choice) {
            case 1:
                channelService.muteUser(server.getServerId(), selectedChannel.getChannelId(), username);
                break;
            case 2:
                channelService.unmuteUser(server.getServerId(), selectedChannel.getChannelId(), username);
                break;
        }
        
        ConsoleUtil.waitForEnter();
    }
    
    // Friends Methods
    private static void viewFriendsList() {
        List<User> friends = authService.getCurrentUserFriends();
        
        if (friends.isEmpty()) {
            ConsoleUtil.printInfo("You have no friends yet. Add some friends to start chatting!");
        } else {
            ConsoleUtil.printHeader("Friends List");
            for (int i = 0; i < friends.size(); i++) {
                User friend = friends.get(i);
                String status = friend.isOnline() ? "🟢 Online" : "🔴 Offline";
                System.out.println((i + 1) + ". " + friend.getUsername() + " " + status);
            }
        }
        ConsoleUtil.waitForEnter();
    }
    
    private static void addFriend() {
        String friendUsername = ConsoleUtil.getInput("Friend's Username");
        authService.addFriend(friendUsername);
        ConsoleUtil.waitForEnter();
    }
    
    private static void removeFriend() {
        String friendUsername = ConsoleUtil.getInput("Friend's Username to Remove");
        authService.removeFriend(friendUsername);
        ConsoleUtil.waitForEnter();
    }
    
    private static void sendDirectMessage() {
        String recipientUsername = ConsoleUtil.getInput("Recipient Username");
        String message = ConsoleUtil.getInput("Message");
        channelService.sendDirectMessage(recipientUsername, message);
        ConsoleUtil.waitForEnter();
    }
    
    private static void viewDirectMessages() {
        String otherUsername = ConsoleUtil.getInput("View conversation with");
        channelService.displayDirectMessages(otherUsername);
        ConsoleUtil.waitForEnter();
    }
    
    // Helper Methods
    private static Server selectServer(List<Server> servers) {
        if (servers.isEmpty()) {
            ConsoleUtil.printError("No servers available.");
            return null;
        }
        
        ConsoleUtil.printInfo("Select a server:");
        for (int i = 0; i < servers.size(); i++) {
            System.out.println((i + 1) + ". " + servers.get(i).getServerName());
        }
        
        int choice = ConsoleUtil.getIntInput("Enter choice (0 to cancel)", 0, servers.size());
        
        if (choice == 0) {
            return null;
        }
        
        return servers.get(choice - 1);
    }
    
    private static Channel selectChannel(Server server) {
        List<Channel> channels = server.getChannels();
        if (channels.isEmpty()) {
            ConsoleUtil.printError("No channels available.");
            return null;
        }
        
        ConsoleUtil.printInfo("Select a channel:");
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            String prefix = channel instanceof TextChannel ? "#" : "🔊";
            System.out.println((i + 1) + ". " + prefix + channel.getChannelName());
        }
        
        int choice = ConsoleUtil.getIntInput("Enter choice (0 to cancel)", 0, channels.size());
        
        if (choice == 0) {
            return null;
        }
        
        return channels.get(choice - 1);
    }
    
    private static Channel selectVoiceChannel(Server server) {
        List<VoiceChannel> voiceChannels = server.getVoiceChannels();
        if (voiceChannels.isEmpty()) {
            ConsoleUtil.printError("No voice channels available.");
            return null;
        }
        
        ConsoleUtil.printInfo("Select a voice channel:");
        for (int i = 0; i < voiceChannels.size(); i++) {
            VoiceChannel channel = voiceChannels.get(i);
            System.out.println((i + 1) + ". 🔊" + channel.getChannelName() + 
                " (" + channel.getConnectedUserCount() + "/" + channel.getMaxUsers() + ")");
        }
        
        int choice = ConsoleUtil.getIntInput("Enter choice (0 to cancel)", 0, voiceChannels.size());
        
        if (choice == 0) {
            return null;
        }
        
        return voiceChannels.get(choice - 1);
    }
    
    private static void exitApplication() {
        ConsoleUtil.printInfo("Thank you for using Discord Clone!");
        ConsoleUtil.printInfo("This application demonstrated:");
        ConsoleUtil.printInfo("✓ Encapsulation - Private fields with controlled access");
        ConsoleUtil.printInfo("✓ Inheritance - User hierarchy and Channel hierarchy");
        ConsoleUtil.printInfo("✓ Polymorphism - Method overriding and dynamic behavior");
        ConsoleUtil.printInfo("✓ Abstraction - Abstract classes and interfaces");
        ConsoleUtil.printInfo("✓ Composition - Objects containing other objects");
        System.exit(0);
    }
}
