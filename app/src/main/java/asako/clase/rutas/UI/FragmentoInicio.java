package asako.clase.rutas.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import asako.clase.rutas.Tools.AdaptadorHistorial;
import asako.clase.rutas.Tools.MiConfig;
import asako.clase.rutas.R;

public class FragmentoInicio extends Fragment {

    private FragmentManager fragmentManager;

    public FragmentoInicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final PantallaInicio pa = (PantallaInicio)getActivity();
        MiConfig datos = pa.datos;

        View v = inflater.inflate(R.layout.fragmento_inicio, container, false);
        setHasOptionsMenu(true);

        fragmentManager = pa.getSupportFragmentManager();

        ImageButton btnSalida = (ImageButton) v.findViewById(R.id.btnNewSalida);

        RecyclerView reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        AdaptadorHistorial adaptador = new AdaptadorHistorial(datos.getHistorial(), getFragmentManager());
        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));

        btnSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(pa);

                final ArrayAdapter<String> adp = new ArrayAdapter<>(pa,
                        android.R.layout.simple_list_item_1, pa.datos.getNombreRutas());

                final TextView tv = new TextView(pa);
                tv.setText("Seleccione la ruta:");

                final ListView sp = new ListView(pa);
                sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                sp.setAdapter(adp);

                LinearLayout ll = new LinearLayout(pa);
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
//
                        Bundle args = new Bundle();
                        args.putParcelable("ruta", pa.datos.getListaRutas().get(position));
                        Fragment fg = new FragmentoSalida();
                        fg.setArguments(args);

                        FragmentTransaction fT = fragmentManager.beginTransaction();
                        fT.replace(R.id.contenedor_principal, fg,"salidaActiva");
                        fT.addToBackStack(null);
                        fT.commit();

                        alert.dismiss();
                    }
                });

                alert.show();

            }
        });



        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


}
