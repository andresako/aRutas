package asako.clase.rutas.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.R;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder>{

    private List<Historial> listaHistoriales = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView fecha;
        public TextView puntos_visitados;
        public TextView tiempo;
        public TextView distancia;

        public ViewHolder(View v) {
            super(v);
            fecha = (TextView) v.findViewById(R.id.texto_fecha);
            puntos_visitados = (TextView) v.findViewById(R.id.texto_puntos_visitados);
            tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
            distancia = (TextView) v.findViewById(R.id.texto_distancia);
        }
    }

    public AdaptadorHistorial(){
    }

    public AdaptadorHistorial(List<Historial> listaHistoriales) {
        this.listaHistoriales = listaHistoriales;
    }
    public void AddNewPunto(Historial ht){
        listaHistoriales.add(0,ht);
        notifyItemInserted(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_historial, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Historial item = listaHistoriales.get(position);
        holder.fecha.setText(item.getFecha());
        holder.puntos_visitados.setText(item.getListaLugaresVisitados().size());
        holder.tiempo.setText(item.getTiempo());
        holder.distancia.setText(item.getDistancia());
    }

    @Override
    public int getItemCount() {
        return listaHistoriales.size();
    }
}
