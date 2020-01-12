package com.hasanizmir.travelguide.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.hasanizmir.travelguide.R;
import com.hasanizmir.travelguide.adapters.PagerAdapter;
import com.hasanizmir.travelguide.utils.Constants;

import java.util.Objects;

public class GalleryFragment extends Fragment {

    private ViewPager2 pager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        pager = root.findViewById(R.id.pager);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        PagerAdapter adapter = new PagerAdapter(Objects.requireNonNull(getActivity()),
                Constants.getFamousCities(Objects.requireNonNull(getActivity())));
        pager.setAdapter(adapter);
    }
}