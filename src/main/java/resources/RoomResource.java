package resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import models.Room;
import services.ChatService;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    private final ChatService chatService = ChatService.getInstance();
    
    @GET
    public List<Room> getAllRooms() {
        return chatService.getAllRooms();
    }
    
    @POST
    public Response createRoom(Room room) {
        Room created = chatService.createRoom(room.getName());
        return Response.status(Response.Status.CREATED)
                      .entity(created)
                      .build();
    }
    
    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {
        Room room = chatService.getRoom(id);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(room).build();
    }
}