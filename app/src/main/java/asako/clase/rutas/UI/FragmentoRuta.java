package asako.clase.rutas.UI;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
    private AppBarLayout appBar;
    private DrawerLayout mDrawer;

    public FragmentoRuta() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_ruta, container, false);

        View padre = (View) container.getParent();
        appBar = (AppBarLayout) padre.findViewById(R.id.appbar);
        appBar.setVisibility(View.GONE);

        mDrawer = (DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ht = (Historial) getArguments().get("historial");

        RecyclerView reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        AdaptadorRuta adaptador = new AdaptadorRuta(ht.getRuta());
        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));

        int Idist = ht.getRuta().getDistancia() / 1000;
        int Idist2 = ht.getRuta().getDistancia() % 1000;
        String Sdist = Idist + "," + Idist2 + " Kms";

        int hours = ht.getRuta().getTiempo() / 60;
        int minutes = ht.getRuta().getTiempo() % 60;
        String time = hours + ":" + minutes + "h";

        ((TextView) v.findViewById(R.id.titulo_historial)).setText(ht.getRuta().getTitulo());
        ((TextView) v.findViewById(R.id.texto_fecha)).setText(ht.getFecha());
        ((TextView) v.findViewById(R.id.texto_distancia)).setText(Sdist);
        ((TextView) v.findViewById(R.id.texto_tiempo)).setText(time);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.setVisibility(View.VISIBLE);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
