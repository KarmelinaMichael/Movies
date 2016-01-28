package com.km.movies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    public static boolean mTwoPane;
    public static Bundle args = new Bundle();
    public static Intent intent;

    private  MovieGetJson[] movieGetJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       movieGetJson=MovieFragment.movieGetJson;
        if (findViewById(R.id.movie_container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int position) {
        if (mTwoPane) {

            movieGetJson=MovieFragment.movieGetJson;

           String[] tt=new String[6];
           tt[0]=movieGetJson[position].getTitle();
            tt[1]=movieGetJson[position].getImg();
            tt[2]=movieGetJson[position].getOverview();
            tt[3]=movieGetJson[position].getrDate();
            tt[4]=movieGetJson[position].getvAvg();
            tt[5]=movieGetJson[position].getId();
           // movieGetJson[position]=new MovieGetJson(tt[0],tt[1],tt[2],tt[3],tt[4],tt[5]);

       args.putString("id", tt[5]);
            args.putString("title", tt[0]);
            args.putString("img", "http://image.tmdb.org/t/p/w500/"+tt[1]);
            args.putString("overview", tt[2]);
            args.putString("rDate", tt[3]);
            args.putString("vAvg", tt[4]);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            movieGetJson=MovieFragment.movieGetJson;

            intent = new Intent(this, DetailActivity.class)

                    .putExtra("Title", movieGetJson[position].getTitle())
                    .putExtra("Img", "http://image.tmdb.org/t/p/w342/" + movieGetJson[position].getImg())
                    .putExtra("Overview", movieGetJson[position].getOverview())
                    .putExtra("rDate", movieGetJson[position].getrDate())
                    .putExtra("vAvg", movieGetJson[position].getvAvg())
                    .putExtra("id", movieGetJson[position].getId());
            startActivity(intent);
        }
    }
}
