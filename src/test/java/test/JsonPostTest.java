import org.codehaus.jettison.json.JSONObject;
import sun.misc.BASE64Encoder;
import sun.net.www.http.HttpClient;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ivan on 10/18/2015.
 */
public class JsonPostTest {

    public static void main(String[] args) throws MalformedURLException {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickName", "he1y1");
            jsonObject.put("firstName", "h1ey1");
            jsonObject.put("password", "h1ey1");
            jsonObject.put("email", "1hey1");

            String authStringEnc = new BASE64Encoder().encode("hey:hey".getBytes());

            URL url = new URL("http://localhost:8080/rest/registerjson/add");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            connection.setRequestProperty("Content-Type", "application/json" );
            connection.setRequestProperty("Accept", "application/json");



            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            String b = jsonObject.toString();
            osw.write(b);
            osw.flush();


            String sb = null;
            BufferedReader br = new BufferedReader(new InputStreamReader( connection.getInputStream(),"utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb+=(line + "\n");
            }
            br.close();
            System.out.println(""+sb.toString());


            osw.flush();
            osw.close();



        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

}
