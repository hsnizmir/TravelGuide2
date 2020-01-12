package com.hasanizmir.travelguide.ui.maps;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hasanizmir.travelguide.R;
import com.hasanizmir.travelguide.ui.home.HomeFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    public static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        findViewById(R.id.toolbarBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (Objects.requireNonNull(info).matches("new")) {
            mMap.setOnMapLongClickListener(this);

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.hasanizmir.travelguide",MODE_PRIVATE);
                    boolean firstTimeCheck = sharedPreferences.getBoolean("notFirstTime",false);

                    if (!firstTimeCheck) {
                        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                        sharedPreferences.edit().putBoolean("notFirstTime",true).apply();
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };


            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    mMap.clear();

                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLocation != null) {
                        LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                    }
                }
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                }


            }

        } else if (Objects.requireNonNull(info).matches("old")){
            mMap.clear();
            int position = intent.getIntExtra("position",0);
            LatLng location = new LatLng(HomeFragment.locations.get(position).latitude,HomeFragment.locations.get(position).longitude);
            String placeName = HomeFragment.names.get(position);

            mMap.addMarker(new MarkerOptions().title(placeName).position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
            mMap.setOnMapLongClickListener(null);

        } else {
            mMap.clear();
            mMap.setOnMapLongClickListener(this);
            int position = intent.getIntExtra("position",0);
            LatLng location = new LatLng(HomeFragment.locations.get(position).latitude,HomeFragment.locations.get(position).longitude);
            String placeName = HomeFragment.names.get(position);

            mMap.addMarker(new MarkerOptions().title(placeName).position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (requestCode == 1) {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    Intent intent = getIntent();
                    String info = intent.getStringExtra("info");

                    if (Objects.requireNonNull(info).matches("new")) {
                        mMap.setOnMapLongClickListener(this);
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lastLocation != null) {
                            LatLng lastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                        }
                    } else if (Objects.requireNonNull(info).matches("old")){
                        mMap.clear();
                        mMap.setOnMapLongClickListener(null);
                        int position = intent.getIntExtra("position",0);
                        LatLng location = new LatLng(HomeFragment.locations.get(position).latitude,HomeFragment.locations.get(position).longitude);
                        String placeName = HomeFragment.names.get(position);

                        mMap.addMarker(new MarkerOptions().title(placeName).position(location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

                    } else {
                        mMap.clear();
                        mMap.setOnMapLongClickListener(this);
                        int position = intent.getIntExtra("position",0);
                        LatLng location = new LatLng(HomeFragment.locations.get(position).latitude,HomeFragment.locations.get(position).longitude);
                        String placeName = HomeFragment.names.get(position);

                        mMap.addMarker(new MarkerOptions().title(placeName).position(location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

                    }

                }

            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if (addressList != null && addressList.size() > 0 ) {
                if (addressList.get(0).getThoroughfare() != null) {
                    address += addressList.get(0).getThoroughfare();

                    if (addressList.get(0).getSubThoroughfare() != null) {
                        address += addressList.get(0).getSubThoroughfare();
                    }
                }
            } else {
                address = getString(R.string.title_new_place);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (Objects.requireNonNull(info).matches("new")) {

            mMap.addMarker(new MarkerOptions().title(address).position(latLng));
            Snackbar snackbar = Snackbar.make(findViewById(R.id.map),
                    getString(R.string.title_new_place_success),
                    Snackbar.LENGTH_SHORT);
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    finish();
                }
            });
            HomeFragment.names.add(address);
            HomeFragment.locations.add(latLng);
            HomeFragment.locationsAdapter.notifyDataSetChanged();
            snackbar.show();

            try {

                double l1 = latLng.latitude;
                double l2 = latLng.longitude;

                String coord1 = Double.toString(l1);
                String coord2 = Double.toString(l2);

                database = this.openOrCreateDatabase("Places", MODE_PRIVATE, null);

                database.execSQL("CREATE TABLE IF NOT EXISTS places (name VARCHAR, latitude VARCHAR, longitude VARCHAR)");

                String toCompile = "INSERT INTO places (name, latitude, longitude) VALUES (?, ?, ?)";

                SQLiteStatement sqLiteStatement = database.compileStatement(toCompile);

                sqLiteStatement.bindString(1, address);
                sqLiteStatement.bindString(2, coord1);
                sqLiteStatement.bindString(3, coord2);

                sqLiteStatement.execute();

            } catch (Exception ignore) {

            }
        } else {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().title(address).position(latLng));

            try {

                double l1 = latLng.latitude;
                double l2 = latLng.longitude;

                String coord1 = Double.toString(l1);
                String coord2 = Double.toString(l2);

                int position = intent.getIntExtra("position", -1);

                if (position != -1) {

                    database = this.openOrCreateDatabase("Places", MODE_PRIVATE, null);

                    database.execSQL("CREATE TABLE IF NOT EXISTS places (name VARCHAR, latitude VARCHAR, longitude VARCHAR)");

                    String  updateRowLatitude = Double.toString(HomeFragment.locations.get(position).latitude);
                    String updateRowLongitude = Double.toString(HomeFragment.locations.get(position).longitude);

                    String toCompile = "UPDATE places SET name = ?, latitude = ?, longitude = ? WHERE latitude = ? AND longitude = ?";
                    SQLiteStatement sqLiteStatement = database.compileStatement(toCompile);
                    sqLiteStatement.bindString(1, address);
                    sqLiteStatement.bindString(2, coord1);
                    sqLiteStatement.bindString(3, coord2);
                    sqLiteStatement.bindString(4, updateRowLatitude);
                    sqLiteStatement.bindString(5, updateRowLongitude);

                    sqLiteStatement.execute();

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.map),
                            getString(R.string.title_new_place_success),
                            Snackbar.LENGTH_SHORT);
                    snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                        }
                    });
                    snackbar.show();
                }

            } catch (Exception ignore) {
            }
        }
    }
}