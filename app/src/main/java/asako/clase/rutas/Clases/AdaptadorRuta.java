package asako.clase.rutas.Clases;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asako.clase.rutas.R;

public class AdaptadorRuta extends RecyclerView.Adapter<AdaptadorRuta.ViewHolder>{

    private Ruta ruta;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public TextView direccion;
        public TextView tiempo;
        public TextView detalle;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.texto_nombre);
            direccion = (TextView) v.findViewById(R.id.texto_direccion);
            tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
            detalle = (TextView) v.findViewById(R.id.texto_detalles);
        }
    }

    public AdaptadorRuta(Ruta ruta){
        this.ruta = ruta;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Punto punto = ruta.getListaLugaresVisitados().get(position);
        holder.nombre.setText(punto.getNombre());
        holder.direccion.setText(punto.getPosicion().toString());
        holder.tiempo.setText(punto.getTiempoMedio() + "minutos");
        holder.detalle.setText(punto.getDetalles());
    }

    @Override
    public int getItemCount() {
        return ruta.getListaLugaresVisitados().size();
    }
}
