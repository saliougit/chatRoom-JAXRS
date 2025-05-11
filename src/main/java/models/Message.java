package models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
    private String id;
    private String content;
    private String userId;
    private String roomId;
    private long timestamp;

    public Message() {} // Constructeur par défaut nécessaire pour JAX-RS

    public Message(String id, String content, String userId, String roomId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.roomId = roomId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
