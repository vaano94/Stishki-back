import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ivan on 11/13/2015.
 */
public class PoemAddTest {

    public static void main(String[] args) {


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("poem", "на джып соседа глеб с балкона\n" +
                    "бросает ночью пару дынь\n" +
                    "тыдыннь он слышит с наслажденьем\n" +
                    "тыдыннь");
            jsonObject.put("token", "38c5abdb9f09ca6e3e403dde52ed98eb6304d3549fdae91afc025b87fae70faf");
            jsonObject.put("genre","Порошок");
            jsonObject.put("tags", "one two three");

            //String authStringEnc = new BASE64Encoder().encode("hey:hey".getBytes());
            for (int i=0;i<40;i++) {

            URL url = new URL("http://localhost:8080/rest/poem/add");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");


                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

                String b = jsonObject.toString();
                osw.write(b);
                osw.flush();


                String sb = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb += (line + "\n");
                }
                br.close();
                System.out.println("" + sb.toString());


                osw.flush();
                osw.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


