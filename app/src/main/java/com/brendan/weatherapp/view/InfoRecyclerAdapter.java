package com.brendan.weatherapp.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.brendan.weatherapp.R;
import com.brendan.weatherapp.databinding.WeatherInfoItemBinding;
import com.brendan.weatherapp.view.model.WeatherInfoItem;

import java.util.ArrayList;

public class InfoRecyclerAdapter extends RecyclerView.Adapter<InfoRecyclerAdapter.InfoItemViewHolder>{

    private ArrayList<WeatherInfoItem> weatherItems;

    public InfoRecyclerAdapter(ArrayList<WeatherInfoItem> weatherItems) {
        this.weatherItems = weatherItems;
    }

    @NonNull
    @Override
    public InfoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeatherInfoItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.weather_info_item,
                parent,
                false
        );
        return new InfoItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoItemViewHolder holder, int position) {
        holder.itemBinding.setInfoItem(weatherItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (weatherItems == null) {
            return 0;
        }
        return weatherItems.size();
    }

    static class InfoItemViewHolder extends RecyclerView.ViewHolder{

        private final WeatherInfoItemBinding itemBinding;

        public InfoItemViewHolder(@NonNull WeatherInfoItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}
