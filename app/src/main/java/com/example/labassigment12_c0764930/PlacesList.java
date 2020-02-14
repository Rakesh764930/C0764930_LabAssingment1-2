package com.example.labassigment12_c0764930;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlacesList extends AppCompatActivity {

    DatabaseHelperClass mDatabase;
    List<PlacesModel> listPlace;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_saved_places);
        listView = findViewById(R.id.saved_places_list);
        listPlace = new ArrayList<>();
        mDatabase = new DatabaseHelperClass(this);
        loadPlaces();


        AdaptorPlaces adaptorPlaces = new AdaptorPlaces(this,R.layout.layout_list_places,listPlace,mDatabase);
        listView.setAdapter(adaptorPlaces);

    }



    private void loadPlaces(){

        Cursor cursor = mDatabase.getPlaces();
        if(cursor.moveToFirst()){

            do{


                listPlace.add(new PlacesModel(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),cursor.getDouble(4)
                ));

            }while (cursor.moveToNext());

            cursor.close();
        }



    }

}
