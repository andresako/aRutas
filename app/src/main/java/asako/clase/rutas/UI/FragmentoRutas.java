package asako.clase.rutas.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;
import asako.clase.rutas.Tools.AdaptadorRutas;
import asako.clase.rutas.Tools.MiConfig;

public class FragmentoRutas extends Fragment {

    private MiConfig MC;

    private ExpandableListView elv;
    private FloatingActionButton fab;

    public FragmentoRutas() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_rutas_lista, container, false);
        setHasOptionsMenu(true);

        PantallaInicio pa = (PantallaInicio)super.getActivity();
        MC = pa.datos;

        AdaptadorRutas rutaAdapter = new AdaptadorRutas(v.getContext(),MC);
        elv = (ExpandableListView) v.findViewById(R.id.rutasLista);
        elv.setAdapter(rutaAdapter);
        elv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("Are you sure you want to delete this?");
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdaptadorRutas yourAdapter = (AdaptadorRutas) ((ExpandableListView)parent).getExpandableListAdapter();
                        Ruta r = (Ruta) yourAdapter.getGroup(position);
                        yourAdapter.remove(position);
                        borrarRuta(r);
                        yourAdapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();

                return true;
            }
        });

        fab = (FloatingActionButton) v.findViewById(R.id.addPunto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PantallaNuevaRuta.class);
                startActivityForResult(i, 111);
            }
        });

        return v;
    }

    private void borrarRuta(Ruta r) {
        Log.d("Borrando ruta:", r.getID()+"");
        MC.HASH_RUTAS.remove(r.getID());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 111) {
            if(resultCode == Activity.RESULT_OK){
                Ruta result= data.getParcelableExtra("result");
                AdaptadorRutas AR = (AdaptadorRutas) elv.getExpandableListAdapter();
                AR.addRuta(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
