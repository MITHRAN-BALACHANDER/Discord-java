package models;

/**
 * AdminUser class demonstrating Inheritance
 * Inherits from User and adds admin-specific permissions
 */
public class AdminUser extends User {
    
    public AdminUser(String username, String hashedPassword) {
        super(username, hashedPassword);
    }
    
    @Override
    public boolean hasPermission(String permission) {
        // Admins have all permissions
        return true;
    }
    
    public boolean canCreateChannels() {
        return true;
    }
    
    public boolean canDeleteChannels() {
        return true;
    }
    
    public boolean canManageUsers() {
        return true;
    }
    
    public boolean canDeleteMessages() {
        return true;
    }
    
    public boolean canBanUsers() {
        return true;
    }
    
    @Override
    public String toString() {
        return "AdminUser{" +
                "username='" + getUsername() + '\'' +
                ", isOnline=" + isOnline() +
                ", role=ADMIN" +
                '}';
    }
}
