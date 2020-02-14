package com.example.labassigment12_c0764930;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdaptorPlaces extends ArrayAdapter {

    Context mcontext;
    int layoutRes;
    DatabaseHelperClass mDatabase;
    List<PlacesModel> listPlace;


    public AdaptorPlaces(@NonNull Context mcontext, int layoutRes, List<PlacesModel> listPlace, DatabaseHelperClass mDatabase) {
        super(mcontext, layoutRes,listPlace);
        this.mcontext = mcontext;
        this.layoutRes = layoutRes;
        this.listPlace = listPlace;
        this.mDatabase = mDatabase;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(layoutRes,null);
        TextView tvname = view.findViewById(R.id.txt_name);
        TextView tvaddress = view.findViewById(R.id.txt_address);
        TextView tvdate = view.findViewById(R.id.txt_date);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());






        final PlacesModel list = listPlace.get(position);
        tvname.setText(list.getDate());
        tvaddress.setText(list.getAddress());
        tvdate.setText(date);



        return view;

    }


}
