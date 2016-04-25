package asako.clase.rutas.Tools;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.R;

public class AdaptadorRuta extends RecyclerView.Adapter<AdaptadorRuta.ViewHolder> {

    private Ruta ruta;

    public AdaptadorRuta(Ruta ruta) {
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
        if (punto.getTiempoMedio() != 0) {
            holder.tiempo.setText(punto.getTiempoMedio() + "minutos");
            holder.tiempo.setVisibility(View.VISIBLE);
            holder.tiempoF.setVisibility(View.VISIBLE);
        }
        holder.detalle.setText(punto.getDetalles());
    }

    @Override
    public int getItemCount() {
        return ruta.getListaLugaresVisitados().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public TextView direccion;
        public TextView tiempo;
        public ImageView tiempoF;
        public TextView detalle;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.texto_nombre);
            direccion = (TextView) v.findViewById(R.id.texto_direccion);
            tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
            detalle = (TextView) v.findViewById(R.id.texto_detalles);
            tiempoF = (ImageView) v.findViewById(R.id.imageView3);
        }
    }
}
