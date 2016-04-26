package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.PoemService;
import org.rest.webapp.Services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
        String response= "BAD";
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
        String response= "BAD";
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
                    JSONArray hashtags = new JSONArray();
                    for (String tag : p.getHashtags()){
                        hashtags.put(tag);
                    }
                    poem.put("hashtags", hashtags);
                    //poem.put("hashtags", p.getHashtags().toString());
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

    @POST
    @Path("/hashtags")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPoemsByHashTag(String hashtags) {
        String response = "BAD";
        System.out.println(hashtags);
        JSONObject result = new JSONObject();
        try {

        JSONObject jsonObject = new JSONObject(hashtags);
        String hashs = jsonObject.getString("hashtags");
        PoemService poemService = new PoemService();
        List<Poem> list = poemService.getByHashTag(hashs);
        ArrayList<JSONObject> poems = new ArrayList<JSONObject>();

        JSONArray mainArray = new JSONArray();

        String poemNumber = "poem";
        int count = 0;
        for (Poem p : list) {

            JSONObject poem = new JSONObject();
            poem.put("genre", p.getGenre());
            poem.put("content", p.getContent());
            JSONArray poemhashtags = new JSONArray();
            for (String tag : p.getHashtags()){
                poemhashtags.put(tag);
            }
            poem.put("author", p.getUser().getNickName());
            poem.put("hashtags", poemhashtags);
            poem.put("likes", p.getLikes().size());
            poem.put("dislikes", p.getDislikes().size());

            poems.add(poem);
            poemNumber = poemNumber + count;

            mainArray.put(poem);
            }
            result.put("result", "OK");
            result.put("poems",poems);
            return result.toString();
        }
        catch(JSONException e) {
            e.printStackTrace();
        }


        try {
            result.put("result","BAD");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


}
