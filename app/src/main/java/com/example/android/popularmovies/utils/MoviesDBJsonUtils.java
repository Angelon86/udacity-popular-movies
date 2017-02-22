/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class MoviesDBJsonUtils {

    private static final int INVALID_KEY_ERROR = 7;

    private static final int RESOURCE_NOT_FOUND_ERROR = 34;

    /**
     * This method parses JSON from a web response and returns an array of objects
     * describing the movies from the movies list.
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return JSONArray of JSON data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static JSONArray getMoviesDataFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        /* All movies are children of the "result" object */
        final String OWM_RESULTS = "results";

        final String OWM_STATUS_CODE = "status_code";

        /* String array to hold each movie's poster data */
        String[] parsedMoviesData = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(OWM_STATUS_CODE)) {
            int errorCode = moviesJson.getInt(OWM_STATUS_CODE);

            switch (errorCode) {
                case INVALID_KEY_ERROR:
                    /* "Invalid API key: You must be granted a valid key." */
                    return null;
                case RESOURCE_NOT_FOUND_ERROR:
                    /* The resource you requested could not be found. */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

        return moviesArray;
    }

}