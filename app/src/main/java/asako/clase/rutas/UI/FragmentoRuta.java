package asako.clase.rutas.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.AdaptadorRuta;
import asako.clase.rutas.Tools.DecoracionLineaDivisoria;

public class FragmentoRuta extends Fragment {

    private Historial ht;
    private LinearLayoutManager linearLayout;
    private ActionBar appBar;
    private DrawerLayout mDrawer;
    //private AppBarLayout appBarLayout;

    public FragmentoRuta() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_ruta, container, false);
        setHasOptionsMenu(true);

        appBar = ((PantallaInicio) getActivity()).getSupportActionBar();
        appBar.setHomeAsUpIndicator(R.drawable.back);

        mDrawer = (DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ht = (Historial) getArguments().get("historial");

        RecyclerView reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        AdaptadorRuta adaptador = new AdaptadorRuta(ht.getRuta());
        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));

        int Idist = ht.getDistancia() / 1000;
        int Idist2 = ht.getDistancia() % 1000;
        String Sdist = Idist + "," + Idist2 + " Kms";

        int hours = ht.getTiempo() / 3600;
        int minutes = (ht.getTiempo() % 3600) / 60;
        String time = hours + ":" + minutes + "h";

        ((TextView) v.findViewById(R.id.titulo_historial)).setText(ht.getRuta().getTitulo());
        ((TextView) v.findViewById(R.id.texto_fecha)).setText(ht.getFecha());
        ((TextView) v.findViewById(R.id.texto_distancia)).setText(Sdist);
        ((TextView) v.findViewById(R.id.texto_tiempo)).setText(time);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
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
        //if(appBarLayout != null)appBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_borrar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
