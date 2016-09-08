package ie.wit.gareth.foodreviewapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ie.wit.gareth.foodreviewapp.models.Restaurant;
import ie.wit.gareth.foodreviewapp.models.Review;
import ie.wit.gareth.foodreviewapp.rest.PostRest;


public class CreateReviewActivity extends ActionBarActivity {

    private String rest_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        Intent intent = getIntent();
        rest_id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        setTitle(name+" Review");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_review, menu);
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

    public void postReview(View view){

        String willReturn;
        EditText reviewTitle = (EditText) findViewById(R.id.reviewTitle);
        EditText reviewReview = (EditText) findViewById(R.id.reviewReview);
        CheckBox reviewCheck = (CheckBox) findViewById(R.id.reviewCheck);
        DatePicker reviewDate = (DatePicker) findViewById(R.id.reviewDate);

        String title = reviewTitle.getText().toString();
        String reviewText = reviewReview.getText().toString();
        boolean comeback = reviewCheck.isChecked();
        if(comeback){
            willReturn = "Yes";
        }
        else{
            willReturn = "No";
        }

        String date = reviewDate.getDayOfMonth()+"/"+(reviewDate.getMonth()+1)+"/"+reviewDate.getYear();
        if(TextUtils.isEmpty(title)){
            Toast.makeText(getApplicationContext(), "Please enter a title!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(reviewText)){
            Toast.makeText(getApplicationContext(), "Please enter a review!", Toast.LENGTH_SHORT).show();
        }

        else {

            SharedPreferences sharedPreferences = this. getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
            String user_id = sharedPreferences.getString("user_id", null);
            MediaPlayer media = MediaPlayer.create(getApplicationContext(), R.raw.getinmybelly);
            media.start();
            Review review = new Review(title, date, reviewText, willReturn, rest_id, user_id);
            new CreateReviewAsync().execute(review);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private class CreateReviewAsync extends AsyncTask<Review, Void, String> {


        @Override
        protected String doInBackground(Review... params) {
            Review r = params[0];
            String data = "";
            PostRest postRest = new PostRest();
            try {
                data =  postRest.postReviewRequest("reviews", r);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Toast.makeText(getApplicationContext(), "Review posted!", Toast.LENGTH_SHORT).show();

        }
    }
}
