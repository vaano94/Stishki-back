package org.rest.webapp;

import org.rest.webapp.Entity.Poem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by Ivan on 10/7/2015.
 */
@Path("/hello")
public class HelloWorldService {
    @GET
    @Path("/{param}")
    public Response getMessage(@PathParam("param") String message) {
        String output = "Jersey says " + message;




        return Response.status(200).entity(output).build();
    }
}
