package asako.clase.rutas.Tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.R;
import asako.clase.rutas.UI.FragmentoRuta;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder> {

    private FragmentManager fragmentManager;
    private List<Historial> listaHistoriales = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo;
        public TextView fecha;
        public ImageButton boton;

        public ViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.texto_titulo);
            fecha = (TextView) v.findViewById(R.id.texto_fecha);
            boton = (ImageButton) v.findViewById(R.id.icono_indicador_derecho);
        }
    }

    public AdaptadorHistorial(List<Historial> listaHistoriales, FragmentManager FM) {
        this.listaHistoriales = listaHistoriales;
        this.fragmentManager = FM;
    }

    public void AddNewSalida(Historial ht) {
        listaHistoriales.add(0, ht);
        notifyItemInserted(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        final ViewHolder vh = new ViewHolder(v);

        vh.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putSerializable("historial", listaHistoriales.get(vh.getAdapterPosition()));
                Fragment fg = new FragmentoRuta();
                fg.setArguments(args);

                FragmentTransaction fT = fragmentManager.beginTransaction();
                fT.add(R.id.contenedor_principal, fg);
                fT.addToBackStack(null);
                fT.commit();

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Historial item = listaHistoriales.get(position);
        holder.titulo.setText(item.getRuta().getTitulo());
        holder.fecha.setText(item.getFecha());

    }

    @Override
    public int getItemCount() {
        return listaHistoriales.size();
    }
}
