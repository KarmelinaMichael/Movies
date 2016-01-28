package com.km.movies.app;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.km.movies.app.data.MyDBHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static  String MOVIE_SHARE_HASHTAG ;

    private ShareActionProvider mShareActionProvider;
    private String mMovie;
    private String id;
    private String[] key;
    private String[] content;
    private String MOVIE_BASE_URL ="https://www.youtube.com/watch?v=";
    private String title;
    private String img;
    private String overview;
    private ExpandableHeightListView listView;
    private ExpandableHeightListView listView1;
    private String rDate;
    private String vAvg;
    private ListAdapter adapter;
    private ReviewAdapter adapterR;
    private FetchTask movieTask ;
    private FetchTask1 revTask ;
    private int position;
    private ArrayList<Uri> list;
    private ArrayList<String> list1;
    private ListView listview;
    private View rootView;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private ImageView imgV;
    private Intent shareIntent  = new Intent(Intent.ACTION_SEND);;
    private MovieGetJson[] movieGetJson=new MovieGetJson[1000];
    private MyDBHandler dbHandler;
    MovieGetJson m;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent=getActivity().getIntent();
        listView = new ExpandableHeightListView(getContext());
        listView1 = new ExpandableHeightListView(getContext());

         listView =((ExpandableHeightListView) (rootView.findViewById(R.id.videos)));
        listView1 =((ExpandableHeightListView) (rootView.findViewById(R.id.reviews)));
        dbHandler = new MyDBHandler(getContext());
        t1=((TextView) rootView.findViewById(R.id.detail_title));
        imgV=((ImageView) rootView.findViewById(R.id.detail_img));
        t2=((TextView) rootView.findViewById(R.id.detail_overview));
        t3=((TextView) rootView.findViewById(R.id.detail_rDate));
        t4=((TextView) rootView.findViewById(R.id.detail_vAvg));

        if (intent != null) {
        movieGetJson=MovieFragment.movieGetJson;
            id= intent.getStringExtra("id");
            if (id==null){

               id=MainActivity.args.getString("id");
                title=MainActivity.args.getString("title");
                img=MainActivity.args.getString("img");
                overview=MainActivity.args.getString("overview");
                rDate=MainActivity.args.getString("rDate");
                vAvg =MainActivity.args.getString("vAvg");

            m=new MovieGetJson(title,img,overview,rDate,vAvg,id);

            }
            else {
                intent=MainActivity.intent;
                title = intent.getStringExtra("Title");
                img = intent.getStringExtra("Img");
                overview = intent.getStringExtra("Overview");
                rDate = intent.getStringExtra("rDate");
                vAvg = intent.getStringExtra("vAvg");
                id = intent.getStringExtra("id");
            }

            t1.setText(title);
            t2.setText(overview);
            t3.setText(rDate);
            t4.setText(vAvg);
            Picasso.with(getContext()).load(img)

                    .into(imgV);


             String MovieJsonStr = "http://api.themoviedb.org/3/movie/"+id+"/videos";
             String MovieJsonR = "http://api.themoviedb.org/3/movie/"+id+"/reviews";
            movieTask=new FetchTask(getActivity());
            movieTask.execute(MovieJsonStr);
            revTask=new FetchTask1(getActivity());
            revTask.execute(MovieJsonR);

            Button button=(Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click ��
                    try {
                        String[] s=img.split("http://image.tmdb.org/t/p/w342/");
                        imgV.buildDrawingCache();
                        Bitmap b = imgV.getDrawingCache();

                       String bitmap= saveToInternalSorage(b,id);
                        MovieGetJson movie = new MovieGetJson(title, bitmap, overview, rDate, vAvg, id);
                        dbHandler.addMovie(movie);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                }
            });


          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            playMedia(adapter.getItem(position));

              }

          });


    }
        return rootView;
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

    private String saveToInternalSorage(Bitmap bitmapImage,String s) throws IOException {
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,s+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.

            mShareActionProvider.setShareIntent(createShareForecastIntent());

    }
    private Intent createShareForecastIntent() {

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
       // shareIntent.putExtra(Intent.EXTRA_TEXT, MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }



    class FetchTask extends AsyncTask<String, Integer, String[]> implements com.km.movies.app.FetchTask {
        Activity activity;

        public FetchTask(Activity activity) {
            this.activity = activity;

        }

        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.

        private final String LOG_TAG = FetchTask.class.getSimpleName();




        public String[] getWeatherDataFromJson(String MovieJson)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String M_ID="key";
            final String M_LIST = "results";



            JSONObject movieJson = new JSONObject(MovieJson);

            JSONArray movieArray = movieJson.getJSONArray(M_LIST);
            JSONObject movie ;
            key=new String [movieArray.length()];

            for(int i=0;i<movieArray.length();i++) {
                movie=movieArray.getJSONObject(i);
                key[i] = movie.getString(M_ID);
            }

        if(key!=null&&key.length>0){
            MOVIE_SHARE_HASHTAG=MOVIE_BASE_URL+key[0];
            shareIntent.putExtra(Intent.EXTRA_TEXT, MOVIE_SHARE_HASHTAG);}
            return key;

        }

        @Override
        protected String[] doInBackground(String... params) {
//            if (params.length == 0) {
//                return null;
//            }


            try {
                // Construct the URL for the themoviedb query
                // Possible parameters are available at themoviedb API page, at



                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(params[0]).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.Movie_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                int j = 0;

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
                    return null;
                }
                try {
                    return getWeatherDataFromJson(buffer.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                Log.v(LOG_TAG, "Movie JSON string: " +buffer.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

       @Override
        protected void onPostExecute(String[] result) {
            list = new ArrayList<>();
           if(result!=null){

            for(int i=0;i<result.length;i++) {
               Uri builtUri = Uri.parse(MOVIE_BASE_URL + result[i]).buildUpon().build();

               list.add(builtUri);


           }



            adapter = new ListAdapter(getContext(),R.layout.custom_row,list);

           listview=listView;

           listView.setAdapter(adapter);
           listView.setExpanded(true);

            }
        }

        @Override
        public void onPostExecute(String[] result, String s) {
            list1 = new ArrayList<>();

            if(result!=null){
                for(int i=0;i<result.length;i++) {

                    list1.add(result[i]);


                }

                adapterR = new ReviewAdapter(getContext(),R.layout.custom_row,list1);
                listView1.setAdapter(adapterR);
                listView1.setExpanded(true);

            }
            else  {



            }

        }

    }



    class FetchTask1 extends AsyncTask<String, Integer, String[]>  {
        Activity activity;

        public FetchTask1(Activity activity) {
            this.activity = activity;

        }

        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.

        private final String LOG_TAG = FetchTask.class.getSimpleName();




        public String[] getWeatherDataFromJson(String MovieJson)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String M_ID="content";
            final String M_LIST = "results";



            JSONObject movieJson = new JSONObject(MovieJson);

            JSONArray movieArray = movieJson.getJSONArray(M_LIST);
            JSONObject movie ;
            content=new String [movieArray.length()];
            for(int i=0;i<movieArray.length();i++) {
                movie=movieArray.getJSONObject(i);
                content[i] = movie.getString(M_ID);
            }
            return content;

        }

        @Override
        protected String[] doInBackground(String... params) {
//            if (params.length == 0) {
//                return null;
//            }


            try {
                // Construct the URL for the themoviedb query
                // Possible parameters are available at themoviedb API page, at



                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(params[0]).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.Movie_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                int j = 0;

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                try {
                    return getWeatherDataFromJson(buffer.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                Log.v(LOG_TAG, "Movie JSON string: " +buffer.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String[] result) {
            list1 = new ArrayList<>();

            if(result!=null){
                for(int i=0;i<result.length;i++) {

                    list1.add(result[i]);


                }



                adapterR = new ReviewAdapter(getContext(),R.layout.custom_row,list1);

                listView1.setAdapter(adapterR);
                listView1.setExpanded(true);
                listView1.setClickable(false);

            }
            else  {




            }

        }

    }

    public void playMedia(Uri result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(result);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, " no receiving apps installed!");
        }

    }


}
