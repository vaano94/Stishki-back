import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ivan on 12/1/2015.
 */
public class GetPoemsByUserTest {

    public static void main(String[] args) {

        try {

            // в одном дне 86400 секунд (ппц)

            long time1 = (System.currentTimeMillis()/1000L);
            long time = (System.currentTimeMillis()/1000L)+259200;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", "weDh8/jq4Pk=1449512438");

            URL url = new URL("http://localhost:8080/poem/getbyuser");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");


            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            String b = jsonObject.toString();
            osw.write(b);
            osw.flush();

            String sb = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb+=line;
            }

            JSONArray array = new JSONArray(sb);
            for (int i=0;i<array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                //System.out.println(object.get("content"));
                //System.out.println(object.get("genre"));
                //System.out.println(object.get("hashtags"));
                //System.out.println(object.get("likes"));
                //System.out.println(object.get("dislikes"));
                //System.out.println(object.get("author"));
                //System.out.println("NEW POEm");
            }


            br.close();
            System.out.println(""+sb.toString());


            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
