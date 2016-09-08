package ie.wit.gareth.foodreviewapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ie.wit.gareth.foodreviewapp.models.Restaurant;
import ie.wit.gareth.foodreviewapp.rest.GetRest;
import ie.wit.gareth.foodreviewapp.rest.PostRest;


public class CreateRestaurantActivity extends ActionBarActivity {
    private EditText restName;
    private Spinner spinner;
    private EditText restAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        spinner = (Spinner) findViewById(R.id.spinner);
        restName = (EditText) findViewById(R.id.restName);
        restAddress = (EditText) findViewById(R.id.restAddress);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.food_types, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void actionButton(View view){

        if(restName.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a restaurant name!", Toast.LENGTH_SHORT).show();
        }

        else if(restAddress.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter the restaurants address", Toast.LENGTH_SHORT).show();
        }

        else {
            Restaurant restaurant = new Restaurant(restName.getText().toString(), spinner.getSelectedItem().toString(), restAddress.getText().toString());
            new CreateRestaurantAsync().execute(restaurant);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }






        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);

    }

    private class CreateRestaurantAsync extends AsyncTask<Restaurant, Void, String> {


        @Override
        protected String doInBackground(Restaurant... params) {
            Restaurant r = params[0];
            String data = "";
            PostRest postRest = new PostRest();
            try {
                data =  postRest.postRequest("restaurants", r);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Toast.makeText(getApplicationContext(), "Restaurant Posted!", Toast.LENGTH_SHORT).show();

        }
    }


}
