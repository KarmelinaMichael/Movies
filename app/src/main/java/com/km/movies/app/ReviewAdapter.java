package com.km.movies.app;

/**
 * Created by lina on 1/26/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewAdapter extends ArrayAdapter<String> {

    ArrayList<String> mIdMap = new ArrayList<>();

    public ReviewAdapter(Context context, int textViewResourceId,
                       ArrayList<String> objects) {
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

        sText.setText(getItem(position));
        sImage.setImageResource(R.mipmap.ic_launcher);
        return customView;
    }
    @Override
    public String getItem(int position) {

        String item = mIdMap.get(position);
        return item;
    }



}


