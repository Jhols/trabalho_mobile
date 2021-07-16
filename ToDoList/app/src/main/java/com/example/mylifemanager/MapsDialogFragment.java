package com.example.mylifemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static androidx.core.content.ContextCompat.getSystemService;

public class MapsDialogFragment extends DialogFragment {

    private static final String TAG = "MapsDialogFragment"; //Tag que identifica o dialog quando este for chamado
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private Context context;
    private GoogleMap mMap;
    private Button button = null;
    private TextView textView = null;

    private LocationManager locationManager = null;
    private String locationProvider = null;
    private @SuppressLint("MissingPermission")
    Location ultimaLocalizacaoConhecida = null;

    public MapsDialogFragment(Context context) {
        this.context = context;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            /*LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

            List<String> permissoes = new ArrayList<>();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                permissoes.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissoes.add(Manifest.permission.ACCESS_COARSE_LOCATION);

                ActivityCompat.requestPermissions(getActivity(), permissoes.toArray(new String[permissoes.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                Toast.makeText(getActivity(), "Permissão Garantida", Toast.LENGTH_SHORT).show();
            }

            ultimaLocalizacaoConhecida = new Location(locationProvider);
            ultimaLocalizacaoConhecida = locationManager.getLastKnownLocation(locationProvider);

            double userLat =  -12.9704; //ultimaLocalizacaoConhecida.getLatitude();
            double userLong = -38.5124; //ultimaLocalizacaoConhecida.getLongitude();

            LatLng salvador = new LatLng(userLat, userLong);
            googleMap.addMarker(new MarkerOptions().position(salvador).title("Marcador em Salvador"));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(salvador));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.locationProvider = LocationManager.NETWORK_PROVIDER;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return inflater.inflate(R.layout.fragment_maps_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        button = (Button) view.findViewById(R.id.btn_escolher_mapa);
        textView = (TextView) view.findViewById(R.id.txt_lati_longi);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "Botao Clicado");

                double userLat =  -12.9704; //ultimaLocalizacaoConhecida.getLatitude();
                double userLong = -38.5124; //ultimaLocalizacaoConhecida.getLongitude();
                MapsDialogFragment.this.textView.setText("Lat:" + userLat + " Long: " + userLong);

            }
        });
    }

    public LatLng getLatLong() {
        return new LatLng(-12.9704, -38.5124);
    }
}