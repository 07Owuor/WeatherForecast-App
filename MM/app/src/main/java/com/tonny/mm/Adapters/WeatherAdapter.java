package com.tonny.mm.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tonny.mm.Models.Weather;
import com.tonny.mm.R;

import java.util.ArrayList;

/**
 * Created by Nitrozeus on 19/08/2021.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    ArrayList<Weather> weatherList;
    Context context;

    public WeatherAdapter(ArrayList<Weather> weatherList, Context context) {
        this.weatherList = weatherList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.weatherDate);
            title = itemView.findViewById(R.id.weatherText);
            description = itemView.findViewById(R.id.weatherDescription);
        }
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder viewHolder, int i) {

        Weather weather = weatherList.get(i);
        viewHolder.title.setText(weather.getTitle());
        viewHolder.date.setText(weather.getDate());
        viewHolder.description.setText(weather.getDescription());

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


}
