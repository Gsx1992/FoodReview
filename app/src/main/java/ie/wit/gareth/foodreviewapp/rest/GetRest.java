package ie.wit.gareth.foodreviewapp.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Gareth on 13/04/2015.
 */
public class GetRest {

    final String URL = "https://dwd-app.herokuapp.com/api/v1/";

    public String getRequest(String query){
        HttpGet http = new HttpGet(URL+query);
        HttpClient client = new DefaultHttpClient();
        String data = "";
        try {
            HttpResponse response = client.execute(http);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
