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
            String genre = result.getString("genre");


            UserService userService = new UserService();
            User user = userService.getByToken(token);

            Poem poem = new Poem();
            poem.setContent(poemText);
            poem.setDate(new Date());
            poem.setUser(user);
            poem.setGenre(genre);
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
                    poem.put("likes", p.getLikes().size());
                    poem.put("id", p.getId());
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
    @Path("/offset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getByOffset(String s) {
        String response= "BAD";
        try {
            System.out.println(s);
            ArrayList<JSONObject> userPoems = new ArrayList<JSONObject>();
            JSONArray mainArray = new JSONArray();

            PoemService poemService = new PoemService();
            ArrayList<Poem> poemList = (ArrayList<Poem>) poemService.getByOffset(Integer.parseInt(s));

            JSONObject result = new JSONObject();

            for (Poem p : poemList) {

                JSONObject poem = new JSONObject();
                poem.put("genre", p.getGenre());
                poem.put("content", p.getContent());
                JSONArray hashtags = new JSONArray();
                for (String tag : p.getHashtags()){
                    hashtags.put(tag);
                }
                poem.put("hashtags", hashtags);
                poem.put("likes", p.getLikes().size());
                poem.put("id", p.getId());
                poem.put("dislikes", p.getDislikes().size());
                poem.put("author", p.getUser().getNickName());
                userPoems.add(poem);
                mainArray.put(poem);
            }

            result.put("offset", Integer.parseInt(s)+40);
            result.put("poems", mainArray);
            return result.toString();
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
                poem.put("id", p.getId());
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
            poem.put("id", p.getId());
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

    @POST
    @Path("/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String setLike(String data) {
        JSONObject result = new JSONObject();

        try {
            JSONObject clientData = new JSONObject(data);
            UserService userService = new UserService();
            System.out.println(data);
            // If no token is present - kick out of method
            if (clientData.getString("token").equals("")) {
                result.put("result","BAD");
                return result.toString();
            }
            else {
                PoemService poemService = new PoemService();
                Poem poem = poemService.getById(clientData.getInt("id"));
                //System.out.println(poem.getContent());
                //Check if token is already sent by a user
                long userId = userService.getByToken(clientData.getString("token")).getId();
                //System.out.println(poem.getLikes());
                //System.out.println(poem.getLikes().size());
                // if Likes Contain user id, it means we should decrement it e.g delete entry
                if (poem.getLikes().contains(userId)) {
                    result.put("result", "decrement");
                    poem.getLikes().remove(userId);
                    poemService.update(poem);
                }
                // If token is not sent, set like
                else {
                    result.put("result","increment");
                    poem.addLike(userService.getByToken(clientData.getString("token")).getId());
                    poemService.update(poem);
                }

                //poem.addLike(userService.getByToken(clientData.getString("token")));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            return result.toString();
        }

        return result.toString();
    }


    @POST
    @Path("/dislike")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String setDislike(String data) {
        JSONObject result = new JSONObject();

        try {
            JSONObject clientData = new JSONObject(data);
            UserService userService = new UserService();
            // If no token is present - kick out of method
            if (clientData.getString("token").equals("")) {
                result.put("result","BAD");
                return result.toString();
            }
            else {
                PoemService poemService = new PoemService();
                Poem poem = poemService.getById(clientData.getInt("id"));
                System.out.println(poem.getContent());
                //Check if token is already sent by a user
                long userId = userService.getByToken(clientData.getString("token")).getId();
                List<Long> dislikes = poem.getDislikes();
                System.out.println(dislikes);
                System.out.println(dislikes.size());
                // if Likes Contain user id, it means we should decrement it e.g delete entry
                if (dislikes.contains(userId)) {
                    result.put("result", "decrement");
                    dislikes.remove(userId);
                    poemService.update(poem);
                }
                // If token is not sent, set like
                else {
                    result.put("result","increment");
                    dislikes.add(userId);
                    poemService.update(poem);
                }

                //poem.addLike(userService.getByToken(clientData.getString("token")));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toString();

    }



}
