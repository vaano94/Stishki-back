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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 5/24/2016.
 */
@Path("/drafts")
public class DraftRest {

    @POST
    @Path("/get")
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


    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteDraft(String incomeData) {
        UserService userService = new UserService();
        JSONObject result = new JSONObject();
        try {
            result.put("result","BAD");
            JSONObject data = new JSONObject(incomeData);
            String token = data.getString("token");
            User user = userService.getByToken(token);
            if (user!=null) {
                Long number = data.getLong("id");
                Draft persistentInstance = userService.getPersistedDraft(number);
                List<Object> list = new ArrayList<Object>();
                list.add(user);
                list.add(persistentInstance);
                boolean isDeleted = userService.deletePersistedDraft(list);
                if (isDeleted) {
                    result.put("result","OK");
                    return result.toString();
                }
                else {
                    result.put("result","BAD");
                    return result.toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return result.toString();
        }

        return result.toString();
    }

}
