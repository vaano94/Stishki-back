import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ivan on 10/24/2015.
 */
public class PreloginTest {

    public static void main(String[] args) {

        try {

            // в одном дне 86400 секунд (ппц)

            long time1 = (System.currentTimeMillis()/1000L);
            long time = (System.currentTimeMillis()/1000L)+259200;

            String token = "UXE=1445975464";

            URL url = new URL("http://localhost:8080/rest/login/prelogin");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");


            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");


            osw.write(token);
            osw.flush();

            String sb = "";
            BufferedReader br = new BufferedReader(new InputStreamReader( connection.getInputStream(),"utf-8"));
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
