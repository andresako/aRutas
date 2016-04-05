package asako.clase.rutas.UI;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Datos.MiConfig;
import asako.clase.rutas.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    private GoogleMap mMap;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    mMap.setMyLocationEnabled(true);
                } else {
                }
                break;
        }
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        } else {
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d("Pulsado el punto ", latLng.toString());
            }
        });

        //DUMMY DATOS
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Punto puntos : MiConfig.LISTA_PUNTOS) {
            mMap.addMarker(new MarkerOptions().position(puntos.getPosicion()).title(puntos.getNombre()));
            builder.include(puntos.getPosicion());
        }
        mMap.addMarker(new MarkerOptions().position(MiConfig.SALIDA.getPosicion()).title(MiConfig.SALIDA.getNombre()));

        builder.include(MiConfig.SALIDA.getPosicion());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);

        mMap.moveCamera(cu);

        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(LOCATION_PERMS, INITIAL_REQUEST);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }
}
