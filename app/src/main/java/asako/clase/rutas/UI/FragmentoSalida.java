package asako.clase.rutas.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asako.clase.rutas.R;

public class FragmentoSalida extends Fragment {


    public FragmentoSalida() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_salida, container, false);


        return v;
    }

}
