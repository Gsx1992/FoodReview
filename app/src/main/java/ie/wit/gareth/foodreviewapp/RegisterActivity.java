package ie.wit.gareth.foodreviewapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import ie.wit.gareth.foodreviewapp.models.User;
import ie.wit.gareth.foodreviewapp.rest.PostRest;


public class RegisterActivity extends ActionBarActivity {

    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerName = (EditText) findViewById(R.id.registerName);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerPasswordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);
    }



    public void registerButton(View view){

    String name = registerName.getText().toString();
    String email = registerEmail.getText().toString();
    String password = registerPassword.getText().toString();
    String password_confirm = registerPasswordConfirm.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password_confirm)) {
            Toast.makeText(getApplicationContext(), "Please re-enter your password", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(password_confirm)) {
            Toast.makeText(getApplicationContext(), "Both passwords must match!", Toast.LENGTH_SHORT).show();
        }

        else {

            User user = new User(registerName.getText().toString(), registerEmail.getText().toString(), registerPassword.getText().toString(), registerPasswordConfirm.getText().toString());
            new RegisterAsync().execute(user);
        }

    }

    private class RegisterAsync extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... params) {
            PostRest postRest = new PostRest();
            try {
                return postRest.postLoginRequest("users/register", params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Error";
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
