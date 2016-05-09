package asako.clase.rutas.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.JsonParser;
import asako.clase.rutas.Tools.MiConfig;

public class PantallaNuevaRuta extends AppCompatActivity {

    private MiConfig MC;

    private ArrayList<String> listaPuntos;

    private Button botonAdd, botonGuardar;
    private ListView lv;
    private Ruta newRuta = new Ruta(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ruta);

        MC = MiConfig.get();
        listaPuntos = new ArrayList<>();

        lv = (ListView) findViewById(R.id.salidaLista);
        botonAdd = (Button) findViewById(R.id.salidaAddBoton);
        botonGuardar = (Button) findViewById(R.id.salidaGuardarBoton);


        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, listaPuntos));

        botonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PantallaNuevaRuta.this);

                final ArrayAdapter<String> adp = new ArrayAdapter<>(PantallaNuevaRuta.this,
                        android.R.layout.simple_list_item_1, MC.getNombrePuntos());

                final TextView tv = new TextView(PantallaNuevaRuta.this);
                tv.setText("Seleccione el punto:");

                final ListView sp = new ListView(PantallaNuevaRuta.this);
                sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                sp.setAdapter(adp);

                LinearLayout ll = new LinearLayout(PantallaNuevaRuta.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(tv);
                ll.addView(sp);

                builder.setView(ll);

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelado.
                    }
                });
                final AlertDialog alert = builder.create();
                sp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(position + " Seleccionado.");

                        ArrayAdapter<String> adp2 = (ArrayAdapter<String>) lv.getAdapter();

                        listaPuntos.add(sp.getItemAtPosition(position).toString());
                        Punto ctp1 = MC.getListaPuntos().get(position);
                        Punto ctp2 = new Punto(ctp1.getID(), ctp1.getNombre(), ctp1.getPosicion());

                        newRuta.addLugar(ctp2);
                        adp2.notifyDataSetChanged();
                        alert.dismiss();
                    }
                });

                alert.show();
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listaPuntos.isEmpty()) {

                    final TextView tv = new TextView(PantallaNuevaRuta.this);
                    tv.setText("Pongale nombre a esta ruta");

                    final EditText et = new EditText(PantallaNuevaRuta.this);
                    et.setHint("Nombre de ruta");
                    et.setSingleLine(true);

                    LinearLayout ll = new LinearLayout(PantallaNuevaRuta.this);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(tv);
                    ll.addView(et);

                    AlertDialog.Builder builder = new AlertDialog.Builder(PantallaNuevaRuta.this);
                    builder.setView(ll);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Aceptado.
                            if (!"".equals(et.getText().toString())) {
                                newRuta.setTitulo(et.getText().toString());
                                finalizado(1);
                            }
                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Cancelado.
                            finalizado(0);
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    private void finalizado(int res) {
        Intent returnIntent = new Intent();
        switch (res) {
            case 0:             //Cancelado
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
            case 1:
                returnIntent.putExtra("result", newRuta);
                setResult(Activity.RESULT_OK, returnIntent);

                new GuardarRuta().execute();
                break;
        }
    }

    class GuardarRuta extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            String url = "http://overant.es/Andres/rutaNueva.php";

            JSONObject obj;
            JsonParser jsonParser = new JsonParser();
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(PantallaNuevaRuta.this);
            obj = newRuta.toJsonObject();

            Log.d("request!", "starting");

            try {
                //Posting user data to script
                nameValuePairs.add(new BasicNameValuePair("user", sp.getString("id_user", "0")));
                nameValuePairs.add(new BasicNameValuePair("json", obj.toString()));

                JSONObject json = jsonParser.peticionHttp(url, "POST", nameValuePairs);

                // full json response
                Log.d("Post Comment attempt", json.toString());

                // json success element
                int success;
                success = json.getInt("Resultado");
                if (success == 1) {
                    Log.d("Ruta guardada!", json.toString());
                    MC.addRuta(json.getInt("idRuta"), newRuta);
                    finish();
                    return json.getString("Desc");
                } else {
                    Log.d("Fallo al guardar!", json.getString("Desc"));
                    return json.getString("Desc");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String resp) {
            if (resp != null && !resp.equals("OK")) {
                Toast.makeText(PantallaNuevaRuta.this, resp, Toast.LENGTH_LONG).show();
            } else if (resp.equals("OK")) {
                Toast.makeText(PantallaNuevaRuta.this, "Guardado satisfactoriamente", Toast.LENGTH_LONG).show();

                finish();
            }
        }
    }
}
