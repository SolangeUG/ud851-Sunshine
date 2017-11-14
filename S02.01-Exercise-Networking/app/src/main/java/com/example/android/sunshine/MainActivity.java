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
package com.example.android.sunshine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // COMPLETED (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.

        // COMPLETED (3) Delete the for loop that populates the TextView with dummy data

        // COMPLETED (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // COMPLETED (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    private void loadWeatherData() {
        Context context = MainActivity.this;
        String location = SunshinePreferences.getPreferredWeatherLocation(context);

        /* When working with URLs
        URL weatherDataUrl = NetworkUtils.buildUrl(location);
        new FetchWeatherTask().execute(weatherDataUrl);
        */

        new FetchWeatherJSONDataTask().execute(location);

    }

    // COMPLETED (5) Create a class that extends AsyncTask to perform network requests
    // COMPLETED (6) Override the doInBackground method to perform your network requests
    // COMPLETED (7) Override the onPostExecute method to display the results of the network request

    public class FetchWeatherTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String weatherData = null;
            try {
                weatherData = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherData;
        }


        @Override
        protected void onPostExecute(String weatherData) {
            if (weatherData != null && ! "".equals(weatherData)) {
                mWeatherTextView.setText(weatherData);
            }
        }
    }

    /**
     * AsyncTask to retrieve weather data in JSON format
     */
    private class FetchWeatherJSONDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... locations) {
            // Instead of a URL array, this method receives an array of locations as parameter
            String location = locations[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String weatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                return OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, weatherResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] jsonWeatherData) {
            /*
             * Iterate through the array and append the Strings to the TextView. The reason why we add
             * the "\n\n\n" after the String is to give visual separation between each String in the
             * TextView. Later, we'll learn about a better way to display lists of data.
             */
            for (String weatherData : jsonWeatherData) {
                mWeatherTextView.append(weatherData + "\n\n\n");
            }
        }
    }
}