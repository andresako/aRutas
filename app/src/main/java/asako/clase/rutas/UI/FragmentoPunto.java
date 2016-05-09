package asako.clase.rutas.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.JsonParser;
import asako.clase.rutas.Tools.MiConfig;

public class FragmentoPunto extends Fragment implements View.OnClickListener {

    private final FragmentManager fragmentManager;
    public boolean editado = false;
    private TextView nombre;
    private TextView direccion;
    private TextView descripcion;
    private Punto punto;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBar appBar;

    private String sNombre, sDireccion, sDescripcion = "";

    public FragmentoPunto() {
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_punto, container, false);

        punto = (Punto) getArguments().get("punto");

        // Textos editables
        nombre = (TextView) v.findViewById(R.id.texto_nombre);
        direccion = (TextView) v.findViewById(R.id.texto_direccion);
        descripcion = (TextView) v.findViewById(R.id.texto_detalles);
        nombre.setOnClickListener(this);
        direccion.setOnClickListener(this);
        descripcion.setOnClickListener(this);

        //poder hacer click en los demás items tambien
        v.findViewById(R.id.texto_nombreT).setOnClickListener(this);
        v.findViewById(R.id.texto_direccionT).setOnClickListener(this);
        v.findViewById(R.id.texto_detallesT).setOnClickListener(this);
        v.findViewById(R.id.texto_nombreI).setOnClickListener(this);
        v.findViewById(R.id.texto_direccionI).setOnClickListener(this);
        v.findViewById(R.id.texto_detallesI).setOnClickListener(this);

        //Rellenando datos
        if (!punto.getNombre().equals("")) {
            nombre.setText(punto.getNombre());
            direccion.setText(punto.getNomPosicion(getContext()));
            descripcion.setText(punto.getDescripcion());
        } else {
            nombre.setHint("Introduce nombre del nuevo punto");
            direccion.setHint("Introduce direccion");
            descripcion.setHint("Algun nombre para recordarlo?");
        }

        toolbar = (Toolbar) this.getActivity().findViewById(R.id.toolbar);
        appBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        mDrawer = (DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout);

        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        appBar.setHomeAsUpIndicator(R.drawable.back);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.save_item:
                new guardarPunto().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        appBar.setHomeAsUpIndicator(R.drawable.drawer_toggle);
    }

    @Override
    public void onClick(View v) {
        TextView tvct = null;
        boolean editar = false;
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Editar el campo");
        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());

        switch (v.getId()) {
            case R.id.texto_nombre:
            case R.id.texto_nombreI:
            case R.id.texto_nombreT:
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                input.setSingleLine(true);
                tvct = nombre;
                sNombre = input.getText().toString();
                editar = true;
                break;
            case R.id.texto_direccion:
            case R.id.texto_direccionI:
            case R.id.texto_direccionT:
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                tvct = direccion;
                sDireccion = input.getText().toString();
                editar = true;
                break;
            case R.id.texto_detalles:
            case R.id.texto_detallesI:
            case R.id.texto_detallesT:
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                tvct = descripcion;
                sDescripcion = input.getText().toString();
                editar = true;
                break;
        }
        if (editar) {
            alert.setView(input);
            final TextView finalTvct = tvct;
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (input.getText().toString().trim().length() != 0) {
                        finalTvct.setText(input.getText());
                        editado = true;
                    }
                }
            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Cancelado.
                }
            });
            alert.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_guardar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    class guardarPunto extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog pDialog;
        JsonParser jsonParser = new JsonParser();
        JSONObject json;
        List<NameValuePair> params;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Guardando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg) {
            String status;
            boolean res = false;
            String titulo = null;
            String detalle = null;
            LatLng latlng = null;

            //  Check localizacion
            String url = "https://maps.googleapis.com/maps/api/geocode/json";
            params = new ArrayList<>();
            params.add(new BasicNameValuePair("address", sDireccion));

            json = jsonParser.peticionHttp(url, "GET", params);

            try {
                status = json.getString("status");

                if (status.equals("OK")) {
                    JSONArray result = (JSONArray) json.get("results");
                    JSONObject obj = (JSONObject) result.get(0);
                    JSONObject geometry = (JSONObject) obj.get("geometry");
                    JSONObject location = (JSONObject) geometry.get("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    latlng = new LatLng(lat, lng);
                    Log.d("latLng", lat + " " + lng);
                } else {
                    Log.d("Pos", "no hay LatLng");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                res = false;
            }

            //  Check Nombre y detalles
            if (!sNombre.equals("")) {
                titulo = sNombre;
                detalle = sDescripcion;

            } else {
                Log.d("nombre", "no hay nombre");
            }

            //  Intento guardar el puntos
            if (latlng != null && titulo != null) {
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("idUser", sp.getString("id_user", "0")));
                params.add(new BasicNameValuePair("nombre", titulo));
                params.add(new BasicNameValuePair("lat", latlng.latitude + ""));
                params.add(new BasicNameValuePair("lng", latlng.longitude + ""));
                params.add(new BasicNameValuePair("descripcion", detalle));
                try {
                    json = jsonParser.peticionHttp("http://overant.es/Andres/puntoNuevo.php", "POST", params);

                    if (json.getInt("Resultado") == 1) {

                        Punto pt = new Punto(
                                json.getInt("ID"),
                                json.getString("nombre"),
                                new LatLng(json.getDouble("lat"), json.getDouble("lng")));
                        pt.setDescripcion(json.getString("descripcion"));
                        MiConfig mc = MiConfig.get();
                        mc.addPunto(pt.getID(), pt);
                        res = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    res = false;
                }
            }

            return res;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.d("Punto", "Guardado correctamente!");
            }
            pDialog.dismiss();
            super.onPostExecute(result);
        }
    }

}
