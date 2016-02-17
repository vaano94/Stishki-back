import org.codehaus.jettison.json.JSONObject;
import sun.misc.BASE64Encoder;

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
            jsonObject.put("poem", "Слепцы стоят на дороге. Их веки - \n Как нависшие пологи. Вдалеке \n Воскресный звон" +
                    "с отстроверхой колокольни \n Мягко качается над полями");
            jsonObject.put("token", "ze0xNDQ1Mzc0NjI1MTA3");

            String authStringEnc = new BASE64Encoder().encode("hey:hey".getBytes());

            URL url = new URL("http://localhost:8080/rest/poem/add");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


