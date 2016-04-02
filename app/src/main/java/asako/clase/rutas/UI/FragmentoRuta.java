package asako.clase.rutas.UI;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asako.clase.rutas.Clases.AdaptadorRuta;
import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.R;

public class FragmentoRuta extends Fragment {

    private Historial ht;
    private LinearLayoutManager linearLayout;

    public FragmentoRuta() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_ruta, container, false);
        ht = (Historial) getArguments().get("historial");

        RecyclerView reciclador = (RecyclerView)v.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        AdaptadorRuta adaptador = new AdaptadorRuta(ht.getRuta());
        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));

        ((TextView)v.findViewById(R.id.titulo_historial)).setText(ht.getRuta().getTitulo());
        ((TextView)v.findViewById(R.id.texto_fecha)).setText(ht.getFecha());
        ((TextView)v.findViewById(R.id.texto_distancia)).setText(ht.getRuta().getDistancia()+ " metros");
        ((TextView)v.findViewById(R.id.texto_tiempo)).setText(ht.getRuta().getTiempo() + " minutos");

        return v;
    }



}