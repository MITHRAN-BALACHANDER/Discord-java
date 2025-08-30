package models;

/**
 * ModeratorUser class demonstrating Inheritance
 * Inherits from User and adds moderator-specific permissions
 */
public class ModeratorUser extends User {
    
    public ModeratorUser(String username, String hashedPassword) {
        super(username, hashedPassword);
    }
    
    @Override
    public boolean hasPermission(String permission) {
        // Moderators have limited permissions
        switch (permission.toLowerCase()) {
            case "mute_users":
            case "kick_users":
            case "delete_messages":
            case "manage_channels":
                return true;
            default:
                return false;
        }
    }
    
    public boolean canMuteUsers() {
        return true;
    }
    
    public boolean canKickUsers() {
        return true;
    }
    
    public boolean canDeleteMessages() {
        return true;
    }
    
    public boolean canManageChannels() {
        return true;
    }
    
    public boolean canBanUsers() {
        return false; // Only admins can ban
    }
    
    @Override
    public String toString() {
        return "ModeratorUser{" +
                "username='" + getUsername() + '\'' +
                ", isOnline=" + isOnline() +
                ", role=MODERATOR" +
                '}';
    }
}
