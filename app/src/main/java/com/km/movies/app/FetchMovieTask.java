package com.km.movies.app;

/**
 * Created by lina on 1/27/2016.
 */
public interface FetchMovieTask {
    void onPostExecute(MovieGetJson[] result, String s);
}
