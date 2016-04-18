package asako.clase.rutas.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;

public class MiConfig implements Serializable {

    private static final String URL_GENERICA = "http://overant.es/Andres/";
    private static final String URL_PUNTOS = "puntos.php";
    private static final String URL_RUTAS = "rutas.php";
    private static final String URL_HISTORIAL = "historial.php";
    private static final String TAG_PUNTOS = "Puntos";
    private static final String TAG_RUTAS = "Rutas";
    private static final String TAG_HISTORIAL = "Historial";
    private static final String TAG_RESULTADO = "Resultado";
    private static final String TAG_RESULTADO_DESCRIPCION = "Desc";
    static List<NameValuePair> params = new ArrayList<>();
    static InputStream is = null;
    static String json = "";
    static JSONObject jObj = null;
    private static MiConfig MC;
    private final Context context;
    public HashMap<Integer, Punto> HASH_PUNTOS = new HashMap<>();
    public HashMap<Integer, Ruta> HASH_RUTAS = new HashMap<>();
    public HashMap<Integer, Historial> HASH_HISTORIAL = new HashMap<>();
    private Punto SALIDA = null;

    public MiConfig(Context cntx) {
        this.context = cntx;
        new rellenarDatos().execute();

    }

    public static MiConfig getConfig() {
        return MC;
    }

    public void setConfig(MiConfig mc) {
        MC = mc;
    }

    public boolean isSalidaSet() {
        return SALIDA != null;
    }

    public Punto getSalida() {
        return this.SALIDA;
    }

    public void setSalida(Punto pt) {
        this.SALIDA = pt;
    }

    private JSONObject recogerDatosDe(String url) {
        // Pedir y recoger datos
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convertir resultado en String
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            json = Html.fromHtml(json).toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error convirtiendo resultado " + e.toString());
        }

        // Convertir String en JsonObject
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parseando datos " + e.toString());
        }

        return jObj;
    }

    private class rellenarDatos extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {
            SharedPreferences shPr = PreferenceManager.getDefaultSharedPreferences(context);
            String ID = shPr.getString("user_ID", "0");
            params.add(new BasicNameValuePair("user", ID));

            JSONObject joPuntos = recogerDatosDe(URL_GENERICA + URL_PUNTOS);
            JSONObject joRutas = recogerDatosDe(URL_GENERICA + URL_RUTAS);
            JSONObject joHistorial = recogerDatosDe(URL_GENERICA + URL_HISTORIAL);

            //Rellenando PUNTOS
            try {
                JSONArray mPuntos = joPuntos.getJSONArray(TAG_PUNTOS);
                for (int i = 0; i < mPuntos.length(); i++) {
                    JSONObject p = mPuntos.getJSONObject(i);

                    int id = p.getInt("ID");
                    String nombre = p.getString("nombre");
                    Double lat = p.getDouble("lat");
                    Double lng = p.getDouble("lng");
                    String det = p.getString("detalles");
                    LatLng pos = new LatLng(lat, lng);

                    Punto ctP = new Punto(id, nombre, pos);
                    ctP.setDetalles(det);

                    HASH_PUNTOS.put(id, ctP);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Rellenando RUTAS
            try {
                JSONArray mRutas = joRutas.getJSONArray(TAG_RUTAS);
                for (int i = 0; i < mRutas.length(); i++) {
                    JSONObject r = mRutas.getJSONObject(i);

                    int id = r.getInt("ID");
                    String titulo = r.getString("titulo");
                    JSONArray puntos = r.getJSONArray(TAG_PUNTOS);
                    List<Punto> listaCt = new ArrayList<>();
                    for (int j = 0; j < puntos.length(); j++) {
                        JSONObject rp = puntos.getJSONObject(j);
                        Punto pt = HASH_PUNTOS.get(rp.getInt("ID"));
                        //pt.setTiempoMedio(rp.getInt("tiempo"));
                        listaCt.add(pt);
                    }
                    Ruta ctR = new Ruta(titulo, 0, listaCt);
                    HASH_RUTAS.put(id, ctR);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Rellenando Historial
            try {
                JSONArray mHistorial = joHistorial.getJSONArray(TAG_HISTORIAL);
                for (int i = 0; i < mHistorial.length(); i++) {
                    JSONObject h = mHistorial.getJSONObject(i);

                    int id = h.getInt("ID");
                    int id_ruta = h.getInt("ID_ruta");
                    String fecha = h.getString("Fecha");

                    Historial ctH = new Historial(HASH_RUTAS.get(id_ruta), fecha);
                    HASH_HISTORIAL.put(id, ctH);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

    }
}