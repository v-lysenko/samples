package lt.mexahuk.samples.cards.api;

import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("cards")
public interface CardResource {

    @GET
    @Produces(APPLICATION_JSON)
    Iterable<Card> list();

    @GET
    @Path("latest")
    @Produces(APPLICATION_JSON)
    Card latest();

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    Card getCard(@PathParam("id") String id);

    @PUT
    @Path("/{id}/move-forward")
    @Produces(APPLICATION_JSON)
    Card moveForward(@PathParam("id") String id);

    @PUT
    @Path("/{id}/unselect")
    @Produces(APPLICATION_JSON)
    Card unselect(@PathParam("id") String id);

    @POST
    @Produces(APPLICATION_JSON)
    Card newCard();

//    @Deprecated
//    @PUT
//    @Path("/{id}")
//    @Produces(APPLICATION_JSON)
//    @Consumes(APPLICATION_JSON)
//    void updateCard(@PathParam("id") String id, Card card);
}
