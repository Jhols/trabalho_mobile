package com.example.atividade_mobile;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ExercicioGPS extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private GoogleMap mMap;
    private Button button = null;
    private TextView textView = null;

    private LocationManager locationManager = null;
    private String locationProvider = null;
    private @SuppressLint("MissingPermission")
    Location ultimaLocalizacaoConhecida = null;
    /*private double latitude = -12.9704;
    private double longitude = -38.5124;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio_gps);

        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        button = (Button) findViewById(R.id.btn_lati_longi);
        textView = (TextView) findViewById(R.id.txt_lati_longi);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "Botao Clicado");

                double userLat = ultimaLocalizacaoConhecida.getLatitude();
                double userLong = ultimaLocalizacaoConhecida.getLongitude();
                ExercicioGPS.this.textView.setText("Lat:" + userLat + " Long: " + userLong);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<String> permissoes = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            permissoes.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissoes.add(Manifest.permission.ACCESS_COARSE_LOCATION);

            ActivityCompat.requestPermissions(this, permissoes.toArray(new String[permissoes.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            Toast.makeText(this, "Permissão Garantida", Toast.LENGTH_SHORT).show();
        }

        this.ultimaLocalizacaoConhecida = new Location(locationProvider);
        this.ultimaLocalizacaoConhecida = locationManager.getLastKnownLocation(locationProvider);

        double userLat = ultimaLocalizacaoConhecida.getLatitude();
        double userLong = ultimaLocalizacaoConhecida.getLongitude();
        Log.d("Lati_Long", "Lat: " + userLat + " Long: " + userLong);

        LatLng salvador = new LatLng(userLat, userLong);
        googleMap.addMarker(new MarkerOptions().position(salvador).title("Marcador em Salvador"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(salvador));

    }
}