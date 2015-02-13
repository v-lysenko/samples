package lt.mexahuk.samples.cards.client;

import lt.mexahuk.samples.cards.api.Card;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;


@Path("/api/cards")
public interface CardResourceClient extends RestService {

    @GET
    @Path("/{id}")
    public void loadCard(@PathParam("id") String id, MethodCallback<Card> callback);

    @PUT
    @Path("/{id}/move-forward")
    public void moveForward(@PathParam("id") String id, MethodCallback<Card> callback);

    @PUT
    @Path("/{id}/unselect")
    public void unselect(@PathParam("id") String id, MethodCallback<Card> callback);

    @GET
    @Path("/latest")
    public void latest(MethodCallback<Card> callback);

    @POST
    public void newCard(MethodCallback<Card> callback);
}