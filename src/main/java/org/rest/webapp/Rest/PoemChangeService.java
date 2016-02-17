package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.PoemService;
import org.rest.webapp.Services.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ivan on 11/13/2015.
 */

@Path("/poem")
public class PoemChangeService {

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPoem(String information) {
        String response= "bad";
        try {
            JSONObject result = new JSONObject(information);
            String poemText = result.getString("poem");
            String token = result.getString("token");
            String tags = result.getString("tags");


            UserService userService = new UserService();
            User user = userService.getByToken(token);

            Poem poem = new Poem();
            poem.setContent(poemText);
            poem.setDate(new Date());
            poem.setUser(user);
            String[] array = tags.split(" ");
            for (String s : array) {
                poem.getHashtags().add(s);
            }
            user.getPoems().add(poem);

            PoemService poemService = new PoemService();
            poemService.persist(poem);
            userService.update(user);

            response = "OK";


        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return response;
        }
    }

    @POST
    @Path("/newpoems")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewPoems(String s) {
        String response= "badresult";
        try {
                System.out.println(s);
                ArrayList<JSONObject> userPoems = new ArrayList<JSONObject>();
                JSONArray mainArray = new JSONArray();

                PoemService poemService = new PoemService();
                ArrayList<Poem> poemList = (ArrayList<Poem>) poemService.getNewOnes();

                for (Poem p : poemList) {

                    JSONObject poem = new JSONObject();
                    poem.put("genre", p.getGenre());
                    poem.put("content", p.getContent());
                    poem.put("hashtags", p.getHashtags().toString());
                    poem.put("likes", p.getLikes().size());
                    poem.put("dislikes", p.getDislikes().size());
                    poem.put("author", p.getUser().getNickName());
                    userPoems.add(poem);
                    mainArray.put(poem);
                }

                return mainArray.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @POST
    @Path("/getbyuser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPoemsById(String information) {
        String response= "bad";
        try {
            JSONObject result = new JSONObject(information);
            String token = result.getString("token");

            UserService userService = new UserService();
            User u = userService.getByToken(token);

            ArrayList<JSONObject> userPoems = new ArrayList<JSONObject>();
            JSONArray mainArray = new JSONArray();

            String poemNumber = "poem";
            int count = 0;
            for (Poem p : u.getPoems()) {

                JSONObject poem = new JSONObject();
                poem.put("genre", p.getGenre());
                poem.put("content", p.getContent());
                poem.put("hashtags", p.getHashtags().toString());
                poem.put("likes", p.getLikes().size());
                poem.put("dislikes", p.getDislikes().size());
                poem.put("author", u.getFirstName());
                userPoems.add(poem);


                poemNumber = poemNumber+count;

                mainArray.put(poem);


            }

            return mainArray.toString();
        }
        catch(Exception e) {
                e.printStackTrace();
            }
        return response;
    }


}
