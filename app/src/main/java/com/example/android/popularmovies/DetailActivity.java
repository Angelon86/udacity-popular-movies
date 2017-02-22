package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    final static String POSTERS_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    final String OWN_POSTER_PATH = "poster_path";
    final String OWN_TITLE = "title";
    final String OWN_RELEASE_DATE = "release_date";
    final String OWN_RATING = "vote_average";
    final String OWN_SYNOPSIS = "overview";


    private String mMovieObject;
    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMovieRating;
    private TextView mMovieSynopsis;
    private ImageView mMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mMovieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mMovieRating = (TextView) findViewById(R.id.tv_movie_rating);
        mMovieSynopsis = (TextView) findViewById(R.id.tv_movie_synopsis);
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieObject = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                try {
                    JSONObject movieJson = new JSONObject(mMovieObject);
                    mMovieTitle.setText(movieJson.getString(OWN_TITLE));
                    mMovieReleaseDate.setText(movieJson.getString(OWN_RELEASE_DATE));
                    mMovieRating.setText(movieJson.getString(OWN_RATING));
                    mMovieSynopsis.setText(movieJson.getString(OWN_SYNOPSIS));
                    Picasso.with(this).load(POSTERS_BASE_URL + movieJson.getString(OWN_POSTER_PATH)).into(mMoviePoster);
                } catch (JSONException e) {
                    //TODO handle exception
                }
            }
        }
    }
}
