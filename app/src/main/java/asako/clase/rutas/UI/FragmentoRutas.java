package asako.clase.rutas.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import asako.clase.rutas.R;
import asako.clase.rutas.Tools.AdaptadorRutas;
import asako.clase.rutas.Tools.MiConfig;

public class FragmentoRutas extends Fragment {


    public FragmentoRutas() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_rutas_lista, container, false);
        setHasOptionsMenu(true);

        PantallaInicio pa = (PantallaInicio)super.getActivity();
        MiConfig datos = pa.datos;

        AdaptadorRutas rutaAdapter = new AdaptadorRutas(v.getContext(),datos);
        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.rutasLista);
        elv.setAdapter(rutaAdapter);

        return v;
    }

}
