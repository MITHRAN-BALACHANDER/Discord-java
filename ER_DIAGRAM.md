# Discord Clone - Entity Relationship Diagram

This document contains the Entity Relationship Diagram for the Discord Clone project, showing the data model and relationships between different entities.

## ER Diagram

```mermaid
erDiagram
    User {
        string userId PK
        string username UK
        string hashedPassword
        boolean isOnline
        date lastSeen
        string currentServer FK
    }
    
    AdminUser {
        string userId PK,FK
        boolean canCreateChannels
        boolean canDeleteChannels
        boolean canManageUsers
        boolean canBanUsers
    }
    
    ModeratorUser {
        string userId PK,FK
        boolean canMuteUsers
        boolean canKickUsers
        boolean canDeleteMessages
    }
    
    Server {
        string serverId PK
        string serverName
        string ownerId FK
        string ownerUsername
        string inviteCode UK
        date createdDate
        string description
    }
    
    Channel {
        string channelId PK
        string channelName
        string serverId FK
        date createdDate
        string channelType
    }
    
    TextChannel {
        string channelId PK,FK
        int maxMessageLength
    }
    
    VoiceChannel {
        string channelId PK,FK
        int maxUsers
        boolean isLocked
    }
    
    Message {
        string messageId PK
        string content
        string senderId FK
        string senderUsername
        date timestamp
        boolean isEdited
        date editedTimestamp
        string channelId FK
    }
    
    ServerMember {
        string serverId PK,FK
        string userId PK,FK
        string role
        date joinedDate
    }
    
    Friendship {
        string userId1 PK,FK
        string userId2 PK,FK
        date createdDate
    }
    
    DirectMessage {
        string messageId PK
        string senderId FK
        string recipientId FK
        string content
        date timestamp
        boolean isRead
    }
    
    MutedUser {
        string channelId PK,FK
        string userId PK,FK
        date mutedDate
        string mutedBy FK
    }
    
    BannedUser {
        string serverId PK,FK
        string userId PK,FK
        date bannedDate
        string bannedBy FK
        string reason
    }
    
    VoiceConnection {
        string channelId PK,FK
        string userId PK,FK
        date connectedAt
        boolean isMuted
        boolean isDeafened
    }

    %% Inheritance Relationships
    User ||--o| AdminUser : "is-a"
    User ||--o| ModeratorUser : "is-a"
    Channel ||--o| TextChannel : "is-a"
    Channel ||--o| VoiceChannel : "is-a"
    
    %% Primary Relationships
    User ||--o{ Server : "owns"
    User }o--o{ Server : "member of"
    Server ||--o{ Channel : "contains"
    Channel ||--o{ Message : "contains"
    User ||--o{ Message : "sends"
    
    %% Many-to-Many Relationships
    User }o--o{ User : "friends with"
    Server }o--o{ User : "has members"
    
    %% Association Tables
    ServerMember }o--|| Server : "belongs to"
    ServerMember }o--|| User : "represents"
    Friendship }o--|| User : "user1"
    Friendship }o--|| User : "user2"
    
    %% Direct Messages
    User ||--o{ DirectMessage : "sends DM"
    User ||--o{ DirectMessage : "receives DM"
    
    %% Moderation Relationships
    Channel ||--o{ MutedUser : "has muted users"
    User ||--o{ MutedUser : "is muted in"
    Server ||--o{ BannedUser : "has banned users"
    User ||--o{ BannedUser : "is banned from"
    
    %% Voice Connections
    VoiceChannel ||--o{ VoiceConnection : "has connections"
    User ||--o{ VoiceConnection : "connected to"
```

## Entity Descriptions

### Core Entities

#### User
- **Primary Entity**: Represents all users in the system
- **Key Attributes**: userId (PK), username (unique), hashedPassword
- **Relationships**: Can own servers, be members of servers, send messages

#### Server
- **Purpose**: Represents Discord-like servers/guilds
- **Key Attributes**: serverId (PK), serverName, ownerId, inviteCode (unique)
- **Relationships**: Owned by one user, contains multiple channels, has multiple members

#### Channel (Abstract)
- **Purpose**: Base entity for all channel types
- **Key Attributes**: channelId (PK), channelName, serverId, channelType
- **Specializations**: TextChannel, VoiceChannel

#### Message
- **Purpose**: Represents messages sent in channels
- **Key Attributes**: messageId (PK), content, senderId, channelId, timestamp
- **Relationships**: Belongs to one channel, sent by one user

### Specialized Entities

#### AdminUser / ModeratorUser
- **Purpose**: User specializations with specific permissions
- **Inheritance**: Extends User entity
- **Key Attributes**: Permission flags specific to role

#### TextChannel / VoiceChannel
- **Purpose**: Channel specializations with type-specific features
- **Inheritance**: Extends Channel entity
- **Key Attributes**: Type-specific properties (maxMessageLength, maxUsers)

### Association Entities

#### ServerMember
- **Purpose**: Many-to-many relationship between Users and Servers
- **Key Attributes**: serverId (FK), userId (FK), role, joinedDate
- **Purpose**: Tracks user membership and roles in servers

#### Friendship
- **Purpose**: Many-to-many self-relationship for User friends
- **Key Attributes**: userId1 (FK), userId2 (FK), createdDate
- **Purpose**: Represents friendship connections between users

### Moderation Entities

#### MutedUser
- **Purpose**: Tracks users muted in specific channels
- **Key Attributes**: channelId (FK), userId (FK), mutedDate, mutedBy (FK)

#### BannedUser
- **Purpose**: Tracks users banned from specific servers
- **Key Attributes**: serverId (FK), userId (FK), bannedDate, bannedBy (FK)

#### VoiceConnection
- **Purpose**: Tracks users connected to voice channels
- **Key Attributes**: channelId (FK), userId (FK), connectedAt, isMuted, isDeafened

## Relationship Types

### One-to-Many (1:N)
- User → Server (ownership)
- Server → Channel
- Channel → Message
- User → Message (authorship)

### Many-to-Many (M:N)
- User ↔ Server (membership) - via ServerMember
- User ↔ User (friendship) - via Friendship

### Inheritance (IS-A)
- AdminUser IS-A User
- ModeratorUser IS-A User
- TextChannel IS-A Channel
- VoiceChannel IS-A Channel

### Composition (HAS-A)
- Server HAS-A collection of Channels
- Channel HAS-A collection of Messages
- User HAS-A collection of Friends

## Key Design Features

1. **Inheritance Hierarchy**: Clear IS-A relationships for users and channels
2. **Polymorphism Support**: Base classes allow polymorphic behavior
3. **Flexible Permissions**: Role-based access control through user types
4. **Audit Trail**: Timestamps and creator tracking for moderation
5. **Scalable Design**: Separate entities for different concerns
6. **Data Integrity**: Foreign key relationships maintain consistency

This ER diagram represents the logical data model that supports all the OOP principles demonstrated in the Discord Clone implementation.
