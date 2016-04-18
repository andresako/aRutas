package asako.clase.rutas.UI;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import asako.clase.rutas.Tools.AdaptadorPunto;
import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Tools.MiConfig;
import asako.clase.rutas.R;

public class FragmentoPuntos extends Fragment {


    private LinearLayoutManager linearLayout;
    private FloatingActionButton fabPunto;
    private MiConfig datos;

    public FragmentoPuntos() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_puntos, container, false);
        setHasOptionsMenu(true);

        PantallaInicio pa = (PantallaInicio)getActivity();
        datos = pa.datos;

        RecyclerView reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        fabPunto = (FloatingActionButton) view.findViewById(R.id.addPunto);
        fabPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Punto ctPnt = new Punto();
                Bundle args = new Bundle();
                args.putSerializable("punto", ctPnt);
                Fragment fg = new FragmentoPunto();
                fg.setArguments(args);

                FragmentTransaction fT = getActivity().getSupportFragmentManager().beginTransaction();
                fT.replace(R.id.contenedor_principal, fg,"puntoActivo");
                fT.addToBackStack(null);
                fT.commit();
            }
        });

        AdaptadorPunto adaptador = new AdaptadorPunto(new ArrayList<>(datos.HASH_PUNTOS.values()), getFragmentManager(),true);

        reciclador.setAdapter(adaptador);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
