package com.example.kentversion1.jamesjrh52.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import android.view.View.OnClickListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Button btn;


    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //final TextView tvData = (TextView)findViewById(R.id.tvJsonItem);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        setContentView(R.layout.james_layout);
        //btn = (Button) findViewById(R.id.button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng senateHouse = new LatLng(51.297278, 1.069744);
        float zoom = 15;
        mMap.addMarker(new MarkerOptions().position(senateHouse).title("Marker in Senate House")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(senateHouse, zoom));

        //instantiate and execute the AsyncTask
        addFriendMarkers mapAsync = new addFriendMarkers();
        mapAsync.execute();

    }

    /*
     * code inspired and modified from
     * http://stackoverflow.com/questions/29724192/using-json-for-android-maps-api-markers-not-showing-up
     */

    private class addFriendMarkers extends AsyncTask<Void, Void, String> {

        //tried making a progress bar for the AsyncTask
        //but couldn't get it working, so have commented it out

        Context context;
        ProgressDialog progressDialog;

        /*
        addFriendMarkers(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Finding friends...");
            progressDialog.setMax(10);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            int progress = values[0];
            progressDialog.setProgress(progress);
        }
        */
        private static final String kentWebAd = "https://www.cs.kent.ac.uk/people/staff/iau/LocalUsers.php";

        // Invoked by execute() method
        @Override
        protected String doInBackground(Void... args) {

            HttpURLConnection connection = null;
            final StringBuilder jsonFullString = new StringBuilder();
            try {
                // Connect to Kent URL
                URL KentURL = new URL(kentWebAd);
                connection = (HttpURLConnection) KentURL.openConnection();
                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());

                // put JSON into a StringBuilder
                //int testing = 0;
                int x;
                char[] buffer = new char[1024];
                while ((x = inputStream.read(buffer)) != -1) {
                    jsonFullString.append(buffer, 0, x);
                    //testing++;
                    //publishProgress(testing);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            //turn into a String
            return jsonFullString.toString();
        }

        // Execute after doInBackground() method
        @Override
        protected void onPostExecute(String jsonFullString) {

            try {
                //get JSON object from String
                JSONObject jOb = new JSONObject(jsonFullString);
                //get JSON array from JSON object
                JSONArray jFriendArray = jOb.getJSONArray("Users");

                //loop through objects in array
                for(int it = 0; it < jFriendArray.length(); it++){

                    JSONObject friendObj = jFriendArray.getJSONObject(it);

                    //get values from JSON object in the array
                    double lat = friendObj.getDouble("lat");
                    double lon = friendObj.getDouble("lon");
                    String name = friendObj.getString("name");

                    // makes a location for the new marker from friendObj
                    LatLng latLon = new LatLng(lat,lon);

                    // add the new marker on the map for each friend
                    mMap.addMarker(new MarkerOptions()

                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title(name)
                            .position(latLon));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}



















