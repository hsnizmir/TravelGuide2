package com.hasanizmir.travelguide.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hasanizmir.travelguide.R;
import com.hasanizmir.travelguide.adapters.LocationsAdapter;
import com.hasanizmir.travelguide.listeners.LocationClickListener;
import com.hasanizmir.travelguide.ui.maps.MapsActivity;
import com.hasanizmir.travelguide.utils.ShareUtils;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<LatLng> locations = new ArrayList<>();

    private RecyclerView locationsRv;
    private ProgressBar progressBar;
    public static LocationsAdapter locationsAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        locationsRv = root.findViewById(R.id.locationsRv);
        progressBar = root.findViewById(R.id.progressBar);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()), MapsActivity.class);
                intent.putExtra("info","new");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetLocationsTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class GetLocationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                MapsActivity.database = Objects.requireNonNull(getActivity()).openOrCreateDatabase("Places", MODE_PRIVATE, null);
                Cursor cursor = MapsActivity.database.rawQuery("SELECT * FROM places", null);

                int nameIx = cursor.getColumnIndex("name");
                int latitudeIx = cursor.getColumnIndex("latitude");
                int longitudeIx = cursor.getColumnIndex("longitude");

                if (names.size() > 0) {
                    names.clear();
                    locations.clear();
                }
                while (cursor.moveToNext()) {

                    String nameFromDatabase = cursor.getString(nameIx);
                    String latitudeFromDatabase = cursor.getString(latitudeIx);
                    String longitudeFromDatabase = cursor.getString(longitudeIx);

                    names.add(nameFromDatabase);

                    double l1 = Double.parseDouble(latitudeFromDatabase);
                    double l2 = Double.parseDouble(longitudeFromDatabase);

                    LatLng locationFromDatabase = new LatLng(l1, l2);

                    locations.add(locationFromDatabase);

                }

                cursor.close();

            } catch (Exception ignored) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()));
            locationsAdapter = new LocationsAdapter(getActivity(), names, new LocationClickListener() {
                @Override
                public void locationClicked(int position) {
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    intent.putExtra("info", "old");
                    intent.putExtra("position", position);
                    startActivity(intent);
                }

                @Override
                public void drawnDownClicked(final View view, final int position) {
                    PopupMenu menu = new PopupMenu(getContext(), view);
                    menu.getMenuInflater().inflate(R.menu.menu_drawn_down, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.share) {
                                ShareUtils.shareOnWhatsApp(Objects.requireNonNull(getActivity()), names.get(position));
                            } else if (item.getItemId() == R.id.update) {
                                Intent intent = new Intent(getContext(), MapsActivity.class);
                                intent.putExtra("info", "update");
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                            return true;
                        }
                    });
                    menu.show();
                }
            });
            locationsRv.setLayoutManager(layoutManager);

            locationsRv.setAdapter(locationsAdapter);
        }
    }
}