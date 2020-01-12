package com.hasanizmir.travelguide.listeners;

import android.view.View;

public interface LocationClickListener {
    void locationClicked(int position);
    void drawnDownClicked(View view, int position);
}
