package asako.clase.rutas.UI;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import asako.clase.rutas.R;
import asako.clase.rutas.Tools.MiConfig;

public class FragmentoPerfil extends Fragment {
    private SharedPreferences sp;
    private TextView tNombre, tUsuario, tSalida;

    public FragmentoPerfil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_perfil, container, false);
        setHasOptionsMenu(true);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        MiConfig mc = MiConfig.getConfig();

        tNombre = (TextView)v.findViewById(R.id.texto_nombre);
        tUsuario = (TextView)v.findViewById(R.id.texto_email);
        tSalida = (TextView)v.findViewById(R.id.texto_salida);

        tNombre.setText(sp.getString("nombre", "Dummy") + " " + sp.getString("apellidos", "Dummy"));
        tUsuario.setText(sp.getString("username", "Dummy"));
        String salida = "Sin punto de salida";
        if (mc.isSalidaSet()){
            try {
                salida = mc.getSalida().getNomPosicion(getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            tSalida.setText(salida);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
