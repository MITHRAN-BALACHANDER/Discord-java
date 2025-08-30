# Discord Clone - Java Console Edition

A comprehensive console-based Discord clone implemented in Java, showcasing all fundamental Object-Oriented Programming (OOP) principles through real-world application design.

## 🎯 Project Overview

This project demonstrates a scalable communication platform that replicates Discord's core functionalities while maintaining clean, educational code that highlights OOP principles:

- **Encapsulation**: Private fields with controlled access through getters/setters
- **Inheritance**: User hierarchy (User → AdminUser, ModeratorUser) and Channel hierarchy (Channel → TextChannel, VoiceChannel)
- **Polymorphism**: Method overriding and dynamic behavior based on user roles and channel types
- **Abstraction**: Abstract Channel class defining contracts for different channel implementations
- **Composition**: Server contains Channels, User contains FriendList, demonstrating "has-a" relationships

## 🚀 Features

### 👤 User Management
- **Registration & Authentication**: Secure user registration with password hashing
- **Role-based Access Control**: Admin, Moderator, and Member roles with different permissions
- **Session Management**: Login/logout with online status tracking
- **Friend System**: Add/remove friends with direct messaging capabilities

### 🏢 Server Management
- **Create/Delete Servers**: Full server lifecycle management
- **Invite System**: Unique invite codes for server joining
- **Member Management**: Kick, ban, and role assignment capabilities
- **Multi-server Support**: Users can join multiple servers simultaneously

### 💬 Communication
- **Text Channels**: Real-time text messaging with edit/delete capabilities
- **Voice Channels**: Simulated voice chat with connection management
- **Direct Messages**: Private messaging between friends
- **Message History**: Persistent message storage and search functionality

### 🔧 Advanced Features
- **Permission System**: Role-based command execution
- **Channel Moderation**: Mute/unmute users in specific channels
- **Message Management**: Edit, delete, and search message history
- **Voice Simulation**: Simulated voice actions (speak, mute, deafen)

## 📂 Project Structure

```
src/
├── Main.java                    # Application entry point with console interface
├── models/                      # Data models (Encapsulation & Inheritance)
│   ├── User.java               # Base user class with common functionality
│   ├── AdminUser.java          # Admin user with elevated permissions
│   ├── ModeratorUser.java      # Moderator user with limited admin rights
│   ├── Server.java             # Server containing channels and members
│   ├── Channel.java            # Abstract channel class (Abstraction)
│   ├── TextChannel.java        # Text messaging implementation
│   ├── VoiceChannel.java       # Voice chat simulation
│   └── Message.java            # Message data structure
├── services/                    # Business logic layer
│   ├── AuthService.java        # User authentication and session management
│   ├── ServerService.java      # Server operations and member management
│   └── ChannelService.java     # Channel operations and messaging
└── utils/                       # Utility classes
    ├── HashUtil.java           # Password hashing and security
    └── ConsoleUtil.java        # Console formatting and input helpers
```

## 🏗️ OOP Principles Demonstrated

### 1. Encapsulation
- All model classes use private fields with public getters/setters
- Controlled access to internal data structures
- Data validation within setter methods

### 2. Inheritance
```java
User (base class)
├── AdminUser (full permissions)
└── ModeratorUser (limited permissions)

Channel (abstract base)
├── TextChannel (text messaging)
└── VoiceChannel (voice simulation)
```

### 3. Polymorphism
- Method overriding: `hasPermission()` behaves differently for each user type
- Dynamic dispatch: Channel operations work differently for Text vs Voice channels
- Interface implementation: Common contracts with different implementations

### 4. Abstraction
- Abstract `Channel` class defines common interface for all channel types
- Service layer abstracts business logic from presentation layer
- Clear separation of concerns across packages

### 5. Composition
- Server **has-a** collection of Channels
- User **has-a** FriendList and ServerList
- Message **has-a** sender (User reference)

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Any Java IDE or text editor
- Command line access

### Installation & Running

1. **Clone the repository**:
   ```bash
   git clone https://github.com/MITHRAN-BALACHANDER/Discord-java.git
   cd Discord-java
   ```

2. **Compile the project**:
   ```bash
   javac -d bin src/**/*.java src/*.java
   ```

3. **Run the application**:
   ```bash
   java -cp bin Main
   ```

### Quick Start with Demo Accounts

The application comes with pre-configured demo accounts:

| Username  | Password | Role      | Permissions                                    |
|-----------|----------|-----------|------------------------------------------------|
| admin     | admin123 | Admin     | All permissions (create/delete channels, ban users, etc.) |
| moderator | mod123   | Moderator | Limited permissions (mute users, delete messages)        |
| user      | user123  | Member    | Basic permissions (send messages, join channels)         |

## 📖 Usage Examples

### Example Workflow

1. **Login**: Use demo account `admin`/`admin123`
2. **Join Demo Server**: Use invite code from existing demo server
3. **Create Channels**: Create text and voice channels
4. **Messaging**: Send, edit, and delete messages
5. **User Management**: Demonstrate role-based permissions
6. **Voice Simulation**: Join voice channels and simulate actions

### Key Interactions

```java
// Polymorphism in action
User admin = new AdminUser("admin", hashedPassword);
User moderator = new ModeratorUser("mod", hashedPassword);

// Different behavior based on user type
admin.hasPermission("ban_users");      // returns true
moderator.hasPermission("ban_users");  // returns false

// Inheritance and method overriding
Channel textChannel = new TextChannel("general", serverId);
Channel voiceChannel = new VoiceChannel("voice-chat", serverId);

// Same interface, different implementations
textChannel.sendMessage("Hello!", user);    // Stores text message
voiceChannel.sendMessage("Hello!", user);   // Handles voice-style message
```

## 🔧 Technical Implementation

### Design Patterns Used
- **Factory Pattern**: User creation based on role type
- **Observer Pattern**: Implicit in message broadcasting
- **Strategy Pattern**: Different permission strategies for user roles
- **Composite Pattern**: Server containing multiple channels

### Key Technical Features
- **Secure Password Handling**: SHA-256 hashing with salt
- **Memory Management**: Efficient data structures and collections
- **Error Handling**: Comprehensive input validation and error messages
- **Extensible Architecture**: Easy to add new user roles or channel types

## 🎓 Educational Value

This project serves as an excellent learning resource for:

- **Object-Oriented Programming**: All four pillars demonstrated with real-world examples
- **Software Architecture**: Clean separation of concerns and modular design
- **Design Patterns**: Multiple patterns implemented naturally
- **Java Best Practices**: Modern Java coding standards and conventions
- **Security Fundamentals**: Basic password hashing and validation

## 🔮 Future Enhancements

- **Database Integration**: Persistent data storage
- **Network Communication**: Real multi-user support
- **GUI Interface**: JavaFX or Swing frontend
- **File Sharing**: Attachment and media support
- **Advanced Permissions**: Fine-grained permission system
- **Server Channels**: Categories and channel organization

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by Discord's excellent communication platform
- Educational focus on demonstrating OOP principles
- Built for learning and teaching object-oriented programming concepts

---

**Note**: This is an educational project designed to demonstrate OOP principles. It's not intended for production use but serves as an excellent learning resource for understanding object-oriented design and implementation.

