package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.EncryptionService;
import org.rest.webapp.Services.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Created by Ivan on 5/20/2016.
 */
@Path("/userdata")
public class UserInfoRest {

    @POST
    @Path("/checkpass")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String checkPassword(String data) {
        JSONObject response = new JSONObject();

        try {
            UserService userService = new UserService();
            JSONObject userData = new JSONObject(data);
            String token = userData.getString("token");
            User user = userService.getByToken(token);
            if (user!=null) {
              String pass = userData.getString("password");
              if (EncryptionService.generatePassHash(pass)
                      .equals(user.getPassword())){
                  response.put("result","OK");
                  return response.toString();
              }
              else {
                  response.put("result", "Wrong Pass");
                  return response.toString();
                }
            }
            else {
                response.put("result", "User not found");
                return response.toString();
            }
        } catch (JSONException e) {
            try {
                response.put("result", "Wrong Pass");
                return response.toString();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return "Пшелнах";
    }

    @POST
    @Path("/changepass")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String changePass(String data) {
        JSONObject result = new JSONObject();
        try {
            UserService userService = new UserService();
            JSONObject userData = new JSONObject(data);
            result.put("result", "BAD");
            String token = userData.getString("token");
            User user = userService.getByToken(token);
            if (user!=null) {
                String password = userData.getString("password");
                user.setPassword(EncryptionService.generatePassHash(password));
                userService.update(user);
                result.put("result", "OK");
                return result.toString();
            } else {
                return result.toString();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @POST
    @Path("/changeinfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeInfo(String data) {
        JSONObject result = new JSONObject();
        try {
            UserService userService = new UserService();
            JSONObject userData = new JSONObject(data);
            result.put("result", "BAD");
            String token = userData.getString("token");
            User user = userService.getByToken(token);
            if (user != null) {
                String nickname = userData.getString("nickname");
                String email = userData.getString("email");
                JSONArray tags = userData.getJSONArray("genres");
                ArrayList<String> tagsArr = new ArrayList<String>();
                for (int i=0;i<tags.length();i++) {
                    tagsArr.add((String)tags.get(i));
                }
                user.setGenres(tagsArr);
                user.setNickName(nickname);
                user.setEmail(email);
                userService.update(user);
                result.put("result", "OK");
                return result.toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    return result.toString();
    }
}
