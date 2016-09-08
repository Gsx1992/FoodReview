package ie.wit.gareth.foodreviewapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ie.wit.gareth.foodreviewapp.models.Restaurant;
import ie.wit.gareth.foodreviewapp.models.User;
import ie.wit.gareth.foodreviewapp.rest.GetRest;
import ie.wit.gareth.foodreviewapp.rest.PostRest;


public class LoginActivity extends ActionBarActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerLink = (TextView) findViewById(R.id.registerLink);

        SharedPreferences sharedPreferences = this. getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);
        if(user_id != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });
    }



    public void loginButton(View view) {

        if(TextUtils.isEmpty(loginEmail.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter an email!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(loginPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_SHORT).show();

        }
        else {
            User user = new User(loginEmail.getText().toString(), loginPassword.getText().toString());
            new LoginAsync().execute(user);
        }

    }


    private class LoginAsync extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... params) {
            PostRest postRest = new PostRest();
            try {
                return postRest.postLoginRequest("users/log_in", params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String data) {

            if(data.split(":")[0].length() < 5){
                Toast.makeText(getApplicationContext(), "Logging in!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id", data.split(":")[0]);
                if(data.contains("true")){
                    editor.putString("admin", "true");
                }
                editor.commit();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
            else{
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(data);

        }
    }
}