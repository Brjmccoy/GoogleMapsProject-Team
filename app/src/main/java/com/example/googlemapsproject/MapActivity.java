package com.example.googlemapsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.*;

//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {





    private static String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15f;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int MAX_BARS = 1000;


    // widgets
    private EditText mSearchText;
    private ImageView mGps;


    // vars
    private boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // list that will contain all bars in database with all of their info
    //private static final Bar [] databaseBars = new Bar [MAX_BARS];
    private static final ArrayList<Bar> databaseBars = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = (EditText)findViewById(R.id.input_search);
        mGps = (ImageView)findViewById(R.id.ic_gps);

        getLocationPermission();

        readBarDataFromDatabase();

    }

    private void init() {
        Log.d(TAG,"init: initializing...");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textview, int actionID, KeyEvent keyevent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyevent.getAction() == KeyEvent.ACTION_DOWN
                        || keyevent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // execute method for searching
                    geoLocate();


                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }


    public void readBarDataFromDatabase() {
        Log.d(TAG, "readBarDataFromDatabase: geolocating");

        // make reference to Firebase database (Real-time database)
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG,"readBarDataFromDatabase: database reference made");

        Query query = myRef;

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // make reference to child in database
                DataSnapshot barSnapShot = dataSnapshot.child("Bar");
                Log.d(TAG,"readBarDataFromDatabase: data snapshot for Bar acquired");

                // get children of child referenced
                Iterable<DataSnapshot> barChildren = barSnapShot.getChildren();
                Log.d(TAG,"readBarDataFromDatabase: children of Bar snapshot acquired");

                // traverse through each child of "Bar"
                for (DataSnapshot barChild : barChildren) {

                    // bar to be populated with info and added to databaseBars
                    Bar databaseBar = new Bar();

                    // get name of child (bar name)
                    String barName = barChild.getKey();
                    Log.d(TAG,"readBarDataFromDatabase: bar name: " + barName);

                    databaseBar.setBarName(barName);

                    // make reference to child under bar name
                    DataSnapshot barChildSnapshot = dataSnapshot.child("Bar").child(barName);
                    Log.d(TAG,"readBarDataFromDatabase: snapshot acquired for " + barName);

                    // get children of bar name referenced
                    Iterable<DataSnapshot> barNameChildren = barChildSnapshot.getChildren();
                    Log.d(TAG,"readBarDataFromDatabase: children acquired for " + barName);

                    // traverse through each child/key of bar name
                    for (DataSnapshot barChildSS : barNameChildren) {

                        // get info of bar name
                        String barKey = barChildSS.getKey();
                        Object barKeyVal = barChildSS.getValue();
                        Log.d(TAG,"readBarDataFromDatabase: bar name: " + barName + ", barKey: " + barKey + ", value: " + barKeyVal);

                        // add values to bar object databaseBar
                        if (barKey.equals("Address")) {
                            databaseBar.setAddress(barKeyVal + "");
                        }
                        if (barKey.equals("BarCode")) {
                            databaseBar.setBarCode(barKeyVal + "");
                        }
                        if (barKey.equals("Genre")) {
                            databaseBar.setGenre(barKeyVal + "");
                        }
                        if (barKey.equals("HappyHours")) {
                            // make reference to happy hour child under bar name
                            DataSnapshot barChildHappyHourSnapshot = dataSnapshot.child("Bar").child(barName).child("HappyHours");
                            Log.d(TAG,"readBarDataFromDatabase: happy hours snapshot acquired for " + barName);

                            // get children of HappyHours
                            Iterable<DataSnapshot> happyHours = barChildHappyHourSnapshot.getChildren();
                            Log.d(TAG,"readBarDataFromDatabase: happy hours acquired for " + barName);

                            // traverse through happy hours of bar
                            for (DataSnapshot happyHour : happyHours) {
                                // get info of happy hours
                                String happyHourDay = happyHour.getKey();
                                Object happyHourTime = happyHour.getValue();

                                // add happy hours to bar object databaseBar
                                if (happyHourDay.equals("Monday")) {
                                    databaseBar.setMondayHappyHours(happyHourTime + "");
                                }
                                if (happyHourDay.equals("Tuesday")) {
                                    databaseBar.setTuesdayHappyHours(happyHourTime + "");
                                }
                                if (happyHourDay.equals("Wednesday")) {
                                    databaseBar.setWednesdayHappyHours(happyHourTime + "");
                                }
                                if (happyHourDay.equals("Thursday")) {
                                    databaseBar.setThursdayHappyHours(happyHourTime + "");
                                }
                                if (happyHourDay.equals("Friday")) {
                                    databaseBar.setFridayHappyHours(happyHourTime + "");
                                }
                                if (happyHourDay.equals("Saturday")) {
                                    databaseBar.setSaturdayHappyHours(happyHourTime + "");
                                }
                                if (happyHourDay.equals("Sunday")) {
                                    databaseBar.setSundayHappyHours(happyHourTime + "");
                                }
                            }
                        }
                        if (barKey.equals("Website")) {
                            databaseBar.setWebsite(barKeyVal + "");
                        }
                    }

                    // THIS ISNT REALLY ASSIGNING
                    databaseBars.add(databaseBar);

                }

                // This IS printing bar content
                for (Bar bar : databaseBars) {
                    Log.d(TAG, "readBarDataFromDatabase: INSIDE anonymous class: " + bar.toString());
                }










            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        query.addValueEventListener(valueEventListener);


        // This is NOT printing bar content
        for (Bar bar : databaseBars) {
            Log.d(TAG, "readBarDataFromDatabase: OUTSIDE anonymous class: " + bar.toString());
        }
    }


    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }
        catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();


            // testing upload
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Test");
            myRef.push().setValue("worked");

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }


    /**
     *  task.getResult() IS NULL, FIX!!
     */
    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: getting current location of device.");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }
                        else {
                            Log.d(TAG, "onComplete: current location is null.");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Security exception: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions().position(latlng).title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready.");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map.");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions.");
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            }
            else {
                ActivityCompat.requestPermissions(MapActivity.this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(MapActivity.this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }


    /**
     * onRequestPermissionsResult IS NOT BEING CALLED, FIX!!
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permissions, @NonNull int [] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission(s) failed.");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission(s) granted.");
                    mLocationPermissionsGranted = true;
                    // initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }





}
