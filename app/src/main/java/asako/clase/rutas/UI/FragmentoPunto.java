package asako.clase.rutas.UI;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.R;

public class FragmentoPunto extends Fragment {

    private TextView nombre;
    private TextView direccion;
    private TextView tiempo;
    private TextView detalles;

    private TextView nombreT;
    private TextView direccionT;
    private TextView tiempoT;
    private TextView detallesT;

    private ImageView nombreI;
    private ImageView direccionI;
    private ImageView tiempoI;
    private ImageView detallesI;

    private final FragmentManager fragmentManager;
    private Punto punto;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private AppBarLayout appBar;

    public FragmentoPunto() {
        fragmentManager = getFragmentManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_punto, container, false);
        punto = (Punto) getArguments().get("punto");

        // Textos editables
        nombre = (TextView) v.findViewById(R.id.texto_nombre);
        direccion = (TextView) v.findViewById(R.id.texto_direccion);
        tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
        detalles = (TextView) v.findViewById(R.id.texto_detalles);

        //poder hacer click en los demás items tambien
        nombreT = (TextView) v.findViewById(R.id.texto_nombreT);
        direccionT = (TextView) v.findViewById(R.id.texto_direccionT);
        tiempoT = (TextView) v.findViewById(R.id.texto_tiempoT);
        detallesT = (TextView) v.findViewById(R.id.texto_detallesT);
        nombreI = (ImageView) v.findViewById(R.id.texto_nombreI);
        direccionI = (ImageView) v.findViewById(R.id.texto_direccionI);
        tiempoI = (ImageView) v.findViewById(R.id.texto_tiempoI);
        detallesI = (ImageView) v.findViewById(R.id.texto_detallesI);

        //Rellenando datos
        nombre.setText(punto.getNombre());
        direccion.setText(punto.getPosicion().toString());
        tiempo.setText(punto.getTiempoMedio() + " minutos");
        detalles.setText(punto.getDetalles());

        toolbar = (Toolbar) this.getActivity().findViewById(R.id.toolbar);
        appBar = (AppBarLayout) this.getActivity().findViewById(R.id.appbar);
        mDrawer = (DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout);

        //// TODO: 05/04/2016 TEMRINAR ESTA PARTE!
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
