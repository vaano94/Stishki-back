package org.rest.webapp.Rest;

import com.sun.jersey.spi.resource.Singleton;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by Ivan on 10/12/2015.
 */


@Path("/register")
@Singleton
public class RegisterService {

    @Context
    SecurityContext context;

    @GET
    @Path("/context")
    public String getUserPrincipal() {
        //System.out.println(context.getUserPrincipal().getName());
        return context.getUserPrincipal().getName();
    }

    @POST
     @Path("/add")
     //@Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     public User registerUser(
     @FormParam("nickName") String nickName,
     @FormParam("firstName") String firstName,
     @FormParam("password") String password,
     @FormParam("e-mail") String email) {

        UserService userService = new UserService();
        User user = new User();
        user.setNickName(nickName);
        user.setFirstName(firstName);
        //user.setPassword(password);
        user.setEmail(email);

        userService.persist(user);

        return user;
    /*return Response.status(200).entity(user.getEmail() + " "
                    + user.getFirstName() + " " +
                    user.getNickName()
    ).build();

    */}

    // end registerUser



}
