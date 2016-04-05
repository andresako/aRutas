package asako.clase.rutas.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asako.clase.rutas.Clases.AdaptadorPunto;
import asako.clase.rutas.Datos.MiConfig;
import asako.clase.rutas.R;

public class FragmentoPuntos extends Fragment {


    private LinearLayoutManager linearLayout;

    public FragmentoPuntos() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_puntos, container, false);

        RecyclerView reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        AdaptadorPunto adaptador = new AdaptadorPunto(MiConfig.LISTA_PUNTOS, getFragmentManager(),true);

        reciclador.setAdapter(adaptador);

        return view;
    }

}
