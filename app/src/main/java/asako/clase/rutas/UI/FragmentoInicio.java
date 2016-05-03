package asako.clase.rutas.UI;

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
import android.widget.ImageButton;

import asako.clase.rutas.Tools.AdaptadorHistorial;
import asako.clase.rutas.Tools.MiConfig;
import asako.clase.rutas.R;

public class FragmentoInicio extends Fragment {

    private FragmentManager fragmentManager;

    public FragmentoInicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PantallaInicio pa = (PantallaInicio)getActivity();
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

                //Bundle args = new Bundle();
                //args.putSerializable("historial", listaHistoriales.get(vh.getAdapterPosition()));
                Fragment fg = new FragmentoSalida();
                //fg.setArguments(args);

                FragmentTransaction fT = fragmentManager.beginTransaction();
                fT.replace(R.id.contenedor_principal, fg);
                fT.addToBackStack(null);
                fT.commit();
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
