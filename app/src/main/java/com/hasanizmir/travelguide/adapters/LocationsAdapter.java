package com.hasanizmir.travelguide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hasanizmir.travelguide.R;
import com.hasanizmir.travelguide.listeners.LocationClickListener;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationVH> {
    private LayoutInflater inflater;
    private List<String> locationList;
    private LocationClickListener locationClickListener;

    public LocationsAdapter(Context context, List<String> locationList, LocationClickListener locationClickListener) {
        inflater = LayoutInflater.from(context);
        this.locationList = locationList;
        this.locationClickListener = locationClickListener;
    }

    class LocationVH extends RecyclerView.ViewHolder {
        AppCompatTextView locationNameTv;
        AppCompatImageButton drawnDownIv;

        LocationVH(@NonNull View itemView) {
            super(itemView);
            locationNameTv = itemView.findViewById(R.id.locationNameTv);
            drawnDownIv = itemView.findViewById(R.id.drawnDownIv);
        }

        void setData(String locationName, final int position) {
            locationNameTv.setText(locationName);
            locationNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationClickListener.locationClicked(position);
                }
            });

            drawnDownIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationClickListener.drawnDownClicked(drawnDownIv, position);
                }
            });
        }
     }

    @NonNull
    @Override
    public LocationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_location, parent, false);
        return new LocationVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationVH holder, int position) {
        String locationName = locationList.get(position);
        holder.setData(locationName, position);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
