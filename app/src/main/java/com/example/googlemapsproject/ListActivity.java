package com.example.googlemapsproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

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
    }
}

