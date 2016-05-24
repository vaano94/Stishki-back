package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.EncryptionService;
import org.rest.webapp.Services.PoemService;
import org.rest.webapp.Services.UserService;
import sun.misc.BASE64Encoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Ivan on 10/22/2015.
 */

@Path("/login")
public class LoginService {

    @POST
    @Path("/go")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String reportLoginStatus(String credentials) {
        JSONObject result = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject(credentials);
            String login = (String) jsonObject.get("nickName");
            String password = (String) jsonObject.get("password");

            byte[] tokenByte = null;
            String token = EncryptionService.generateToken();


            // в одном дне 86400 секунд (ппц)
            // в трех днях 259200

            UserService userService = new UserService();
            // if userField is empty add token and timeStamp
            long time = (System.currentTimeMillis()/1000L);
            long timePlusThreeDays = (System.currentTimeMillis()/1000L)+259200;

            // check if user already exists
            User userByNick = userService.getByNickName(login);
            if (userByNick==null) {
                result.put("result", "BAD");
                result.put("token", "");
                return result.toString();
            }

            // if password hashes are equal check further
            if (userByNick.getPassword().equals(EncryptionService.generatePassHash(password))) {
                // if no Token present, create a new one and set timestamp
                if (userByNick.getToken() == null || userByNick.getToken().isEmpty()) {
                    token = EncryptionService.generateToken();
                    timePlusThreeDays = (System.currentTimeMillis()/1000L)+259200;
                    userByNick.setToken(token);
                    userByNick.setTokenExpirationTime(timePlusThreeDays);
                    userService.update(userByNick);
                    result.put("result", "OK");
                    result.put("token",token);
                    result.put("id", userByNick.getId());
                    JSONArray genres = new JSONArray();
                    for (int i = 0; i < userByNick.getGenres().size(); i++) {
                        genres.put(userByNick.getGenres().get(i));
                    }
                    result.put("genres", genres);
                    return result.toString();
                }
                // else check existing tokens
                else {
                    // if tokens are equal, and dates are valid everything is ok
                    if (token.equals(userByNick.getToken()) && userByNick.getTokenExpirationTime()>timePlusThreeDays) {
                        result.put("result", "OK");
                        result.put("token",userByNick.getToken());
                        result.put("id", userByNick.getId());
                        JSONArray genres = new JSONArray();
                        for (int i = 0; i < userByNick.getGenres().size(); i++) {
                            genres.put(userByNick.getGenres().get(i));
                        }
                        result.put("genres", genres);
                        return result.toString();
                    }
                    // else we generate new one if old is deprecated and create new TimeStamp
                    else {
                        token = EncryptionService.generateToken();
                        timePlusThreeDays = (System.currentTimeMillis()/1000L)+259200;
                        userByNick.setToken(token);
                        userByNick.setTokenExpirationTime(timePlusThreeDays);
                        userService.update(userByNick);

                        result.put("result", "OK");
                        result.put("token",token);
                        result.put("id", userByNick.getId());
                        JSONArray genres = new JSONArray();
                        for (int i = 0; i < userByNick.getGenres().size(); i++) {
                            genres.put(userByNick.getGenres().get(i));
                        }
                        result.put("genres", genres);
                        return result.toString();

                    }
                }
            }
            else {
                result.put("result", "BAD");
                result.put("token","");
                return result.toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    @POST
    @Path("/prelogin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String preLogin(String clientToken) {
        String result = "";
        String token;




        UserService userService = new UserService();
        for (User user : userService.getAll()) {
            if (user.getToken().equals(clientToken)) {
                long newTime = System.currentTimeMillis()/1000L+259200;
                StringBuilder builder = new StringBuilder();
                builder.append(clientToken);
                builder.reverse();
                String s =builder.substring(0,10);

                if (Long.parseLong(s) > newTime) {
                    //generate new token
                    String login = user.getNickName();
                    String password = user.getPassword();
                    String logpass = login+password;
                    token = new BASE64Encoder().encode(logpass.getBytes()) + ((System.currentTimeMillis()/1000L)+259200);
                    user.setToken(token);
                    userService.update(user);
                    return token;
                }
                else {return clientToken;}

            }
        }
        return clientToken;
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String reportLogoutStatus(String token) {
        String result = "";
        UserService userService = new UserService();
        for (User u: userService.getAll()) {
            if (u.getToken().equals(token)) {
                u.setToken(null);
                userService.update(u);
                result = "logoutOK";
                break;
            }

        }
        return result;
    }

    @POST
    @Path("/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserDetails(String clientToken) {
        String result = "";

        try {
            UserService userService = new UserService();
            JSONObject jsonObject = new JSONObject(clientToken);
            String token = jsonObject.getString("token");
            User user = userService.getByToken(token);
            PoemService poemService = new PoemService();


            int likes = 0;
            int dislikes = 0;

            for (Poem p: user.getPoems()) {
                likes += p.getLikes().size();
                dislikes += p.getDislikes().size();
            }
            System.out.println("LIKES : " + likes + " DISLIKES :" + dislikes);

            System.out.println("################# " + user.getEmail() + " #################");
            /*for (Poem p : user.getPoems()) {
                System.out.println(p.getContent());

            }*/
            JSONArray genres = new JSONArray();
            for (int i = 0; i < user.getGenres().size(); i++) {
                genres.put(user.getGenres().get(i));
            }

            JSONObject userDetails = new JSONObject();
            userDetails.put("name", user.getFirstName());
            userDetails.put("nickName", user.getNickName());
            userDetails.put("email", user.getEmail());
            userDetails.put("likes", String.valueOf(likes));
            userDetails.put("dislikes", String.valueOf(dislikes));
            userDetails.put("poemcount", user.getPoems().size());
            userDetails.put("genres", genres);
            result = userDetails.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
