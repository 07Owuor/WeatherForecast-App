package com.tonny.mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tonny.mm.Adapters.WeatherAdapter;
import com.tonny.mm.Models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.tonny.mm.Utils.Urls.WEATHER_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    RecyclerView recyclerView;
    WeatherAdapter adapter;
    ArrayList<Weather> weatherList;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView title, description;

    private static final String TAG = "WeatherFragment";

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_weather, container, false);
        title = view.findViewById(R.id.currentWeather);
        description = view.findViewById(R.id.currentDescription);
        recyclerView = view.findViewById(R.id.weatherRecycler);
        swipeRefreshLayout = view.findViewById(R.id.weatherSwipe);

        //News RecyclerView population
        weatherList = new ArrayList<>();
        adapter = new WeatherAdapter(weatherList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Swipe Refresh for news and latest posts
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);
                geCurrentWeather();
                getWeather();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                geCurrentWeather();
                getWeather();

            }
        });


        return view;
    }

    private void geCurrentWeather(){

        swipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WEATHER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {

                            JSONObject jObj = new JSONObject(response);
                            JSONObject current = jObj.getJSONObject("current");
                            JSONArray jArray = current.getJSONArray("weather");
//                          boolean error = jObj.getBoolean("error");
                            for(int i=0;i<jArray.length();i++) {
                                JSONObject obj = jArray.getJSONObject(i);

                                title.setText(obj.getString("main"));
                                description.setText(obj.getString("description"));



                            }


                            swipeRefreshLayout.setRefreshing(false);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Oops: " + error );
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        // Adding request to request queue
        queue.add(stringRequest);

    }

    private void getWeather(){

        swipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WEATHER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            weatherList.clear();
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jArray = jObj.getJSONArray("daily");
//                          boolean error = jObj.getBoolean("error");
                            for(int i=0;i<jArray.length();i++) {
                                JSONObject obj = jArray.getJSONObject(i);

                                JSONArray weatherArray = obj.getJSONArray("weather");
                                for(int j=0; j<weatherArray.length(); j++) {
                                    JSONObject wObj = weatherArray.getJSONObject(j);

                                    Weather weather = new Weather();

                                    long time = obj.getLong("dt") * (long) 1000;
                                    Date date = new Date(time);
                                    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss");
                                    format.setTimeZone(TimeZone.getTimeZone("GMT"));

                                    weather.setDate(format.format(date));
                                    weather.setTitle(wObj.getString("main"));
                                    weather.setDescription(wObj.getString("description"));


                                    weatherList.add(weather);
                                }


                            }
                            adapter.notifyDataSetChanged();

                            swipeRefreshLayout.setRefreshing(false);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Oops: " + error );
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        // Adding request to request queue
        queue.add(stringRequest);

    }

}
