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

public class FragmentoInicio extends Fragment {

    public FragmentoInicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PantallaInicio pa = (PantallaInicio)getActivity();
        MiConfig datos = pa.datos;

        View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        setHasOptionsMenu(true);

        RecyclerView reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        AdaptadorHistorial adaptador = new AdaptadorHistorial(datos.getHistorial(), getFragmentManager());
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
