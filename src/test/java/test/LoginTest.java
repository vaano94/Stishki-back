import org.codehaus.jettison.json.JSONObject;
import org.rest.webapp.Services.EncryptionService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ivan on 10/22/2015.
 */


public class LoginTest {

    public static void main(String[] args) {

        try {

            // в одном дне 86400 секунд (ппц)

            long time1 = (System.currentTimeMillis()/1000L);
            long time = (System.currentTimeMillis()/1000L)+259200;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickName", "blah");
            jsonObject.put("password", "blah");

            URL url = new URL("http://localhost:8080/rest/login/go");

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
                sb+=(line + "\n");
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
