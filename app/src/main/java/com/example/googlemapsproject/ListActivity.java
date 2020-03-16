package com.example.googlemapsproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    ArrayList<String> events;
    ArrayAdapter<String> eventsAdapter;
    ListView lvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        events = new ArrayList<>();

        events.add("Bar 1");
        events.add("Bar 2");
        events.add("Bar 3");
        events.add("Bar 4");
        events.add("Happy Hour start");
        events.add("Happy Hour end");
        events.add("Next time frame");
        events.add("Bar 5");
        events.add("Bar 6");
        events.add("New special event");
        events.add("Bar 1 closes");
        events.add("advertisement");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");
        events.add("dummy data");

        eventsAdapter = new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, events);
        lvEvents = findViewById(R.id.lvEvents);
        lvEvents.setAdapter(eventsAdapter);

        if (isServicesOK()) {
            init();
        }
    }

    private void init() {
        Button btnMap = findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, MapActivity.class));
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ListActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            // everything is fine and user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ListActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}

