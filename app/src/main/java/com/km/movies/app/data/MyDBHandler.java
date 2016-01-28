package com.km.movies.app.data;

/**
 * Created by lina on 1/15/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.km.movies.app.MovieGetJson;

import java.io.UnsupportedEncodingException;

public class MyDBHandler extends SQLiteOpenHelper {

     private static final int DATABASE_VERSION = 3;
     private static final String DATABASE_NAME = "favoriteMoviess.db";
    public static final String LOG_TAG = MyDBHandler.class.getSimpleName();

    MovieGetJson m;

     //We need to pass database information along to superclass
            /* public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
         super(context, DATABASE_NAME, factory, DATABASE_VERSION);
         }*/
     public MyDBHandler(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
     }

     @Override
     public void onCreate(SQLiteDatabase db) {
         String query = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_MOVIES + "(" +
                 MovieContract.MovieEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                 MovieContract.MovieEntry.COLUMN_IMG + " TEXT, " +
                 MovieContract.MovieEntry.COLUMN_MOVIENAME + " TEXT, " +
                 MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                 MovieContract.MovieEntry.COLUMN_RDATE + " TEXT, " +
                 MovieContract.MovieEntry.COLUMN_VAVG + " TEXT " +
         ");";
         db.execSQL(query);
         }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_MOVIES);
         onCreate(db);
         }

     //Add a new row to the database
     public void addMovie(MovieGetJson movie){
         ContentValues values = new ContentValues();
         values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());

         String[]s=movie.getImg().split("http://image.tmdb.org/t/p/w342//");
        // Bitmap bitmap=StringToBitMap(s[1]);

     //    byte[] data=getBytes(bitmap);
         values.put(MovieContract.MovieEntry.COLUMN_IMG, movie.getImg());
         values.put(MovieContract.MovieEntry.COLUMN_MOVIENAME, movie.getTitle());
         values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
         values.put(MovieContract.MovieEntry.COLUMN_RDATE, movie.getrDate());
         values.put(MovieContract.MovieEntry.COLUMN_VAVG, movie.getvAvg());
         SQLiteDatabase db = getWritableDatabase();
         db.insert(MovieContract.MovieEntry.TABLE_MOVIES, null, values);
         db.close();
         }

     //Delete a product from the database
          /*  public void deleteMovie(int movieID){
         SQLiteDatabase db = getWritableDatabase();
         db.execSQL("DELETE FROM " + MovieContract.MovieEntry.TABLE_MOVIES + " WHERE " + MovieContract.MovieEntry.COLUMN_ID + "=\"" + movieID + "\";");
        }*/
     public Bitmap StringToBitMap(String encodedString){

             byte [] encodeByte= Base64.decode(encodedString.getBytes(), Base64.DEFAULT);
             Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
             return bitmap;

     }

    public MovieGetJson getContact(String id) throws UnsupportedEncodingException {

        MovieGetJson movie = new MovieGetJson();

            String query = "Select * FROM " + MovieContract.MovieEntry.TABLE_MOVIES+ " WHERE " + MovieContract.MovieEntry.COLUMN_ID + " =  \"" + id + "\"";

            SQLiteDatabase db = this.getReadableDatabase();

            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIM);

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {

                cursor.moveToFirst();

                movie.setId((cursor.getString(0)));
                movie.setImg(cursor.getString(1));
                movie.setTitle((cursor.getString(2)));
                movie.setOverview((cursor.getString(3)));
                movie.setrDate((cursor.getString(4)));
                movie.setvAvg((cursor.getString(5)));

                cursor.close();
            } else {

            }
        db.close();

        return movie;
    }



    public String databaseShow(){
        String dbString="";
         SQLiteDatabase db = getWritableDatabase();
         String query = "SELECT * FROM " + MovieContract.MovieEntry.TABLE_MOVIES + " WHERE 1";

         //Cursor points to a location in your results
         Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
         c.moveToFirst();

         //Position after the last row means the end of the results

         while (!c.isAfterLast()) {

             if (c.getString(c.getColumnIndex("img")) != null) {
                 c.getString(c.getColumnIndex("img"));
                 dbString+=c.getString(c.getColumnIndex("img"));
                 dbString += " ";

                 }
             c.moveToNext();
             }
         db.close();
         return dbString;
         }
    public String databaseShow1(){
        String dbString="";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + MovieContract.MovieEntry.TABLE_MOVIES + " WHERE 1";

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results

        while (!c.isAfterLast()) {

            if (c.getString(c.getColumnIndex("_id")) != null) {
                c.getString(c.getColumnIndex("_id"));
                dbString+=c.getString(c.getColumnIndex("_id"));
                dbString += " ";

            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
 
}
