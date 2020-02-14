package com.example.labassigment12_c0764930;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;

public class GetDirections extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String direction_url;
    String url;
    LatLng latLng, userLatLng;
    String distance,duration;


    @Override
     protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng = (LatLng) objects[2];
        userLatLng = (LatLng) objects[3];
        UrlConnector URLConnector = new UrlConnector();
        try {
            direction_url = URLConnector.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return direction_url;
    }

    @Override
    protected void onPostExecute(String s) {


        HashMap<String, String> dist_map = null;
        JSONDataParser distancesParser = new JSONDataParser();
        dist_map = distancesParser.parseDistance(s);

        distance = dist_map.get("distance");
        duration = dist_map.get("duration");
        Log.d("Main", "onPostExecute: " + distance +"..." + duration);
        mMap.clear();
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Duration : " + duration)
                .snippet("Distance : " + distance);
        mMap.addMarker(options);

        MarkerOptions options1 = new MarkerOptions()
                .position(userLatLng)
                .draggable(false)
                .title("Duration : " + duration).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet("Distance : " + distance);
        mMap.addMarker(options1);

        String[] directionsList;
        JSONDataParser parser = new JSONDataParser();
        directionsList = parser.parseDirections(s);
        Log.d("", "onPostExecute: " + directionsList);
        drawRoute(directionsList);

    }

    private void drawRoute(String[] directionsList) {
        int count = directionsList.length;
        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions()
                    .color(Color.BLUE)
                    .width(15)
                    .addAll(PolyUtil.decode(directionsList[i]));
            mMap.addPolyline(options);
        }
    }

}
