package com.tonny.mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tonny.mm.Adapters.NewsAdapter;
import com.tonny.mm.Models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.tonny.mm.Utils.Urls.NEWS_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    RecyclerView newsRecycler;
    NewsAdapter nAdapter;
    ArrayList<News> newsList;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG = "NewsFragment";


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsRecycler = view.findViewById(R.id.newsRecycler);
        swipeRefreshLayout = view.findViewById(R.id.newsSwipe);

        //News RecyclerView population
        newsList = new ArrayList<>();
        nAdapter = new NewsAdapter(newsList, getContext());
        newsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecycler.setItemAnimator(new DefaultItemAnimator());
        newsRecycler.setAdapter(nAdapter);

        // Swipe Refresh for news and latest posts
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);
                getNews();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getNews();

            }
        });
        return view;
    }

    private void getNews(){

        swipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, NEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            newsList.clear();
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jArray = jObj.getJSONArray("articles");
//                          boolean error = jObj.getBoolean("error");
                            for(int i=0;i<jArray.length();i++) {
                                JSONObject obj = jArray.getJSONObject(i);

                                News news = new News();

                                news.setTitle(obj.getString("title"));
                                Log.d(TAG, "onResponse: "+obj.getString("title"));
                                news.setImage(obj.getString("urlToImage"));
                                news.setText(obj.getString("description"));

                                newsList.add(news);

                            }
                            nAdapter.notifyDataSetChanged();

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
