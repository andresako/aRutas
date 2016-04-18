package asako.clase.rutas.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import asako.clase.rutas.Tools.AdaptadorHistorial;
import asako.clase.rutas.Tools.MiConfig;
import asako.clase.rutas.R;

public class FragmentoHistorial extends Fragment {

    private LinearLayoutManager linearLayout;

    public FragmentoHistorial() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_historial, container, false);
        PantallaInicio pa = (PantallaInicio)getActivity();
        MiConfig datos = pa.datos;

        setHasOptionsMenu(true);

        RecyclerView reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        AdaptadorHistorial adaptador = new AdaptadorHistorial(new ArrayList<>(datos.HASH_HISTORIAL.values()), getFragmentManager());
        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

}
