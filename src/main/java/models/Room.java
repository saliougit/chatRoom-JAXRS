package models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Room {
    private String id;
    private String name;

    public Room() {} // Constructeur par défaut nécessaire pour JAX-RS

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
