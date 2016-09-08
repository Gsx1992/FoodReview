package ie.wit.gareth.foodreviewapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ie.wit.gareth.foodreviewapp.models.Review;
import ie.wit.gareth.foodreviewapp.rest.DeleteRest;
import ie.wit.gareth.foodreviewapp.rest.GetRest;


public class FragmentActivity extends ActionBarActivity implements ActionBar.TabListener {


    public  static ArrayList<Review> reviews = new ArrayList<Review>();
    public  static ListView listView;
    public static ReviewAdapter radapter;
    public List<String> results = new ArrayList<String>();
    public static String rest_id;
    public static String name;
    public static String food;
    public static String address;
    public static LatLng latLng;
    public static String is_admin;


    SectionsPagerAdapter mSectionsPagerAdapter;


    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        rest_id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        setTitle(name);
        food = intent.getStringExtra("food");
        address = intent.getStringExtra("address");
        latLng = new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0));
        SharedPreferences sharedPreferences = this.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        is_admin = sharedPreferences.getString("admin", null);
        new RestaurantGetAsync().execute();


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_review) {
            Intent i = new Intent(this, CreateReviewActivity.class);
            i.putExtra("id", rest_id);
            i.putExtra("name", name);
            startActivity(i);
            reviews.clear();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return FirstFragment.newInstance(position+ 1);
            }

            else{
                return SecondFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Restaurant";
                case 1:
                    return "Reviews";

            }
            return null;
        }
    }


    public static class FirstFragment extends Fragment implements View.OnClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static FirstFragment newInstance(int sectionNumber) {
            FirstFragment fragment = new FirstFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public FirstFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_test, container, false);
            TextView restName = (TextView) rootView.findViewById(R.id.rest_name);
            TextView restFood = (TextView) rootView.findViewById(R.id.restFood);
            TextView restAddress = (TextView) rootView.findViewById(R.id.restAddress);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageFood);

            if(food.equalsIgnoreCase("Chinese Food")){
                imageView.setImageResource(R.drawable.chinesefood);
            }

            else if(food.equalsIgnoreCase("Italian Food")){
                imageView.setImageResource(R.drawable.italian);
            }

            else if(food.equalsIgnoreCase("Indian Food")){
                imageView.setImageResource(R.drawable.indianfood);
            }

            else if(food.equalsIgnoreCase("Fast Food")){
                imageView.setImageResource(R.drawable.fastfood);
            }

            else if(food.equalsIgnoreCase("Sea Food")){
                imageView.setImageResource(R.drawable.seafood);
            }

            else{
                imageView.setImageResource(R.drawable.barfood);
            }
            Button mapButton = (Button) rootView.findViewById(R.id.mapButton);
            mapButton.setOnClickListener(this);

            restName.setText(name);
            restFood.setText("Food type: "+food);
            restAddress.setText("Address: "+address);

            return rootView;
        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.mapButton:
                    Intent i = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                    i.putExtra("lat", latLng.latitude);
                    i.putExtra("lng", latLng.longitude);
                    i.putExtra("name", name);
                    startActivity(i);
                    reviews.clear();
            }

        }
    }

    public  static class SecondFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static SecondFragment newInstance(int sectionNumber) {
            SecondFragment fragment = new SecondFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SecondFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment2_test, container, false);
            listView = (ListView) rootView.findViewById(R.id.listView2);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if(is_admin != null){
                        final int pos = position;
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("Delete?");
                        alert.setMessage("This cant be undone.");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ReviewDeleteAsync().execute(reviews.get(pos).getId());
                                Toast.makeText(getActivity(), "Review Deleted!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                                getActivity().finish();
                                reviews.clear();
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

            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        reviews.clear();
        super.onBackPressed();
    }

    private  class RestaurantGetAsync extends AsyncTask<Void, Void, List<String>> {


        @Override
        protected List<String> doInBackground(Void... params) {

            GetRest getRest = new GetRest();
            results.add(getRest.getRequest("restaurants/"+rest_id));
            results.add(getRest.getRequest("restreview/"+rest_id));
            return results;

        }

        @Override
        protected void onPostExecute(List<String> data) {
            super.onPostExecute(data);
            try {
                String restaurantReview = data.get(1);

                JSONArray array = new JSONArray(restaurantReview);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    Review r = new Review(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getString("date"), jsonObject.getString("details"), jsonObject.getString("comeback"));
                    reviews.add(r);
                }

                radapter = new ReviewAdapter(getApplicationContext(), reviews);
                listView.setAdapter(radapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public class ReviewAdapter extends ArrayAdapter<Review> {
        public ReviewAdapter(Context context, List<Review> reviews) {
            super(context, R.layout.row_rest, reviews);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.row_rest, null);
            }

            Review review = reviews.get(position);
            TextView reviewTitle = (TextView) convertView.findViewById(R.id.row_restname);
            //TextView restFood = (TextView) convertView.findViewById(R.id.row_restfood);
            TextView reviewDetail = (TextView) convertView.findViewById(R.id.row_restaddress);
            ImageView restRate = (ImageView) convertView.findViewById(R.id.row_restRate);
            if(review.getComeback().equalsIgnoreCase("yes")){
                restRate.setImageResource(R.drawable.ic_emoticon_happy_black_36dp);
                restRate.setVisibility(View.VISIBLE);
            }
            else if(review.getComeback().equalsIgnoreCase("no")){
                restRate.setImageResource(R.drawable.ic_emoticon_sad_black_36dp);
                restRate.setVisibility(View.VISIBLE);
            }
            reviewTitle.setText(review.getTitle());
            reviewDetail.setText(review.getDetails());
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private static class ReviewDeleteAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            DeleteRest deleteRest = new DeleteRest();
            try {
                return deleteRest.deleteRequest("reviews/"+params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "An Error has occured";

        }

        @Override
        protected void onPostExecute(String data) {
            Log.d("LOOK AT ME", data);
            super.onPostExecute(data);




        }
    }

}
