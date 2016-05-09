package asako.clase.rutas.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.AdaptadorSalida;

public class FragmentoSalida extends Fragment implements OnMapReadyCallback{


    private ActionBar appBar;
    private DrawerLayout mDrawer;
    private GoogleMap mMap;
    private PantallaInicio pa;
    private Ruta ruta;

    private TextView tiempo,distancia;

    public FragmentoSalida() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_salida, container, false);
        setHasOptionsMenu(true);

        //Bundle extras = getActivity().getIntent().getExtras();
        Bundle extras = getArguments();
        ruta = extras.getParcelable("ruta");
        pa = (PantallaInicio) super.getActivity();

        appBar = ((PantallaInicio) getActivity()).getSupportActionBar();
        appBar.setHomeAsUpIndicator(R.drawable.back);

        mDrawer = (DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        String currentDateandTime = sdf.format(new Date());

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(currentDateandTime);
        collapsingToolbar.setTitle(ruta.getTitulo());

        RecyclerView reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        AdaptadorSalida adp = new AdaptadorSalida(ruta.getListaLugaresVisitados());
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);
        reciclador.setAdapter(adp);

        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tiempo = (TextView)v.findViewById(R.id.salida_tiempo);
        distancia = (TextView)v.findViewById(R.id.salida_distancia);

        int time = 0;
        for (int x = 0; x < ruta.getListaLugaresVisitados().size();x++){
            time += ruta.getListaLugaresVisitados().get(x).getTiempoMedio();
        }
        tiempo.setText(time + " minutos");

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        PolylineOptions lineOptions = new PolylineOptions();

        for (int x=0; x< ruta.getListaLugaresVisitados().size();x++){
            Punto p = ruta.getListaLugaresVisitados().get(x);
            mMap.addMarker(new MarkerOptions().position(p.getPosicion()).title(p.getNombre()));
            builder.include(p.getPosicion());
            lineOptions.add(p.getPosicion());
        }
        LatLngBounds bounds = builder.build();
        lineOptions.width(2);
        lineOptions.color(Color.RED);


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);

        mMap.moveCamera(cu);
        mMap.addPolyline(lineOptions);
        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick ( Marker marker )
            {
                return true;
            }
        });
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        appBar.setHomeAsUpIndicator(R.drawable.drawer_toggle);
    }

}
