package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.PoemInfo;
import org.rest.webapp.Services.PoemInfoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Ivan on 5/7/2016.
 */
@Path("/poeminfo/{poem}")
public class PoemInfoRest {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(@PathParam("poem") String clientData) {
        JSONObject result = new JSONObject();
        try {
            //JSONObject data = new JSONObject(clientData);
            JSONArray examples = new JSONArray();
            String type = clientData;

            PoemInfoService poemInfoService = new PoemInfoService();
            PoemInfo poemInfo = poemInfoService.getByType(type);

            result.put("shortDes", poemInfo.getShortDescription());
            result.put("sylRules", poemInfo.getSyllableRules());
            result.put("howTo", poemInfo.getHowToWrite());
            result.put("path", poemInfo.getImagePath());
            List<String> poemExamples = poemInfo.getExamples();
            for (String example : poemExamples) {
                examples.put(example);
            }
            result.put("examples", examples);


        }catch(JSONException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
