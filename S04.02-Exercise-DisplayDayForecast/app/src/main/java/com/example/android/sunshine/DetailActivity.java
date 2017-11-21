package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView mDayForecastDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        mDayForecastDisplay = (TextView) findViewById(R.id.tv_day_forecast);

        Intent originIntent = getIntent();
        if (originIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String dayForecast = originIntent.getStringExtra(Intent.EXTRA_TEXT);
            mDayForecastDisplay.setText(dayForecast);
        }

    }
}