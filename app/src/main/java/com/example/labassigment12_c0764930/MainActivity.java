package com.example.labassigment12_c0764930;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Variable declarations
    GoogleMap mMap;
    private final int REQUEST_CODE = 1;
    DatabaseHelperClass databaseHelper;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    LatLng customMarker;
    String address;
    LatLng currentLocation;
    final int RADIUS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        userLocation();

       Spinner spinner = (Spinner)findViewById(R.id.maptype);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        Toast.makeText(MainActivity.this, "Hybrid Map View", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        Toast.makeText(MainActivity.this, "Satellite Map View", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        Toast.makeText(MainActivity.this, "Terrain Map View", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        Toast.makeText(MainActivity.this, "Terrain Map View", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "No Map View ", Toast.LENGTH_SHORT).show();
                       break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!checkPermission()) {
            requestPermission();
        }
        else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        databaseHelper = new DatabaseHelperClass(this);
    }



    private void userLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(10);
        setHomeMarker();
    }

    private void setHomeMarker() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    currentLocation = userLocation;

                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(userLocation)
                            .zoom(15)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(userLocation)
                            .title("Your location").snippet("You are here "));

                }
            }
        };
    }

    private boolean checkPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setHomeMarker();
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                customMarker = latLng;
                setMarker(latLng);


                getAddress(customMarker);
            }
        });

    }





    private void setMarker(LatLng latLng){
        MarkerOptions options = new MarkerOptions().position(latLng).title(address)
                .snippet("Your Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(options);


    }
    private void getAddress(LatLng latLng){
        List<Address> addressList;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String dateTime=dateFormatter();
        try{
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
            if(!addressList.isEmpty()){
                address = addressList.get(0).getLocality() + " " + addressList.get(0).getAddressLine(0);
                System.out.println(addressList.get(0).getAddressLine(0));


                if (databaseHelper.savePlaces(addressList.get(0).getLocality(),addressList.get(0).getAddressLine(0),latLng.latitude,latLng.longitude,dateTime)){

                    Toast.makeText(MainActivity.this, "Address: "+addressList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(MainActivity.this, "Date: "+dateTime, Toast.LENGTH_SHORT).show();

                }
            }
        }catch (IOException e){
            e.printStackTrace();

        }

    }

    public String dateFormatter(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
    public void btnClick(View view) {


        Object[] objects = new Object[2];;
        String url;
        NearByPlaces getNearbyPlaceData = new NearByPlaces();

        switch (view.getId()) {
            case R.id.btn_restaurant:
                // get the url from place api
                url = getUrl(currentLocation.latitude, currentLocation.longitude,"restaurant");

                objects[0] = mMap;
                objects[1] = url;
                Log.d("", "btnClick: " + url);

                getNearbyPlaceData.execute(objects);
                Toast.makeText(this, "Restaurants", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_museum:
                url = getUrl(currentLocation.latitude, currentLocation.longitude, "museum");

                objects[0] = mMap;
                objects[1] = url;

                getNearbyPlaceData.execute(objects);
                Toast.makeText(this, "Museums", Toast.LENGTH_SHORT).show();
                break;



            case R.id.btn_direction:
                objects = new Object[4];
                url = getDirectionUrl();
                objects[0] = mMap;
                objects[1] = url;
                objects[2] = customMarker;
                objects[3] = new LatLng(currentLocation.latitude,currentLocation.longitude);
                GetDirections getDirectionsData = new GetDirections();

                getDirectionsData.execute(objects);
                break;



            case R.id.btn_cafe:

                url = getUrl(currentLocation.latitude, currentLocation.longitude, "cafe");
                objects = new Object[2];
                objects[0] = mMap;
                objects[1] = url;
                getNearbyPlaceData.execute(objects);
                Toast.makeText(this, "Cafe'", Toast.LENGTH_SHORT).show();
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                break;
            case R.id.btn_add_fav:
                Intent intent = new Intent(MainActivity.this,FavouritePlacesActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_clear:
                mMap.clear();
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            default:
                    break;





        }
    }


    private String getDirectionUrl() {
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        urlBuilder.append("origin="+currentLocation.latitude+","+currentLocation.longitude);
        urlBuilder.append("&destination="+customMarker.latitude+","+customMarker.longitude);
        urlBuilder.append("&key=AIzaSyBdKN4R296edvW6EnskkoeGUfW0uyNNea8");
        Log.d("", "getDirectionUrl: "+urlBuilder);
        return urlBuilder.toString();
    }


    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder placeUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        placeUrl.append("location="+latitude+","+longitude);
        placeUrl.append("&radius="+RADIUS);
        placeUrl.append("&type="+nearbyPlace);
        placeUrl.append("&key=AIzaSyBdKN4R296edvW6EnskkoeGUfW0uyNNea8");
        System.out.println(placeUrl.toString());
        return placeUrl.toString();
    }
}

