package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Dodavanje markera i pomeranje kamere
        LatLng kg = new LatLng(44.8303726,  20.454847);
        mMap.addMarker(new MarkerOptions().position(kg).title("Our office"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kg));

        //ovde ćemo kasnije podešavati tip mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //podešavam kontrolu zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //podešavam kontrolu kompas

        mMap.getUiSettings().setCompassEnabled(true);


        //podešavam program za rad sa MOJOM LOKACIJOM

        //ovako je bilo pre Android 6 modela dozvola
       /* mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);*/

        //sada to treba ovako
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {

            //uključivanje dijaloga za potvrdu dozvole
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }

        //Geokodiranje - prikazivanje adrese iz koordinata

        Geocoder gk = new Geocoder(getBaseContext(), Locale.getDefault());
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.addMarker(new MarkerOptions().position(position));
                Geocoder gk = new Geocoder(getBaseContext(), Locale.getDefault());

                try {
                    List<Address> adr;
                    adr = gk.getFromLocation(position.latitude, position.longitude, 1);
                    //String ad = "";
                    /*if (adr.size() > 0)
                        for (int i = 0; i < adr.get(0).getMaxAddressLineIndex(); i++)
                        ad += adr.get(0).getAddressLine(i) + "\n";*/

                    Address adresa = adr.get(0);

                    String lokalitet = adresa.getLocality();
                    String grad = adresa.getCountryName();
                    String region = adresa.getCountryCode();
                    String ulica = adresa.getFeatureName();

                    StringBuilder ad = new StringBuilder();
                    ad.append(lokalitet + " ");
                    ad.append(grad + " ");
                    ad.append(region + " ");
                    ad.append(ulica + " ");

                    Toast.makeText(getBaseContext(),ad.toString(),Toast.LENGTH_LONG).show();

                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }


   }

