package asako.clase.rutas.UI;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
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

public class FragmentoPerfil extends Fragment implements View.OnClickListener {
    private SharedPreferences sp;
    private MiConfig datos;
    private TextView tNombre, tUsuario, tSalida;
    private String TAG = "frgPerfil";

    public FragmentoPerfil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_perfil, container, false);
        setHasOptionsMenu(true);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        datos = ((PantallaInicio) getActivity()).datos;

        tNombre = (TextView) v.findViewById(R.id.texto_nombre);
        tUsuario = (TextView) v.findViewById(R.id.texto_email);
        tSalida = (TextView) v.findViewById(R.id.texto_salida);
        GridLayout grid = (GridLayout) v.findViewById(R.id.lSalida);
        grid.setOnClickListener(this);
        tNombre.setOnClickListener(this);
        String sNombre = sp.getString("nombre", "Dummy");
        String sApellido = sp.getString("apellidos", "Dummy");
        String nombreFinal = "Sin nombre aún, pulse para añadir";
        if (!sNombre.equalsIgnoreCase("null") || !sApellido.equalsIgnoreCase("null")) {
            nombreFinal = "";
            if (!sNombre.equalsIgnoreCase("null")) nombreFinal += sNombre + " ";
            if (!sApellido.equalsIgnoreCase("null")) nombreFinal += sApellido;
        }
        tNombre.setText(nombreFinal);
        tUsuario.setText(sp.getString("username", "Dummy"));
        String salida = "Sin punto de salida";
        if (datos.isSalidaSet()) {
            salida = datos.getSalida().getNomPosicion(getActivity());
        }
        tSalida.setText(salida);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        switch (v.getId()) {
            case R.id.texto_nombre:
                alert.setTitle("Nuevo nombre?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().trim().length() != 0) {
                            new ActualizarNombre().execute(input.getText().toString());
                        }
                    }
                });
                break;

            case R.id.lSalida:
                alert.setTitle("Direccion de salida?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().trim().length() != 0) {
                            new StringToSalida().execute(input.getText().toString());
                        }
                    }
                });
                break;
        }
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelado.
            }
        });
        alert.show();
    }


    private class StringToSalida extends AsyncTask<String, Void, Boolean> {

        JSONObject json;
        List<NameValuePair> params;
        LatLng latlng;
        Punto p;

        @Override
        protected Boolean doInBackground(String... args) {
            String direccion = args[0];
            String status;

            String url = "https://maps.googleapis.com/maps/api/geocode/json";
            params = new ArrayList<>();
            params.add(new BasicNameValuePair("address", direccion));

            JsonParser jsonParser = new JsonParser();
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

                    params = new ArrayList<>();
                    if (datos.isSalidaSet()) {
                        p = datos.getSalida();
                        p.setPosicion(latlng);

                        params.add(new BasicNameValuePair("accion", "7"));
                        params.add(new BasicNameValuePair("id", p.getID() + ""));
                    } else {
                        params.add(new BasicNameValuePair("accion", "4"));
                        params.add(new BasicNameValuePair("idUser", sp.getString("id_user", "0")));
                        params.add(new BasicNameValuePair("nombre", "SALIDA"));
                        params.add(new BasicNameValuePair("lat", latlng.latitude + ""));
                        params.add(new BasicNameValuePair("lng", latlng.longitude + ""));
                    }

                    try {
                        json = jsonParser.peticionHttp("http://overant.es/Andres/acciones.php", "POST", params);

                        if (json.getInt("Resultado") == 1) {
                            if (!datos.isSalidaSet()) {
                                Punto pt = new Punto(
                                        json.getInt("ID"),
                                        json.getString("nombre"),
                                        new LatLng(json.getDouble("lat"), json.getDouble("lng")));
                                datos.setSalida(pt);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }


                } else {
                    Log.d("Pos", "no hay LatLng");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {

            if (msg) {
                tSalida.setText(datos.getSalida().getNomPosicion(getActivity()));
            }
            super.onPostExecute(msg);
        }
    }
    private class ActualizarNombre extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... args) {
            String nombre = args[0];

            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("accion", "9"));
                params.add(new BasicNameValuePair("nombre", nombre));
                params.add(new BasicNameValuePair("id", sp.getString("id_user", "0")));

                JsonParser jParser = new JsonParser();
                JSONObject json = jParser.peticionHttp("http://overant.es/Andres/acciones.php", "POST", params);

                int success = json.getInt("Resultado");
                if (success == 1) {
                    Log.d(TAG, "Editado correctamente! " + json.toString());
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("nombre",nombre);
                    edit.apply();
                }else{
                    return false;
                }

            }catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            super.onPostExecute(msg);
            if (msg){
                tNombre.setText(sp.getString("nombre", "Vacio"));
            }
        }
    }

}
