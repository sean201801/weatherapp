package com.example.shuangzhecheng.myweatherapp.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shuangzhecheng.myweatherapp.R;
import com.example.shuangzhecheng.myweatherapp.model.Current;
import com.example.shuangzhecheng.myweatherapp.model.Day;
import com.example.shuangzhecheng.myweatherapp.model.Forecast;
import com.example.shuangzhecheng.myweatherapp.model.Hour;
import com.example.shuangzhecheng.myweatherapp.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.shuangzhecheng.myweatherapp.ui.Constant.APIKEY;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.BASE_URL;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.CURRENTLY;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.DAILY;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.DATA;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.ERROR_DIALOG;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.HOURLY;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.HUMIDITY;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.ICON;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.SUMMARY;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.TEMPERATURE;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.TIME;
import static com.example.shuangzhecheng.myweatherapp.ui.Constant.TIME_ZONE;

/**
 * Created by shuangzhecheng on 2/6/16.
 */

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST="DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private Util util;


    private Forecast mForecast;

    @BindView(R.id.timeLabel)TextView mTimeLabel;
    @BindView(R.id.temperatureTextView)TextView mTemperatureLabel;
    @BindView(R.id.humidityValue)TextView mHunidityLabel;
    @BindView(R.id.precipValueLabel)TextView mPrecipLabel;
    @BindView(R.id.summaryLabel)TextView mSummaryLabel;
    @BindView(R.id.iconImageView)ImageView mImageIconLabel;
    @BindView(R.id.dailyButton)Button mButton;
    @BindView(R.id.refereshImageView)ImageView mRefreshImageView;
    @BindView(R.id.progressBar)ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        util = new Util();
        final double latitute = 41.9142;
        final double longitude = -88.3087;
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitute,longitude);
            }
        });
        getForecast(latitute,longitude);
            Log.d(TAG, "main thread");
    }

    /**
     * This method handles the network call and takes two params
     * @param latitute
     * @param longitude
     * This method returns forecast data for Chicago
     */
    private void getForecast(double latitute,double longitude) {
        String apiKey = APIKEY;
        String foreCastUrl = BASE_URL + apiKey + "/" + latitute + "," + longitude;

        if (util.isNetworkAvailable(MainActivity.this)) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.
                    Builder().
                    url(foreCastUrl).
                    build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    String jsonData=response.body().string();
                    if (response.isSuccessful()) {
                        try {

                            mForecast =getForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    upDateDisplay();
                                }
                            });
                        } catch (JSONException e) {
                            Log.e(TAG,"json Exception caught",e);
                        }
                    } else {
                        alertUserAboutError();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,"network unavailble",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Showing icon during download
     */
    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method is use to update the labels
     */
    private void upDateDisplay() {
        Current mCurrent=mForecast.getCurrent();
        mTemperatureLabel.setText(mCurrent.getmTemperature()+"");
        mTimeLabel.setText("At "+ mCurrent.getFormattedtime()+" it will be");
        mHunidityLabel.setText(mCurrent.getmHumidity()+"");
        mPrecipLabel.setText(mCurrent.getmPrecipChance()+"%");
        mSummaryLabel.setText(mCurrent.getmSummary());

        Drawable drawable=getResources().getDrawable(mCurrent.getIconId());
        mImageIconLabel.setImageDrawable(drawable);
    }

    /**
     * This method handle jsonData
     * @param jsonData
     * @return
     * @throws JSONException
     * it will return the forecast details
     */
    private Forecast getForecastDetails(String jsonData) throws JSONException{
        Forecast forecast=new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
            return forecast;
    }

    /**
     * This method handle jsonData
     * @param jsonData
     * @return
     * @throws JSONException
     * it will return the forecast details hourly
     */
    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecastObject=new JSONObject(jsonData);
        String timezone=forecastObject.getString(TIME_ZONE);
        JSONObject hourly=forecastObject.getJSONObject(HOURLY);
        JSONArray data=hourly.getJSONArray(DATA);

        Hour[] hours=new Hour[data.length()];
        for (int i=0;i<data.length();i++){
            JSONObject jsonHour=data.getJSONObject(i);
            Hour hour=new Hour();
            hour.setSummary(jsonHour.getString(SUMMARY));
            hour.setIcon(jsonHour.getString(ICON));
            hour.setTemperature(jsonHour.getDouble(TEMPERATURE));
            hour.setTime(jsonHour.getLong(TIME));
            hour.setTimeZone(timezone);
            hours[i]=hour;
        }
        return hours;
    }

    /**
     * This method handle jsonData
     * @param jsonData
     * @return
     * @throws JSONException
     * it will return the forecast details daily
     */
    private Day[] getDailyForecast(String jsonData) throws JSONException {

        JSONObject forecastObject=new JSONObject(jsonData);
        String timezone=forecastObject.getString(TIME_ZONE);
        JSONObject daily=forecastObject.getJSONObject(DAILY);
        JSONArray data=daily.getJSONArray(DATA);

       Day[] days=new Day[data.length()];
        for (int i=0;i<data.length();i++){
            JSONObject jsonDay=data.getJSONObject(i);
            Day day=new Day();
            day.setSummary(jsonDay.getString(SUMMARY));
            day.setIcon(jsonDay.getString(ICON));
            day.setMaxTemperature(jsonDay.getDouble("temperatureHigh"));
            day.setTime(jsonDay.getLong(TIME));
            day.setTimeZone(timezone);
            days[i]=day;
        }
        return days;
    }

    /**
     * This method handle jsonData
     * @param jsonData
     * @return
     * @throws JSONException
     * it will return the forecast details for current time
     */
    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecastObject=new JSONObject(jsonData);
        String timezone=forecastObject.getString(TIME_ZONE);
        JSONObject currently=forecastObject.getJSONObject(CURRENTLY);

        Current current =new Current();
        current.setmHumidity(currently.getDouble(HUMIDITY));
        current.setmIcon(currently.getString(ICON));
        current.setmPrecipChance(currently.getDouble("precipProbability"));
        current.setmTemperature(currently.getDouble(TEMPERATURE));
        current.setmSummary(currently.getString(SUMMARY));
        current.setmTime(currently.getLong(TIME));
        current.setTimeZone(timezone);
        Log.i(TAG,"From JSON: "+ current.getFormattedtime());
        return current;
    }


    private void alertUserAboutError() {
        new AlertDialogFragment().show(getFragmentManager(),ERROR_DIALOG);

    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent=new Intent(this,DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        startActivity(intent);
    }


    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }
}
