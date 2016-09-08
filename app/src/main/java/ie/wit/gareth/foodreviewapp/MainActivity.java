package ie.wit.gareth.foodreviewapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ie.wit.gareth.foodreviewapp.models.Restaurant;
import ie.wit.gareth.foodreviewapp.rest.DeleteRest;
import ie.wit.gareth.foodreviewapp.rest.GetRest;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {


    public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainAsync().execute();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
                String admin = sharedPreferences.getString("admin", null);
                if(admin != null){
                final int pos = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete?");
                alert.setMessage("This cant be undone, all reviews for this restaurant will also be deleted.");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RestaurantDeleteAsync().execute(restaurants.get(pos).getId());
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.remove("admin");
            editor.commit();
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }

        else if(id == R.id.action_create_rest){
            Intent i = new Intent(getApplicationContext(), CreateRestaurantActivity.class);
            startActivity(i);
        }

        else if(id == R.id.action_website){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://dwd-app.herokuapp.com"));
            startActivity(i);
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), FragmentActivity.class);
        i.putExtra("id", restaurants.get(position).getId());
        i.putExtra("name", restaurants.get(position).getName());
        i.putExtra("food", restaurants.get(position).getFood());
        i.putExtra("address", restaurants.get(position).getAddress());
        i.putExtra("lat", restaurants.get(position).getLatLng().latitude);
        i.putExtra("lng", restaurants.get(position).getLatLng().longitude);
        startActivity(i);
    }


    private  class RestaurantDeleteAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            DeleteRest deleteRest = new DeleteRest();
            try {
                return deleteRest.deleteRequest("restaurants/"+params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "An Error has occured";

        }

        @Override
        protected void onPostExecute(String data) {

            super.onPostExecute(data);
            Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();


        }
    }

    private class MainAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            GetRest getRest = new GetRest();
            return getRest.getRequest("restaurants");

        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    LatLng latLng = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                    Restaurant p = new Restaurant(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("food"), jsonObject.getString("address"), latLng);
                    restaurants.add(p);

                }
                RestaurantAdapter radapter = new RestaurantAdapter(getApplicationContext(), restaurants);
                listView.setAdapter(radapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
            super(context, R.layout.row_rest, restaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.row_rest, null);
            }

            Restaurant restaurant = restaurants.get(position);
            TextView restName = (TextView) convertView.findViewById(R.id.row_restname);
            //TextView restFood = (TextView) convertView.findViewById(R.id.row_restfood);
            TextView restAddress = (TextView) convertView.findViewById(R.id.row_restaddress);

            restName.setText(restaurant.getName());
            //restFood.setText(restaurant.getFood());
            restAddress.setText(restaurant.getAddress());
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }
}



