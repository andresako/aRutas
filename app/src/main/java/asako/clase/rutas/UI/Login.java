package asako.clase.rutas.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.JsonParser;
import asako.clase.rutas.Tools.MiConfig;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String LOGIN_URL = "http://overant.es/Andres/login.php";
    // TAGS de respuestas del JSON php Script;
    private static final String TAG_USER = "username";
    private static final String TAG_PASS = "password";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_APELLIDOS = "apellidos";
    private static final String TAG_RESULTADO = "Resultado";
    private static final String TAG_RESUTLADO_DESCRIPCION = "Desc";
    private static final String TAG_ID_USER = "id_user";
    private String TAG = "Login";
    private MiConfig mc;
    private TextView pass2T;
    private EditText user, pass, pass2;
    private CheckBox cbRecordar;
    private Button mSubmit, btnRegistro, btnRegAcc, btnRegCan;
    private ProgressDialog pDialog;
    private SharedPreferences sp;
    private JsonParser jParser = new JsonParser();
    private Punto ctp = null;

    private String sUser, sPass;
    private boolean bRecordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        pass2 = (EditText) findViewById(R.id.password2);
        pass2T = (TextView) findViewById(R.id.TextRepPass);
        mSubmit = (Button) findViewById(R.id.btnLogin);
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        btnRegAcc = (Button) findViewById(R.id.btnRegAcc);
        btnRegCan = (Button) findViewById(R.id.btnRegCan);
        cbRecordar = (CheckBox) findViewById(R.id.recordarContra);

        mSubmit.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
        btnRegAcc.setOnClickListener(this);
        btnRegCan.setOnClickListener(this);

        checkLogeo();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkLogeo() {
        bRecordar = sp.getBoolean("recordarContra", false);

        if (bRecordar) {
            cbRecordar.setChecked(true);
            sUser = sp.getString(TAG_USER, null);
            sPass = sp.getString(TAG_PASS, null);

            user.setText(sUser);
            pass.setText(sPass);

            Log.d(TAG, "Intentando loguear");
            new IntentoLogeo().execute();
        }
    }

    private void modoRegistro(boolean modo){
        if (modo){
            pass2.setVisibility(View.VISIBLE);
            pass2T.setVisibility(View.VISIBLE);
            btnRegAcc.setVisibility(View.VISIBLE);
            btnRegCan.setVisibility(View.VISIBLE);

            cbRecordar.setVisibility(View.GONE);
            btnRegistro.setVisibility(View.GONE);
            mSubmit.setVisibility(View.GONE);
        }else{
            pass2.setVisibility(View.GONE);
            pass2T.setVisibility(View.GONE);
            btnRegAcc.setVisibility(View.GONE);
            btnRegCan.setVisibility(View.GONE);

            cbRecordar.setVisibility(View.VISIBLE);
            btnRegistro.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                sUser = user.getText().toString();
                sPass = pass.getText().toString();
                bRecordar = cbRecordar.isChecked();
                new IntentoLogeo().execute();
                break;

            case R.id.btnRegistro:
                modoRegistro(true);
                break;

            case R.id.btnRegCan:
                modoRegistro(false);
                break;

            case R.id.btnRegAcc:
                if (user.getText().toString().trim().length() != 0){
                    if (pass.getText().toString().equals(pass2.getText().toString())){
                        sUser = user.getText().toString();
                        sPass = pass.getText().toString();
                        new IntentoRegistro().execute();
                    }else
                    Log.d(TAG, "onClick: 1");
                }else
                    Log.d(TAG, "onClick: 2");

            default:
                break;
        }
    }

    private void msgError(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    class IntentoLogeo extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logeando...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            if (isNetworkAvailable()) {

                // Recogida y envío de datos por http
                int success;

                try {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair(TAG_USER, sUser));
                    params.add(new BasicNameValuePair(TAG_PASS, sPass));

                    Log.d(TAG, "user:" + sUser + ", pass:" + sPass);

                    JSONObject json = jParser.peticionHttp(LOGIN_URL, "POST", params);

                    success = json.getInt(TAG_RESULTADO);
                    if (success == 1) {
                        Log.d(TAG, "Logeado correctamente! " + json.toString());

                        SharedPreferences.Editor edit = sp.edit();
                        if (bRecordar) {
                            // Guardar datos para la siguiente
                            edit.putBoolean("recordarContra", true);
                        } else {
                            sPass = "";
                        }
                        edit.putString(TAG_USER, sUser);
                        edit.putString(TAG_PASS, sPass);
                        edit.putString(TAG_ID_USER, json.getString(TAG_ID_USER));
                        edit.putString(TAG_NOMBRE, json.getString(TAG_NOMBRE));
                        edit.putString(TAG_APELLIDOS, json.getString(TAG_APELLIDOS));


                        Log.d(TAG, "añadida salida " + json.getBoolean("salida"));
                        if (json.getBoolean("salida")) {
                            LatLng ltg = new LatLng(json.getDouble("lat"), json.getDouble("lng"));
                            ctp = new Punto(json.getInt("id"), "salida", ltg);
                            edit.putString("lat", json.getString("lat"));
                            edit.putString("lng", json.getString("lng"));
                        } else {
                            edit.putString("lat", "");
                            edit.putString("lng", "");
                        }

                        edit.apply();

                        return true;
                    } else {
                        Log.d(TAG, "Fallo al logear! " + json.getString(TAG_RESUTLADO_DESCRIPCION));
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean msg) {

            super.onPostExecute(msg);
            if (msg) {
                new RecuperarDatos().execute();
            } else {
                pDialog.dismiss();
                msgError("Compruebe internet y la contraseña");
            }
        }
    }
    class RecuperarDatos extends AsyncTask<Void, Void, Boolean> {

        List<NameValuePair> params = new ArrayList<>();

        String URL = "http://overant.es/Andres/acciones.php";
        int CARGAR_PUNTOS = 1;
        int CARGAR_RUTAS = 2;
        int CARGAR_HISTORIAL = 3;

        String TAG_PUNTOS = "Puntos";
        String TAG_RUTAS = "Rutas";
        String TAG_HISTORIAL = "Historial";

        @Override
        protected void onPreExecute() {
            mc = MiConfig.get();
            pDialog.setMessage("Recuperando datos...");

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... args) {

            if (ctp != null) mc.setSalida(ctp);

            String ID = sp.getString(TAG_ID_USER, "0");
            params.add(new BasicNameValuePair("user", ID));

            JSONObject joPuntos = recogerDatosDe(URL, CARGAR_PUNTOS);
            JSONObject joRutas = recogerDatosDe(URL, CARGAR_RUTAS);
            JSONObject joHistorial = recogerDatosDe(URL, CARGAR_HISTORIAL);

            Log.d(TAG, "Cargando puntos");
            //Rellenando PUNTOS
            try {
                JSONArray mPuntos = joPuntos.getJSONArray(TAG_PUNTOS);
                for (int i = 0; i < mPuntos.length(); i++) {
                    JSONObject p = mPuntos.getJSONObject(i);

                    int id = p.getInt("ID");
                    String nombre = p.getString("nombre");
                    Double lat = p.getDouble("lat");
                    Double lng = p.getDouble("lng");
                    String des = p.getString("descripcion");
                    LatLng pos = new LatLng(lat, lng);

                    Punto ctP = new Punto(id, nombre, pos);
                    if (!des.equals("")) ctP.setDescripcion(des);
                    mc.addPunto(id, ctP);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Cargando rutas");
            // Rellenando RUTAS
            try {
                JSONArray mRutas = joRutas.getJSONArray(TAG_RUTAS);
                for (int i = 0; i < mRutas.length(); i++) {
                    JSONObject r = mRutas.getJSONObject(i);

                    int id = r.getInt("ID");
                    String titulo = r.getString("titulo");
                    JSONArray puntos = r.getJSONArray(TAG_PUNTOS);
                    ArrayList<Punto> listaCt = new ArrayList<>();
                    for (int j = 0; j < puntos.length(); j++) {
                        JSONObject rp = puntos.getJSONObject(j);
                        Punto pt;
                        pt = (mc.getPunto(rp.getInt("ID")));
                        Punto pt2 = new Punto(id, pt.getNombre(), pt.getPosicion());
                        pt2.setDescripcion(pt.getDescripcion());
                        if (rp.getString("tiempo") != null && rp.getInt("tiempo") != 0) {
                            pt2.setTiempoMedio(rp.getInt("tiempo"));
                        }
                        if (rp.getString("detalles") != null && !rp.getString("detalles").equals("null")) {
                            pt2.setComentarios(rp.getString("detalles"));
                            Log.d("Login", "comentarios encontrados");
                        }
                        listaCt.add(pt2);
                    }
                    Ruta ctR = new Ruta(titulo, 0, listaCt);
                    ctR.setID(id);
                    mc.addRuta(id, ctR);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Cargando historia");
            // Rellenando Historial
            try {
                JSONArray mHistorial = joHistorial.getJSONArray(TAG_HISTORIAL);
                for (int i = 0; i < mHistorial.length(); i++) {
                    JSONObject h = mHistorial.getJSONObject(i);

                    int id = h.getInt("ID");
                    int id_ruta = h.getInt("ID_ruta");
                    String fecha = h.getString("Fecha");
                    int distancia = h.getInt("distancia");
                    int tiempo = h.getInt("tiempo");

                    Historial ctH = new Historial(mc.getRuta(id_ruta), fecha, distancia, tiempo);
                    mc.addHistorial(id, ctH);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            pDialog.dismiss();
            super.onPostExecute(msg);
            if (msg) {

                Intent in = new Intent(Login.this, PantallaInicio.class);
                startActivity(in);
            }

        }

        private JSONObject recogerDatosDe(String url, int accion) {

            InputStream is = null;
            String json = "";
            JSONObject jObj = null;
            params.add(new BasicNameValuePair("accion", accion + ""));
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


    }

    class IntentoRegistro extends AsyncTask<Void, Void, Boolean>{
        String URL = "http://overant.es/Andres/acciones.php";
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Registrando...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            if (isNetworkAvailable()) {
                int success;
                try {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("accion", "8"));
                    params.add(new BasicNameValuePair("user", sUser));
                    params.add(new BasicNameValuePair("pass", sPass));

                    JSONObject json = jParser.peticionHttp(URL, "POST", params);

                    success = json.getInt(TAG_RESULTADO);
                    if (success == 1) {
                        Log.d(TAG, "Creado correctamente! " + json.toString());
                    }else{
                        return false;
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }else{
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean msg) {
            super.onPostExecute(msg);
            pDialog.dismiss();
            if (msg) {
                modoRegistro(false);
                msgError("Usuario creado correctamente");
            } else {
                msgError("ERROR\nUsuario ya existe");
            }
        }
    }
}

