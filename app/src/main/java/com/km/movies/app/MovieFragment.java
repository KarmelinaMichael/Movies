package com.km.movies.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.km.movies.app.data.MovieContract;
import com.km.movies.app.data.MyDBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static ProgressDialog progressDialog;
    private static GridView view;
    private static ImageAdapter gridAdapter;
    private static FavAdapter favAdapter;
    private static String[] resultStrs;
    public static MovieGetJson[] movieGetJson;
    private static MovieGetJson mJson;
    private static String title;
    private  MovieGetJson [] mm;
    private static String img;
    private static String overview;
    private static String rDate;
    private static String vAvg;
    private String sort;

    private static String id;
    private MyDBHandler dbHandler;
    private String[] fav;
    private static final int CURSOR_LOADER_ID = 0;
    private TextView t;
    private int item2;
    private static Uri builtUri;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private Callback mCallbacks = sCallbacks;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */

      public  void onItemSelected(int position);
    }
    private static Callback sCallbacks = new Callback() {
        @Override
        public void onItemSelected(int id) {
        }
    };



    public MovieFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sort="http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";

        new FetchMovieTask(getActivity()).execute(sort);
        dbHandler = new MyDBHandler(getContext());

        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        dbHandler = new MyDBHandler(getContext());

        int id = item.getItemId();
        if (id == R.id.action_mostPopular) {
            menu(id);
            item2=R.id.action_mostPopular;

            FetchMovieTask movieTask = new FetchMovieTask(getActivity());
            sort="http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            movieTask.execute(sort);

            return true;
        }
        if (id == R.id.action_topRated) {
            menu(id);
           item2 =R.id.action_topRated;
            FetchMovieTask movieTask = new FetchMovieTask(getActivity());

            sort="http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc";
            movieTask.execute(sort);

            return true;
        }
        if (id == R.id.action_favorites) {
        item2=R.id.action_favorites;
            menu(id);


            FetchMovieTask movieTask1 = new FetchMovieTask(getActivity());
            String[] ss = printDatabase1().split(" ");

            
          movieGetJson=new MovieGetJson[ss.length];
            for(int i=0;i<ss.length;i++){
                movieGetJson[i]=new MovieGetJson(show(ss[i]).getTitle(), show(ss[i]).getImg(),
                        show(ss[i]).getOverview(),show(ss[i]).getrDate(),show(ss[i]).getvAvg(),show(ss[i]).getId());


            }

            movieTask1.onPostExecute(movieGetJson,"");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callback)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sCallbacks;
    }

@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            view.smoothScrollToPosition(mPosition);
        }

    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        view = (GridView) rootView.findViewById(R.id.gridview_movie);


        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                mPosition = position;

             ((Callback) getActivity()).onItemSelected(position);

            }

        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
           mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        //}
        return rootView;

    }

    public void menu (int item1){
        item2=item1;

    }

    public MovieGetJson show(String id){
        MovieGetJson movie= null;
        try {
            movie = dbHandler.getContact(id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  movie;
    }

    public String printDatabase(){
        dbHandler = new MyDBHandler(getContext());
        String db = dbHandler.databaseShow();
        return  db;
    }
    public String printDatabase1(){
        dbHandler = new MyDBHandler(getContext());
        String db = dbHandler.databaseShow1();
        return  db;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }



    public static class FetchMovieTask extends AsyncTask<String, Integer, MovieGetJson[]> implements com.km.movies.app.FetchMovieTask {
        Activity activity;

        public FetchMovieTask(Activity activity) {
            this.activity = activity;

        }

        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String MovieJsonStr = null;
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading images from TMDB. Please wait...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(String.format("Loading images from TMDB %s/%s. Please wait...", values[0], values[1]));
        }

        public MovieGetJson[] getWeatherDataFromJson(String MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String M_LIST = "results";
            final String M_POSTER = "poster_path";
            final String M_TITLE = "original_title";
            final String M_OVERVIEW = "overview";
            final String M_RDATE = "release_date";
            final String M_VAVG = "vote_average";
            final String M_ID="id";


            JSONObject movieJson = new JSONObject(MovieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(M_LIST);

            resultStrs = new String[movieArray.length()];
            movieGetJson=new MovieGetJson[movieArray.length()];
             for (int i = 0; i < movieArray.length(); i++) {

                JSONObject movie = movieArray.getJSONObject(i);

                 img = movie.getString(M_POSTER);
                 title=movie.getString(M_TITLE);
                 overview=movie.getString(M_OVERVIEW);
                 rDate=movie.getString(M_RDATE);
                 vAvg=movie.getString(M_VAVG);
                 id=movie.getString(M_ID);



                 mJson=new MovieGetJson(title,img,overview,rDate,vAvg,id);

                 movieGetJson[i]=mJson;
                 resultStrs[i] = "http://image.tmdb.org/t/p/w185/" + mJson.getImg();


             }
            for (String ss : resultStrs) {
                Log.v(LOG_TAG, "Movie entry: " + ss);
            }

            return movieGetJson;

        }

        @Override
        protected MovieGetJson[] doInBackground(String... params) {
//            if (params.length == 0) {
//                return null;
//            }


            try {


                final String FORECAST_BASE_URL = params[0];

                final String APPID_PARAM = "api_key";
                 builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
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

                MovieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movie JSON string: " + MovieJsonStr);
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
            try {
                return getWeatherDataFromJson(MovieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieGetJson[] result) {
            if(result!=null){
        String[] res=new  String[result.length];
            progressDialog.dismiss();
            for(int i=0;i<result.length;i++) {
                res[i]="http://image.tmdb.org/t/p/w342/" + result[i].getImg();

            }

            gridAdapter = new ImageAdapter(activity,res.length, res);


            view.setAdapter(gridAdapter);
            super.onPostExecute(result);}
            else  {

            Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }

        }
        @Override
        public void onPostExecute(MovieGetJson[] result, String s) {
            if(result!=null){
                String[] res=new  String[result.length];

                progressDialog.dismiss();
                for(int i=0;i<result.length;i++) {
                    res[i]=result[i].getImg();


                }

                favAdapter = new FavAdapter(activity,res.length,res);


                view.setAdapter(favAdapter);
                super.onPostExecute(result);
            }
            else  {

                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }

        }


    }

}
