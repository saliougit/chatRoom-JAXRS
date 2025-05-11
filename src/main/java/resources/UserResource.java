package resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import models.User;
import services.ChatService;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final ChatService chatService = ChatService.getInstance();
    
    @GET
    public List<User> getAllUsers() {
        return chatService.getAllUsers();
    }
    
    @POST
    public Response createUser(User user) {
        User created = chatService.createUser(user.getUsername());
        return Response.status(Response.Status.CREATED)
                      .entity(created)
                      .build();
    }
    
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id) {
        User user = chatService.getUser(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
}
