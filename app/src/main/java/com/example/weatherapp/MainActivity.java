
package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    EditText etCity, etCountry;
    TextView tvResult;
    private final String appid = BuildConfig.API_KEY;//"b6e14c98a02ac1e05072cb9683a7f659";

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);


    }

    public void getWeatherDetails(View view) {
        String tempUrl;
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String url = "https://api.openweathermap.org/data/2.5/weather";
        if (!country.equals("")) {
            tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
            //Log.d("response", response);
            String output="";
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                JSONObject jsonObjectWather = jsonArray.getJSONObject(0);
                String description = jsonObjectWather.getString("description");

                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                double temp = jsonObjectMain.getDouble("temp")-273.15;
                double feelsLike = jsonObjectMain.getDouble("feels_like")-273.15;
                float pressure = jsonObjectMain.getInt("pressure");
                int humidity = jsonObjectMain.getInt("humidity");
                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                String wind = jsonObjectWind.getString("speed");
                JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                String clouds = jsonObjectClouds.getString("all");
                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                String countryName = jsonObjectSys.getString("country");
                String cityName = jsonResponse.getString("name");

                tvResult.setTextColor(Color.rgb(68,134,199));

                output +="Current wather of "+cityName+" ("+countryName+")"
                        +"\n Temp: "+df.format(temp)+" \u2103"
                        +"\n Feels Like: "+df.format(feelsLike)+" \u2103"
                        +"\n Humidity: "+humidity+"%"
                        +"\n Description: "+description
                        +"\n Wind Speed: "+wind+"m/s (meters per second)"
                        +"\n Cloudiness: "+clouds+"%"
                        +"\n Pressure: "+pressure+"hpa";
                tvResult.setText(output);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}