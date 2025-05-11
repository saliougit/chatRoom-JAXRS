package models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    private String id;
    private String username;

    public User() {} // Constructeur par défaut nécessaire pour JAX-RS

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
