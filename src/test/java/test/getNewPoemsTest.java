import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ivan on 12/3/2015.
 */
public class getNewPoemsTest {

        public static void main(String[] args) {

            try {

                // в одном дне 86400 секунд (ппц)

                URL url = new URL("http://localhost:8080/rest/poem/newpoems");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");


                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

                String b = "getpoems";
                osw.write(b);
                osw.flush();

                String sb = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    sb+=line;
                }
                br.close();



                osw.flush();
                osw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

}
