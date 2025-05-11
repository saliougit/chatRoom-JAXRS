package services;

import models.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final Map<String, List<Message>> messages = new ConcurrentHashMap<>();
    
    private static final ChatService instance = new ChatService();
    
    private ChatService() {}
    
    public static ChatService getInstance() {
        return instance;
    }
    
    // Gestion des salons
    public Room createRoom(String name) {
        String id = UUID.randomUUID().toString();
        Room room = new Room(id, name);
        rooms.put(id, room);
        messages.put(id, new ArrayList<>());
        return room;
    }
    
    public Room getRoom(String id) {
        return rooms.get(id);
    }
    
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }
    
    public void deleteRoom(String id) {
        rooms.remove(id);
        messages.remove(id);
    }
    
    // Gestion des utilisateurs
    public User createUser(String username) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, username);
        users.put(id, user);
        return user;
    }
    
    public User getUser(String id) {
        return users.get(id);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // Gestion des messages
    public Message createMessage(String content, String userId, String roomId) {
        String id = UUID.randomUUID().toString();
        Message message = new Message(id, content, userId, roomId);
        List<Message> roomMessages = messages.get(roomId);
        if (roomMessages != null) {
            roomMessages.add(message);
        }
        return message;
    }
    
    public List<Message> getRoomMessages(String roomId) {
        return messages.getOrDefault(roomId, new ArrayList<>());
    }
}
