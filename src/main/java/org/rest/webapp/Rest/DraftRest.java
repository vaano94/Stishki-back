package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.Draft;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Ivan on 5/24/2016.
 */
@Path("/drafts")
public class DraftRest {

    @POST
    @Path("get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getDrafts(String data) {
        JSONObject result = new JSONObject();
        UserService userService = new UserService();
        try {
            result.put("result","BAD");
            JSONObject details = new JSONObject(data);
            User user = userService.getByToken(details.getString("token"));
            JSONArray draftArray = new JSONArray();
            if (user!=null) {
                List<Draft> drafts = user.getDrafts();
                for (Draft d: drafts) {
                    JSONObject draft = new JSONObject();
                    draft.put("content", d.getContent());
                    draft.put("date", d.getDate());
                    draft.put("genre",d.getGenre());
                    JSONArray drafthashtags = new JSONArray();
                    for (String tag : d.getHashtags()) {
                        drafthashtags.put(tag);
                    }
                    draft.put("hashtags", drafthashtags);
                    draft.put("id",d.getId());
                    draftArray.put(draft);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
