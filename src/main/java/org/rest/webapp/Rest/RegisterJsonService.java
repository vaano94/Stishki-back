package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.EncryptionService;
import org.rest.webapp.Services.UserService;
import sun.misc.BASE64Encoder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ivan on 10/18/2015.
 */
@Path("/registerjson")
public class RegisterJsonService {
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUser(String obj2) throws JSONException {


        JSONObject jsonObject = new JSONObject(obj2);
        System.out.println(jsonObject.toString());

        String nickName = (String) jsonObject.get("nickName");
        String firstName = (String) jsonObject.get("firstName");
        String password = (String) jsonObject.get("password");
        String email = (String) jsonObject.get("email");


        UserService userService = new UserService();
        User user = new User();

        // checks if email and nick are already created!
        for (User s : userService.getAll()) {
            if (s.getNickName().equals(nickName)) {
                return "NickNameIssue";
            }
            if (s.getEmail().equals(email)) {
                return "EmailIssue";
            }
        }
        // generate MD5 hash for password
        String passHash = EncryptionService.generatePassHash(password);

        // set user data
        user.setNickName(nickName);
        user.setFirstName(firstName);
        user.setPassword(passHash);
        user.setEmail(email);
        user.setType("user");

        userService.persist(user);
        System.out.println(obj2);
        return "OK";

    }

    // end registerUser


}
