package org.rest.webapp;

import Utils.HibernateUtil;
import org.hibernate.Session;
import org.rest.webapp.Entity.Track;
import org.rest.webapp.Entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Ivan on 10/8/2015.
 */

@Path("/json/metallica")
public class SimpleJsonService {

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Track getTrackInJson() {
        Track track = new Track();
        track.setTitle("Enter Sandman");
        track.setSinger("Metallica");

        
        /*Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        User user = new User();*/



        /*session.save(user);
        session.getTransaction().commit();*/

        return track;
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrackInJSON(Track track) {

        String result = "Track saved : " + track;
        return Response.status(201).entity(result).build();


    }

}
