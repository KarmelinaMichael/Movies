package com.km.movies.app;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageAdapter extends ArrayAdapter {

    private Context context;
    private String[] items;
    static int j=0;


    public ImageAdapter(Context context, int resource,  String[] items) {
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

        return items[position];

    }



    @Override

    public long getItemId(int position) {

        return position;

    }




    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView img = null;

        if (convertView == null) {
            img = new ImageView(context);
            convertView = img;

            //img.setPadding(0, 0, 0, 0);


        } else {

            img = (ImageView) convertView;

        }

        Picasso.with(context).load(items[position])
                .into(img);
        Picasso.with(this.context).setLoggingEnabled(true);


        return convertView;

    }
    /*  private Target target = new Target() {

     *  @Override
         public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
             new Thread(new Runnable() {

                 @Override
                 public void run() {

                     File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ j+".jpg");
                     try {
                         file.createNewFile();
                         FileOutputStream ostream = new FileOutputStream(file);
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                         ostream.close();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
             j++;
                 }
             }).start();
         }
 */
       /* @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }*/


    };



//}

