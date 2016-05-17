package org.rest.webapp.Rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Entity.Dictionary;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.DictionaryService;
import org.rest.webapp.Services.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by Ivan on 5/12/2016.
 */
@Path("/rhymes")
public class DictionaryRest {

    @POST
    @Path("/rhyme")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getRhyme(String data) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            JSONObject userData = new JSONObject(data);
            String token = userData.getString("token");
            UserService userService = new UserService();
            User u = userService.getByToken(token);

            String rhyme = userData.getString("rhyme");
            int syllablesNumber = 0;

            JSONArray jsonArray = obtainRhymeList(rhyme);

            result.put("resut", "OK");
            result.put("rhymes", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
            result.put("result", "BAD");
            return result.toString();
        }
       return result.toString();
    }

    public JSONArray obtainRhymeList(String word) throws JSONException {
        StringBuilder buffer = new StringBuilder(word);
        StringBuilder end=null;
        List<String> endings = new ArrayList<String>();

        buffer.reverse();
        char letter = buffer.charAt(0);
        // if a word begins with a vowel
        if (letter=='а'||letter=='э'||letter=='у'||letter=='о'||letter=='и'
                ||letter=='ы'||letter=='ю'||letter=='e'||letter=='ё'||letter=='я') {
            for (int i = 1 ; i<buffer.length(); i++) {
                char loopLetter = buffer.charAt(i);
                if (loopLetter=='а'||loopLetter=='э'||loopLetter=='у'||loopLetter=='о'||loopLetter=='и'||
                        loopLetter=='ы'||loopLetter=='ю'||loopLetter=='e'||loopLetter=='ё'||loopLetter=='я') {
                    end = new StringBuilder(buffer.substring(0, i));
                    end.reverse();
                    endings.add(end.toString());
                    break;
                }
            }
            // now when we've found last syllable, cut it until length>=2
            int counter = 1; int endLength = end.toString().length();
            /*while (endLength>2) {
                String result = end.substring(counter, end.length());
                endings.add(result);
                endLength--;

                //endings.add(end.substring(counter, end.length()));
                counter++;
            }*/
        }
        // if a word begins with a consonant
        else {
            for (int i = 1 ; i<buffer.length(); i++) {
                // If the letter is syllable
                char loopLetter = buffer.charAt(i);
                if (loopLetter=='а'||loopLetter=='э'||loopLetter=='у'||loopLetter=='о'||loopLetter=='и'||
                        loopLetter=='ы'||loopLetter=='ю'||loopLetter=='e'||loopLetter=='ё'||loopLetter=='я') {
                    // we look for the second letter
                    char loopLetter2 = buffer.charAt(i+1);
                    if (loopLetter2!='а'||loopLetter2!='э'||loopLetter2!='у'||loopLetter2!='о'||loopLetter2!='и'||
                            loopLetter2!='ы'||loopLetter2!='ю'||loopLetter2!='e'||loopLetter2!='ё'||loopLetter2!='я') {
                        end = new StringBuilder(buffer.substring(0, i+2));
                        end.reverse();
                        endings.add(end.toString());
                        break;
                    }
                }
            }
            // now when we've found last syllable, cut it until length>=2
            int counter = 1; int endLength = end.toString().length();
            while (endLength>2) {
                String result = end.substring(counter, end.length());
                endings.add(result);
                endLength--;

                //endings.add(end.substring(counter, end.length()));
                counter++;
            }
        }

        List<Dictionary> commonList = new ArrayList<Dictionary>();
        DictionaryService dictionaryService = new DictionaryService();

        for (int i = 0; i < endings.size(); i++) {
            List<Dictionary> list = dictionaryService.getByEnding(endings.get(i));
            commonList.remove(list);
            commonList.addAll(list);
        }



        Comparator c = new Comparator<Dictionary>() {

            public int compare(Dictionary o1, Dictionary o2) {
                if (o1.getWord().length()>o2.getWord().length()) return 1;
                if (o1.getWord().length()==o2.getWord().length()) return 0;
                else return -1;
            }
        };
        System.out.println("Common list size is: " + commonList.size());

        Collections.sort(commonList, c);
        ArrayList<String> rhymes = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();
        Iterator<Dictionary> iter = commonList.iterator();

        ArrayList<Dictionary> toDelete = new ArrayList<Dictionary>();

        for (Dictionary d: commonList) {
            if (d.getWord().length()>word.length()+1 && d.getWord().length()<word.length()-1) {
                toDelete.add(d);
            }
        }
        for (Dictionary d: toDelete) {
            commonList.remove(d);
        }
        /*for (Dictionary d : commonList) {
            if (d.getWord().length()>word.length()+1) {
                commonList.remove(d);
            }
        }*/
        Set<String> allValues = new HashSet<String>();
        for (Dictionary d : commonList) {
            allValues.add(d.getWord());
        }


        for (String s : allValues) {
            jsonArray.put(s);
        }
        return jsonArray;
    }

}
