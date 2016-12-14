package com.a21210.walkthewalk;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Geocoder geocoder;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Double latitude;
    Double longitude;
    String str_latitude = "";
    String str_longitude = "";
    String bestProvider;
    List<Address> user = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //for getting location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location == null){
            Toast.makeText(this,"Location Not found",Toast.LENGTH_LONG).show();
        }else{
            geocoder = new Geocoder(this);
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                latitude=(double)user.get(0).getLatitude();
                longitude=(double)user.get(0).getLongitude();
                System.out.println(" DDD lat: " +latitude+",  longitude: "+longitude);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }




        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                latitude = location.getLatitude();
                longitude = location.getLongitude();

                str_latitude = String.valueOf(latitude);
                str_longitude = String.valueOf(longitude);

                Log.d("lat_lo ng: ", str_latitude + " " + str_longitude);Log.d("lat_lo ng: ", str_latitude + " " + str_longitude);






            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {


            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);


            }
        };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);


                return;
            }
        }
        else
        {
            locationManager.requestLocationUpdates("gps", 2000, 5, locationListener);

            //Toast.makeText(this, lati + " " + longe, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case 10:
                if(grantResults.length>0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();
                return;
        }
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

        // Add a marker in Sydney and move the camera
        LatLng quarteira = new LatLng(37.0722649, -8.1080549);
        mMap.addMarker(new MarkerOptions().position(quarteira).title("Home Sweet home!"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(quarteira));
        //zoom para localização
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quarteira, 16));

        //Toast.makeText(this, "Latitude: " + str_latitude + "\nLongitude: " + str_longitude, Toast.LENGTH_SHORT).show();


    }
}
