package com.hasanizmir.travelguide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hasanizmir.travelguide.R;
import com.hasanizmir.travelguide.model.PagerModel;

import java.util.List;

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.PagerVH> {
    private LayoutInflater inflater;
    private List<PagerModel> famousList;

    public PagerAdapter(Context context, List<PagerModel> famousList) {
        this.inflater = LayoutInflater.from(context);
        this.famousList = famousList;
    }

    class PagerVH extends RecyclerView.ViewHolder {
        AppCompatImageView cityPhotoIv;
        AppCompatTextView cityNameTv, cityDetailTv;

        PagerVH(@NonNull View itemView) {
            super(itemView);
            cityPhotoIv = itemView.findViewById(R.id.cityPhotoIv);
            cityNameTv = itemView.findViewById(R.id.cityNameTv);
            cityDetailTv = itemView.findViewById(R.id.cityDetailTv);
        }

        void setData(PagerModel model) {
            cityNameTv.setText(model.getPlaceName());
            cityPhotoIv.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), model.getResId()));
            if (model.getPlaceDetail() != null) {
                cityDetailTv.setText(model.getPlaceDetail());
            }
        }
    }

    @NonNull
    @Override
    public PagerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_famous_city, parent, false);
        return new PagerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerVH holder, int position) {
        PagerModel model = famousList.get(position);
        holder.setData(model);
    }

    @Override
    public int getItemCount() {
        return famousList.size();
    }
}
