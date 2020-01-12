package com.hasanizmir.travelguide.utils;

import android.content.Context;

import com.hasanizmir.travelguide.R;
import com.hasanizmir.travelguide.model.PagerModel;

import java.util.ArrayList;

public class Constants {

    public static ArrayList<PagerModel> getFamousCities(Context context) {
        ArrayList<PagerModel> famousList = new ArrayList<>();
        famousList.add(new PagerModel(R.drawable.bg_izmir_main_area, context.getString(R.string.city_name_istanbul)));
        famousList.add(new PagerModel(R.drawable.bg_izmir_main_area, context.getString(R.string.city_name_izmir), context.getString(R.string.large_text)));
        famousList.add(new PagerModel(R.drawable.bg_izmir_main_area, context.getString(R.string.city_name_madrid)));
        famousList.add(new PagerModel(R.drawable.bg_izmir_main_area, context.getString(R.string.city_name_rio)));
        famousList.add(new PagerModel(R.drawable.ic_drawn_down, context.getString(R.string.city_name_ny)));
        famousList.add(new PagerModel(R.drawable.ic_add_place, context.getString(R.string.city_name_london)));
        return famousList;
    }
}
