package com.km.movies.app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lina on 1/22/2016.
 */
public class ListAdapter extends ArrayAdapter<Uri> {

    ArrayList<Uri> mIdMap = new ArrayList<>();

    public ListAdapter(Context context, int textViewResourceId,
                              ArrayList<Uri> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.add(objects.get(i));
        }
    }
    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

         View customView = inflater.inflate(R.layout.custom_row, parent, false);


         TextView sText = (TextView) customView.findViewById(R.id.ListText);
         ImageView sImage = (ImageView) customView.findViewById(R.id.ListImage);
        int i=position+1;

         sText.setText("Trailer " + i);
         sImage.setImageResource(R.drawable.icon_xll);
         return customView;
        }
    @Override
    public Uri getItem(int position) {

        Uri item = mIdMap.get(position);
        return item;
    }



}


