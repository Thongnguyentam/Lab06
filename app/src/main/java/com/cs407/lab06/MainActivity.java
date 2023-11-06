package com.cs407.lab06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {
    //Bascom Hall
    private final LatLng mDestinationaLatLng = new LatLng(43.07588277196975, -89.40396348820286);
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);

        // Use .getMapAsync(..) to be notified when the MapFragment is
        // ready, and then create your markers
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap; // Save GoogleMap Object

            // Display a Marker
            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationaLatLng)
                    .title("Destination"));
        });
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        // Display the user’s location by querying the FusedLocationProviderClient
        displayMyLocation();
    }

    private void displayMyLocation() {
        // Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        // If not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // if permission granted, display marker at current location
        else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();

                if (task.isSuccessful() && mLastKnownLocation != null){
                    // Show the polyline between the obtained location and the marker created in the last
                    //milestone.
                    Log.i("Information",  mLastKnownLocation.getLatitude()+ " " +mLastKnownLocation.getLongitude());
                    // add marker for the last known location
                    LatLng mDestinationaLatLng2 = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(mDestinationaLatLng2)
                            .title("Start Point"));
                    mMap.addPolyline(new PolylineOptions().add(mDestinationaLatLng2, mDestinationaLatLng));
                }
                else{
                    Log.i("Information", "null last know location");
                }
            });
        }
    }

    /*
    * Handles the result ò the request for location permissions
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                displayMyLocation();
            }
        }
    }
}