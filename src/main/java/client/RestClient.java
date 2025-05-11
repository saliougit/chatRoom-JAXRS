package client;

import models.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.util.List;

public class RestClient {    private static final String BASE_URL = "http://localhost:8080/chatroom/api";
    private final Client client;
    private final WebTarget baseTarget;

    public RestClient() {
        this.client = ClientBuilder.newClient();
        this.baseTarget = client.target(BASE_URL);
    }    // User operations
    public User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        return baseTarget.path("users")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(user), User.class);
    }

    public List<User> getAllUsers() {
        return baseTarget.path("users")
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<User>>(){});
    }

    public User getUser(String id) {
        return baseTarget.path("users")
                        .path(id)
                        .request(MediaType.APPLICATION_JSON)
                        .get(User.class);
    }

    // Room operations
    public Room createRoom(String name) {
        Room room = new Room();
        room.setName(name);
        return baseTarget.path("rooms")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(room), Room.class);
    }

    public List<Room> getAllRooms() {
        return baseTarget.path("rooms")
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Room>>(){});
    }

    // Message operations
    public Message sendMessage(String content, String userId, String roomId) {
        Message message = new Message();
        message.setContent(content);
        message.setUserId(userId);
        message.setRoomId(roomId);
        return baseTarget.path("messages")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(message), Message.class);
    }

    public List<Message> getRoomMessages(String roomId) {
        return baseTarget.path("messages")
                        .queryParam("roomId", roomId)
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Message>>(){});
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
