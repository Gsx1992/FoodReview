package ie.wit.gareth.foodreviewapp.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Gareth on 15/04/2015.
 */
public class DeleteRest {

    final String URL = "https://dwd-app.herokuapp.com/api/v1/";

    public String deleteRequest(String query) throws IOException, JSONException {

        HttpDelete delete = new HttpDelete(URL + query);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(delete);
        HttpEntity entity = response.getEntity();
        JSONObject obj = new JSONObject();
        if (response.getStatusLine().getStatusCode() == 200) {
            obj = new JSONObject(EntityUtils.toString(response.getEntity()));
            return obj.getString("message");
        }
        else if(response.getStatusLine().getStatusCode() == 404){
            obj = new JSONObject(EntityUtils.toString(response.getEntity()));
            return obj.getString("error");
        }

        return "An error has occurred, please check that you have an active internet connection!";
    }
}