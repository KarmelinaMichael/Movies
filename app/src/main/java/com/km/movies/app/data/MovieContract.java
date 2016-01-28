package com.km.movies.app.data;

/**
 * Created by lina on 1/19/2016.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract{

    public static final String CONTENT_AUTHORITY = "com.km.movies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieEntry implements BaseColumns{
   /*     // table name
        public static final String TABLE_FLAVORS = "flavor";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VERSION_NAME = "version_name";*/
   public static final String TABLE_MOVIES = "movies";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_MOVIENAME = "title";
        public static final String COLUMN_ID="_id";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_RDATE="rDate";
        public static final String COLUMN_VAVG="vAvg";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        // for building URIs on insertion
        public static Uri buildFlavorsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}