package com.example.android.quakereport;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {
private EarthquakeAdapter mAdapter;

    //Url for earthquake earthquake  data from USGS dataset //


    private static final String USGS_REQUEST_URL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes

        mAdapter= new EarthquakeAdapter(this, ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);
        EarthquakeAsyncTask task= new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeuri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW ,earthquakeuri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });
    }
    private class EarthquakeAsyncTask extends AsyncTask<String, void, List<Earthquake>>{


        protected List<Earthquake> doInBackground(String... urls){

            if(urls.length <1 || urls[0]== null)
            {
                return null;
            }
            List<Earthquake> result;
            result = QueryUtils.fetchEarthquakeData(urls[0]);

        }

        protected void onPostExecute(List<Earthquake> data){

            mAdapter.clear();

            if (data!=null && !data.isEmpty())
            {
                mAdapter.addAll(data);
            }
        }
    }
}