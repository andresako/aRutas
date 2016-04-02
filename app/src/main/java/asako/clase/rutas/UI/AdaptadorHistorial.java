package asako.clase.rutas.UI;

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

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder>{

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

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public AdaptadorHistorial(){
    }

    public AdaptadorHistorial(List<Historial> listaHistoriales) {
        this.listaHistoriales = listaHistoriales;
    }
    public void AddNewViaje(Historial ht){
        listaHistoriales.add(0,ht);
        notifyItemInserted(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_lista, parent, false);
        return new ViewHolder(v);
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
