package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utils.MoviesDBJsonUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    final static String SORT_BY_POPULARITY = "popularity.desc";
    final static String SORT_BY_RATING = "vote_average.desc";

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * Set a GridLayoutManager
         */
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The MoviesAdapter is responsible for linking our movies data with the Views that
         * will end up displaying our.
         */
        mMoviesAdapter = new MoviesAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMoviesAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the movies data. */
        loadMoviesData(SORT_BY_POPULARITY);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popular) {
            mMoviesAdapter.setMoviesData(null);
            loadMoviesData(SORT_BY_POPULARITY);
            return true;
        }
        if (id == R.id.action_sort_rating) {
            mMoviesAdapter.setMoviesData(null);
            loadMoviesData(SORT_BY_RATING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will get the movies data in the background.
     */
    private void loadMoviesData(String sortType) {
        showMoviesDataView();

        new FetchMoviesTask().execute(sortType);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            if (params[0] != null) {
                URL moviesRequestUrl = NetworkUtils.buildUrl(params[0]);

                try {
                    if (NetworkUtils.isOnline(MainActivity.this)) { // check netwrk connectivity
                        String jsonMoviesResponse = NetworkUtils
                                .getResponseFromHttpUrl(moviesRequestUrl);

                        JSONArray simpleJsonMoviesData = MoviesDBJsonUtils
                                .getMoviesDataFromJson(MainActivity.this, jsonMoviesResponse);

                        return simpleJsonMoviesData;
                    }

                    return null;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                mMoviesAdapter.setMoviesData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public void onClick(JSONObject param) {
        //TODO show detail view
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, param.toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
