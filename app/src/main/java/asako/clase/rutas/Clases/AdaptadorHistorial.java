package asako.clase.rutas.Clases;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.R;
import asako.clase.rutas.UI.FragmentoRuta;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder> {

    private List<Historial> listaHistoriales = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo;
        public TextView fecha;
        public ImageButton boton;
        public int pos;

        public ViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.texto_titulo);
            fecha = (TextView) v.findViewById(R.id.texto_fecha);
            boton = (ImageButton) v.findViewById(R.id.icono_indicador_derecho);
        }
    }

    public AdaptadorHistorial() {
    }

    public AdaptadorHistorial(List<Historial> listaHistoriales) {
        this.listaHistoriales = listaHistoriales;
    }

    public void AddNewViaje(Historial ht) {
        listaHistoriales.add(0, ht);
        notifyItemInserted(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        ViewHolder vh = new ViewHolder(v);

        Bundle args = new Bundle();
        args.putSerializable("historial", listaHistoriales.get(vh.pos));
        final Fragment fg = new FragmentoRuta();
        fg.setArguments(args);
        vh.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)parent.getContext())
                        .getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor_principal, fg)
                        .commit();
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Historial item = listaHistoriales.get(position);
        holder.titulo.setText(item.getRuta().getTitulo());
        holder.fecha.setText(item.getFecha());
        holder.pos = position;
    }

    @Override
    public int getItemCount() {
        return listaHistoriales.size();
    }
}
