package com.example.labassigment12_c0764930;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;



public class PlacesList extends AppCompatActivity {

    DatabaseHelperClass mDatabase;
    List<PlacesModel> listPlace;
    SwipeMenuListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_saved_places);
        listView = (SwipeMenuListView )findViewById(R.id.saved_places_list);
        listPlace = new ArrayList<>();
        mDatabase = new DatabaseHelperClass(this);
        loadPlaces();

        final AdaptorPlaces placesAdaptor = new AdaptorPlaces(this,R.layout.layout_list_places,listPlace,mDatabase);
        listView.setAdapter(placesAdaptor);

        //https://github.com/baoyongzhang/SwipeMenuListView
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {


                SwipeMenuItem item = new SwipeMenuItem(
                        getApplicationContext());
                item.setTitle("Delete");

                item.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));

                item.setWidth(280);
                item.setTitleSize(20);
                item.setTitleColor(Color.WHITE);

                menu.addMenuItem(item);

                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());

                item1.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x66,
                        0xff)));

                item1.setWidth(280);

                item1.setTitle("Update");

                item1.setTitleSize(20);


                item1.setTitleColor(Color.WHITE);

                menu.addMenuItem(item1);


            }
        };


        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {

                    case 0:

                        if (mDatabase.deletePlaces(listPlace.get(position).id)) {

                            listPlace.remove(position);
                            loadPlaces();
                            listView.setAdapter(placesAdaptor);

                            Toast.makeText(PlacesList.this, "delete", Toast.LENGTH_SHORT).show();
                            loadPlaces();


                        } else {

                            Toast.makeText(PlacesList.this, "not deleted", Toast.LENGTH_SHORT).show();


                        }
                        loadPlaces();


                        break;

                    case 1:
                        Intent intent = new Intent(PlacesList.this, MainActivity.class);
                        intent.putExtra("id", listPlace.get(position).id);
                        intent.putExtra("lat", listPlace.get(position).latitude);
                        intent.putExtra("lng", listPlace.get(position).longitude);
                        intent.putExtra("edit", true);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });


    }




    private void loadPlaces(){

        Cursor cursor = mDatabase.getPlaces();
        if(cursor.moveToFirst()){

            do{


                listPlace.add(new PlacesModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),cursor.getDouble(5)
                ));

            }while (cursor.moveToNext());

            cursor.close();
        }



    }

}
