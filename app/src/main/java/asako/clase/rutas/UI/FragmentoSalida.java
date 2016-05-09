package asako.clase.rutas.UI;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.AdaptadorSalida;

public class FragmentoSalida extends Fragment implements OnMapReadyCallback {


    private ActionBar appBar;
    private DrawerLayout mDrawer;
    private GoogleMap mMap;
    private PantallaInicio pa;
    private Ruta ruta;
    private SharedPreferences sp;

    private TextView tiempo, distancia;
    private int tiempoTotal, distanciaTotal = 0;

    public FragmentoSalida() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_salida, container, false);
        setHasOptionsMenu(true);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tiempo = (TextView) v.findViewById(R.id.salida_tiempo);
        distancia = (TextView) v.findViewById(R.id.salida_distancia);

        for (int x = 0; x < ruta.getListaLugaresVisitados().size(); x++) {
            int time= ruta.getListaLugaresVisitados().get(x).getTiempoMedio() * 60;
            tiempoTotal += time;
        }

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        for (int x = 0; x < ruta.getListaLugaresVisitados().size(); x++) {
            Punto p = ruta.getListaLugaresVisitados().get(x);
            mMap.addMarker(new MarkerOptions().position(p.getPosicion()).title(p.getNombre()));
            builder.include(p.getPosicion());
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);


        rellenarMapa(mMap);
    }

    private void rellenarMapa(GoogleMap mMap) {
        StringBuilder stringBuilder = new StringBuilder();

        String salida = sp.getString("lat", "") + "," + sp.getString("lng", "");
        String puntos = "";
        for (int x = 0; x < ruta.getListaLugaresVisitados().size(); x++) {
            Punto p = ruta.getListaLugaresVisitados().get(x);
            if(x != 0)puntos += "%7C";
            puntos += p.getPosicion().latitude + "," + p.getPosicion().longitude;
        }
        try {
            String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + salida + "&destination=" + salida + "&waypoints=" + puntos + "&mode=driving";
            HttpPost httppost = new HttpPost(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (IOException e) {
        }
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray array = jsonObject.getJSONArray("routes");
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            for (int x = 0; x < legs.length(); x++){
                JSONObject steps = legs.getJSONObject(x);
                Log.d("FgmSalida, ", "steps: "+ steps.toString());
                JSONObject distance = steps.getJSONObject("distance");
                JSONObject duration = steps.getJSONObject("duration");
                distanciaTotal += distance.getInt("value");
                tiempoTotal += duration.getInt("value");
            }
            JSONObject draw = routes.getJSONObject("overview_polyline");
            String poly = draw.getString("points");

            pintarMapa(mMap, poly);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        distancia.setText(distanciaTotal/1000 + " Km");
        tiempo.setText(tiempoTotal /60 + "min");

    }

    private void pintarMapa(GoogleMap mMap, String poli) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        PolylineOptions lineOptions = new PolylineOptions();

        int index = 0, len = poli.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = poli.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poli.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            lineOptions.add(p);
            builder.include(p);
        }
        lineOptions.width(5);
        lineOptions.color(Color.RED);
        mMap.addPolyline(lineOptions);

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.moveCamera(cu);
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
