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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.JsonParser;
import asako.clase.rutas.Tools.MiConfig;

public class PantallaNuevaRuta extends AppCompatActivity {

    private MiConfig MC;

    private ArrayList<String> listaPuntos;

    private TextView fecha, distancia, tiempo;
    private Button botonAdd, botonGuardar;
    private ListView lv;
    private Ruta newRuta = new Ruta(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ruta);

        MC = MiConfig.get();
        listaPuntos = new ArrayList<>();

        fecha = (TextView) findViewById(R.id.salidaFecha);
        distancia = (TextView) findViewById(R.id.salidaDistancia);
        tiempo = (TextView) findViewById(R.id.salidaTiempo);
        lv = (ListView) findViewById(R.id.salidaLista);
        botonAdd = (Button) findViewById(R.id.salidaAddBoton);
        botonGuardar = (Button) findViewById(R.id.salidaGuardarBoton);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yy");
        String currentDateandTime = sdf.format(new Date());
        fecha.setText(currentDateandTime);
        distancia.setText("0 Kms");
        tiempo.setText("0:00h");

        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, listaPuntos));

        botonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayAdapter<String> adp = new ArrayAdapter<>(PantallaNuevaRuta.this,
                        android.R.layout.simple_spinner_item, MC.getNombrePuntos());
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final TextView tv = new TextView(PantallaNuevaRuta.this);
                tv.setText("Seleccione el punto:");

                final Spinner sp = new Spinner(PantallaNuevaRuta.this);
                sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                adp.add("---Vacio---");
                sp.setAdapter(adp);
                sp.setSelection(adp.getCount() - 1);

                final TextView tv2 = new TextView(PantallaNuevaRuta.this);
                tv2.setText("Tiempo estimado?");

                final EditText et = new EditText(PantallaNuevaRuta.this);
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                et.setHint("Minutos");
                et.setSingleLine(true);

                LinearLayout ll = new LinearLayout(PantallaNuevaRuta.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(tv);
                ll.addView(sp);
                ll.addView(tv2);
                ll.addView(et);

                AlertDialog.Builder builder = new AlertDialog.Builder(PantallaNuevaRuta.this);
                builder.setView(ll);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Aceptado.
                        if (!sp.getSelectedItem().toString().equals("---Vacio---")) {
                            System.out.println(sp.getSelectedItemPosition() + " Seleccionado.");
                            int tiempom = 0;
                            if (!"".equals(et.getText().toString()))
                                tiempom = Integer.valueOf(et.getText().toString());

                            ArrayAdapter<String> adp2 = (ArrayAdapter<String>) lv.getAdapter();

                            listaPuntos.add(sp.getSelectedItem().toString() + ", " + tiempom + " min.");
                            Punto ctp1 = MC.getListaPuntos().get(sp.getSelectedItemPosition());
                            Punto ctp2 = new Punto(ctp1.getID(), ctp1.getNombre(), ctp1.getPosicion());
                            ctp2.setTiempoMedio(tiempom);

                            newRuta.addLugar(ctp2);
                            newRuta.addTiempo(tiempom);
                            tiempo.setText(newRuta.getTiempo() + " min");
                            adp2.notifyDataSetChanged();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelado.

                    }
                });
                builder.create().show();
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
