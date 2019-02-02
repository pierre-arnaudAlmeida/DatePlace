package fr.blackmamba.dateplaceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_view_layout);
        ListView list = (ListView) findViewById(R.id.theList);
        Log.d(TAG, "onCreate: Started.");


        ville gogo = new ville("gogo","08-03-1987","Male");
        ville steve = new ville("Steve","08-03-1987","Male");
        ville stacy = new ville("Stacy","11-15-2000","Female");
        ville ashley = new ville("Ashley","07-02-1999","Female");
        ville matt = new ville("Matt","03-29-2001","Male");

        //Add the ville objects to an ArrayList
        ArrayList<ville> peopleList = new ArrayList<>();
        peopleList.add(gogo);
        peopleList.add(steve);
        peopleList.add(stacy);
        peopleList.add(ashley);
        peopleList.add(matt);

        villeListAdapter adapter = new villeListAdapter(this, R.layout.adapter_view_layout, peopleList);
        list.setAdapter(adapter);







    }

}
