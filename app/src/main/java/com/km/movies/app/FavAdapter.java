package com.km.movies.app;

/**
 * Created by lina on 1/27/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class FavAdapter extends ArrayAdapter {

    private Context context;
    private String[] items;
    private MovieGetJson[] movieGetJson=MovieFragment.movieGetJson;
    ImageView img;
    int position;


    public FavAdapter(Context context, int resource,  String[] items) {
        super(context, resource);
        this.context=context;
        this.items = items;
    }


    @Override

    public int getCount() {

        return items.length;

    }





    @Override

    public Object getItem(int position) {
this.position=position;
        return items[position];

    }



    @Override

    public long getItemId(int position) {
        this.position=position;
        return position;

    }




    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        this.position=position;

        if (convertView == null) {
            img = new ImageView(context);
            convertView = img;

            //img.setPadding(0, 0, 0, 0);


        } else {

            img = (ImageView) convertView;

        }

//img.setImageURI(items[position]);
      //  Bitmap bitmap=StringToBitMap(items[position]);
        //String[]s=items[position].split("http://image.tmdb.org/t/p/w342//");
      // img.setImageURI(Uri.parse(items[position]));


      /*  Picasso.with(context).load(items[position])
                .into(img);
        Picasso.with(this.context).setLoggingEnabled(true);*/
       loadImageFromStorage(items[position]);
        return convertView;

    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path,movieGetJson[position].getId() +".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            img.setImageBitmap(Bitmap.createScaledBitmap(b,342, 513, false));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }





}