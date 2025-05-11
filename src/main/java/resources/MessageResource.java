package resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import models.Message;
import services.ChatService;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {
    private final ChatService chatService = ChatService.getInstance();
    
    @GET
    public List<Message> getMessages(@QueryParam("roomId") String roomId) {
        return chatService.getRoomMessages(roomId);
    }
    
    @POST
    public Response createMessage(Message message) {
        Message created = chatService.createMessage(
            message.getContent(),
            message.getUserId(),
            message.getRoomId()
        );
        return Response.status(Response.Status.CREATED)
                      .entity(created)
                      .build();
    }
}
