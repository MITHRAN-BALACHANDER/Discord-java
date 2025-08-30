package models;

import java.util.*;

/**
 * Server class demonstrating Composition
 * Has-a relationship with Channel objects and User objects
 */
public class Server {
    private String serverId;
    private String serverName;
    private String ownerId;
    private String ownerUsername;
    private String inviteCode;
    private Date createdDate;
    private List<Channel> channels;
    private Map<String, String> members; // userId -> role (ADMIN, MODERATOR, MEMBER)
    private Map<String, String> memberUsernames; // userId -> username
    private Set<String> bannedUsers;
    private String description;
    
    public Server(String serverName, String ownerId, String ownerUsername) {
        this.serverId = UUID.randomUUID().toString();
        this.serverName = serverName;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.inviteCode = generateInviteCode();
        this.createdDate = new Date();
        this.channels = new ArrayList<>();
        this.members = new HashMap<>();
        this.memberUsernames = new HashMap<>();
        this.bannedUsers = new HashSet<>();
        this.description = "";
        
        // Add owner as admin
        members.put(ownerId, "ADMIN");
        memberUsernames.put(ownerId, ownerUsername);
        
        // Create default channels
        createDefaultChannels();
    }
    
    private void createDefaultChannels() {
        // Create default text channel
        TextChannel generalText = new TextChannel("general", serverId);
        channels.add(generalText);
        
        // Create default voice channel
        VoiceChannel generalVoice = new VoiceChannel("General Voice", serverId);
        channels.add(generalVoice);
    }
    
    private String generateInviteCode() {
        // Generate a random 8-character invite code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return code.toString();
    }
    
    // Channel management
    public void addChannel(Channel channel) {
        channels.add(channel);
    }
    
    public boolean removeChannel(String channelId) {
        return channels.removeIf(channel -> channel.getChannelId().equals(channelId));
    }
    
    public Channel findChannel(String channelId) {
        return channels.stream()
                .filter(channel -> channel.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }
    
    public Channel findChannelByName(String channelName) {
        return channels.stream()
                .filter(channel -> channel.getChannelName().equalsIgnoreCase(channelName))
                .findFirst()
                .orElse(null);
    }
    
    public List<Channel> getChannels() {
        return new ArrayList<>(channels);
    }
    
    public List<TextChannel> getTextChannels() {
        return channels.stream()
                .filter(channel -> channel instanceof TextChannel)
                .map(channel -> (TextChannel) channel)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public List<VoiceChannel> getVoiceChannels() {
        return channels.stream()
                .filter(channel -> channel instanceof VoiceChannel)
                .map(channel -> (VoiceChannel) channel)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // Member management
    public boolean addMember(String userId, String username) {
        if (bannedUsers.contains(userId)) {
            return false; // User is banned
        }
        
        if (!members.containsKey(userId)) {
            members.put(userId, "MEMBER");
            memberUsernames.put(userId, username);
            return true;
        }
        return false; // Already a member
    }
    
    public boolean removeMember(String userId) {
        if (userId.equals(ownerId)) {
            return false; // Cannot remove owner
        }
        
        members.remove(userId);
        memberUsernames.remove(userId);
        return true;
    }
    
    public boolean isMember(String userId) {
        return members.containsKey(userId);
    }
    
    public String getMemberRole(String userId) {
        return members.get(userId);
    }
    
    public void setMemberRole(String userId, String role) {
        if (members.containsKey(userId) && !userId.equals(ownerId)) {
            members.put(userId, role.toUpperCase());
        }
    }
    
    public Map<String, String> getMembers() {
        return new HashMap<>(members);
    }
    
    public Map<String, String> getMemberUsernames() {
        return new HashMap<>(memberUsernames);
    }
    
    // Ban management
    public void banUser(String userId) {
        bannedUsers.add(userId);
        removeMember(userId);
    }
    
    public void unbanUser(String userId) {
        bannedUsers.remove(userId);
    }
    
    public boolean isUserBanned(String userId) {
        return bannedUsers.contains(userId);
    }
    
    // Getters and Setters
    public String getServerId() {
        return serverId;
    }
    
    public String getServerName() {
        return serverName;
    }
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public String getOwnerUsername() {
        return ownerUsername;
    }
    
    public String getInviteCode() {
        return inviteCode;
    }
    
    public void regenerateInviteCode() {
        this.inviteCode = generateInviteCode();
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getMemberCount() {
        return members.size();
    }
    
    public int getChannelCount() {
        return channels.size();
    }
    
    @Override
    public String toString() {
        return String.format("Server{name='%s', owner='%s', members=%d, channels=%d, code='%s'}", 
            serverName, ownerUsername, getMemberCount(), getChannelCount(), inviteCode);
    }
}
