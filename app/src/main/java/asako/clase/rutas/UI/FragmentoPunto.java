package asako.clase.rutas.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.R;

public class FragmentoPunto extends Fragment implements View.OnClickListener {

    private TextView nombre;
    private TextView direccion;
    private TextView tiempo;
    private TextView detalles;

    private final FragmentManager fragmentManager;
    private Punto punto;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBar appBar;

    public boolean editado = false;

    public FragmentoPunto() {
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_punto, container, false);
        setHasOptionsMenu(true);

        punto = (Punto) getArguments().get("punto");

        // Textos editables
        nombre = (TextView) v.findViewById(R.id.texto_nombre);
        direccion = (TextView) v.findViewById(R.id.texto_direccion);
        tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
        detalles = (TextView) v.findViewById(R.id.texto_detalles);
        nombre.setOnClickListener(this);
        direccion.setOnClickListener(this);
        tiempo.setOnClickListener(this);
        detalles.setOnClickListener(this);

        //poder hacer click en los demás items tambien
        v.findViewById(R.id.texto_nombreT).setOnClickListener(this);
        v.findViewById(R.id.texto_direccionT).setOnClickListener(this);
        v.findViewById(R.id.texto_tiempoT).setOnClickListener(this);
        v.findViewById(R.id.texto_detallesT).setOnClickListener(this);
        v.findViewById(R.id.texto_nombreI).setOnClickListener(this);
        v.findViewById(R.id.texto_direccionI).setOnClickListener(this);
        v.findViewById(R.id.texto_tiempoI).setOnClickListener(this);
        v.findViewById(R.id.texto_detallesI).setOnClickListener(this);

        //Rellenando datos
        if (!punto.getNombre().equals("")) {
            nombre.setText(punto.getNombre());
            direccion.setText(punto.getPosicion().toString());
            tiempo.setText(punto.getTiempoMedio() + " minutos");
            detalles.setText(punto.getDetalles());
        } else {
            nombre.setHint("Introduce nombre del nuevo punto");
            direccion.setHint("Introduce direccion");
            tiempo.setHint("Tiempo empleado?");
            detalles.setHint("Algun detalle más?");
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
                break;
            case R.id.texto_direccion:
            case R.id.texto_direccionI:
            case R.id.texto_direccionT:
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                tvct = direccion;
                break;
            case R.id.texto_tiempo:
            case R.id.texto_tiempoI:
            case R.id.texto_tiempoT:
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.setSingleLine(true);
                tvct = tiempo;
                break;
            case R.id.texto_detalles:
            case R.id.texto_detallesI:
            case R.id.texto_detallesT:
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                tvct = detalles;
                break;
        }

        alert.setView(input);
        final TextView finalTvct = tvct;
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().toString().trim().length() != 0) {
                    System.out.println(input.getText());
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

    public void guardarDatos() {
        if (punto.getNombre().equals("")) {          //Nuevo punto

        } else {                                      //Modificacion

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_borrar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
