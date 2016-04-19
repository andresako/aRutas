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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Punto;
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
    public static MiConfig mc;
    private EditText user, pass;
    private Button mSubmit;
    private CheckBox cbRecordar;
    private ProgressDialog pDialog;
    private SharedPreferences sp;
    private JsonParser jParser = new JsonParser();
    private Intent in = null;
    private Punto ctp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        mSubmit = (Button) findViewById(R.id.btnLogin);
        cbRecordar = (CheckBox) findViewById(R.id.recordarContra);

        mSubmit.setOnClickListener(this);

        checkLogeo();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void checkLogeo() {
        if (sp.getBoolean("recordarContra", false)) {
            cbRecordar.setChecked(true);
            user.setText(sp.getString(TAG_USER, null));
            pass.setText(sp.getString(TAG_PASS, null));

            new IntentoLogeo().execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                new IntentoLogeo().execute();
                break;

            default:
                break;
        }
    }

    class IntentoLogeo extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logeando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... args) {
            if (isNetworkAvailable()) {

                // Recogida y envío de datos por http
                int success;
                String username = user.getText().toString();
                String password = pass.getText().toString();
                try {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair(TAG_USER, username));
                    params.add(new BasicNameValuePair(TAG_PASS, password));

                    Log.d("PeticionPost!", "user:" + username + ", pass:" + password);

                    JSONObject json = jParser.peticionHttp(LOGIN_URL, "POST", params);

                    success = json.getInt(TAG_RESULTADO);
                    if (success == 1) {
                        Log.d("Logeado correctamente!", json.toString());

                        SharedPreferences.Editor edit = sp.edit();
                        if (cbRecordar.isChecked()) {
                            // Guardar datos para la siguiente
                            edit.putBoolean("recordarContra", true);
                        } else {
                            username = "";
                            password = "";
                        }
                        edit.putString(TAG_USER, username);
                        edit.putString(TAG_PASS, password);
                        edit.putString(TAG_ID_USER, json.getString(TAG_ID_USER));
                        edit.putString(TAG_NOMBRE, json.getString(TAG_NOMBRE));
                        edit.putString(TAG_APELLIDOS, json.getString(TAG_APELLIDOS));

                        if (json.getBoolean("salida")) {
                            LatLng ltg = new LatLng(json.getDouble("lat"), json.getDouble("lng"));
                            ctp = new Punto(json.getInt("id"), "salida", ltg);
                        } else {
                            edit.putString("lat", "");
                            edit.putString("lng", "");
                        }

                        edit.apply();

                        in = new Intent(Login.this, PantallaInicio.class);

                        mc = new MiConfig(getApplicationContext());
                        if (ctp != null) mc.setSalida(ctp);

                        mc.setConfig(mc);

                        return true;
                    } else {
                        Log.d("Fallo al logear!", json.getString(TAG_RESUTLADO_DESCRIPCION));
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        protected void onPostExecute(Boolean msg) {

            if (msg) {
                startActivity(in);
            }
            super.onPostExecute(msg);
            pDialog.dismiss();
        }
    }
}
