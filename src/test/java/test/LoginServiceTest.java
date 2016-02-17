import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.rest.webapp.Rest.LoginService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by Ivan on 10/25/2015.
 */
public class LoginServiceTest {



    @Test
    public void testreportLoginStatusOK() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("nickName", "hey");
        credentials.put("password", "hey");
        LoginService service = new LoginService();
        String result = service.reportLoginStatus(credentials.toString());
        assertNotNull(result);
    }

    @Test
    public void testreportLoginStatusBad() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("nickName", "Qasd");
        credentials.put("password", "qasddd");
        LoginService service = new LoginService();
        String result = service.reportLoginStatus(credentials.toString());
        JSONObject jsonresult = new JSONObject(result);
        assertEquals("BAD", jsonresult.get("result"));
        assertEquals("", jsonresult.getString("token"));

    }


    @Test
    public void testreportLoginStatusException() throws JSONException{
        JSONObject creds = new JSONObject();
        creds.put("lol", "a");

        LoginService service = new LoginService();
        String result = service.reportLoginStatus(creds.toString());
        JSONObject jsonresult = new JSONObject(result);
        assertEquals(jsonresult.toString(), "{}");

    }


}