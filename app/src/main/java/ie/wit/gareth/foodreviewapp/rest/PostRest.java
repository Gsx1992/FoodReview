package ie.wit.gareth.foodreviewapp.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

import ie.wit.gareth.foodreviewapp.models.Restaurant;
import ie.wit.gareth.foodreviewapp.models.Review;
import ie.wit.gareth.foodreviewapp.models.User;

/**
 * Created by Gareth on 14/04/2015.
 */
public class PostRest {

    final String URL = "https://dwd-app.herokuapp.com/api/v1/";

    public String postRequest(String query, Restaurant restaurant) throws IOException {

        HttpPost post = new HttpPost(URL+query);
        HttpClient client = new DefaultHttpClient();
        HashMap<String, Restaurant> hashMap = new HashMap<String, Restaurant>();
        hashMap.put("restaurant", restaurant);
        post.setHeader("Content-Type", "application/json; charset=utf-8");
        Type mapType = new TypeToken<HashMap<String, Restaurant>>(){}.getType();
        post.setEntity(new StringEntity(new Gson().toJson(hashMap, mapType)));
        HttpResponse response = client.execute(post);
        return response.getStatusLine().getStatusCode()+"";
    }

    public String postReviewRequest(String query, Review review) throws IOException {

        HttpPost post = new HttpPost(URL+query);
        HttpClient client = new DefaultHttpClient();
        HashMap<String, Review> hashMap = new HashMap<String, Review>();
        hashMap.put("review", review);
        post.setHeader("Content-Type", "application/json; charset=utf-8");
        Type mapType = new TypeToken<HashMap<String, Review>>(){}.getType();
        post.setEntity(new StringEntity(new Gson().toJson(hashMap, mapType)));
        HttpResponse response = client.execute(post);
        return response.getStatusLine().getStatusCode()+"";
    }

    public String postLoginRequest(String query, User user) throws IOException, JSONException {
        JSONObject obj = new JSONObject();
        HttpPost post = new HttpPost(URL+query);
        HttpClient client = new DefaultHttpClient();
        HashMap<String, User> hashMap = new HashMap<String, User>();
        hashMap.put("user", user);
        post.setHeader("Content-Type", "application/json; charset=utf-8");
        Type mapType = new TypeToken<HashMap<String, User>>(){}.getType();
        post.setEntity(new StringEntity(new Gson().toJson(hashMap, mapType)));
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode() == 200) {
        obj = new JSONObject(EntityUtils.toString(response.getEntity()));
                return obj.getString("id")+":"+obj.getString("admin");
        }
        else if (response.getStatusLine().getStatusCode() == 404){
            obj = new JSONObject(EntityUtils.toString(response.getEntity()));
            return obj.getString("message");
        }
        return "An Error has occured!";
    }
}
