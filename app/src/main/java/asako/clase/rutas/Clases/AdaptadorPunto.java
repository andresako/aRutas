package asako.clase.rutas.Clases;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import asako.clase.rutas.R;

public class AdaptadorPunto extends RecyclerView.Adapter<AdaptadorPunto.ViewHolder>{

    private final List<Lugar> listaLugares;

    public AdaptadorPunto(List<Lugar> listaLugares){
        this.listaLugares = listaLugares;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView posicion;
        public TextView tiempo;
        public TextView detalles;
        public ImageView imgDetalles;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.texto_nombre);
            posicion = (TextView) v.findViewById(R.id.texto_direccion);
            tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
            detalles = (TextView) v.findViewById(R.id.texto_detalles);
            imgDetalles = (ImageView) v.findViewById(R.id.imageView4);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta, parent, false);
        final ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Lugar lugar = listaLugares.get(position);
        holder.nombre.setText(lugar.getNombre());
        holder.tiempo.setText(lugar.getTiempoMedio()+" min");
        holder.posicion.setText(lugar.getPosicion().toString());

        if (!lugar.getDetalles().equals("")){
            holder.detalles.setText(lugar.getDetalles());
        }else{
            holder.detalles.setVisibility(View.GONE);
            holder.imgDetalles.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaLugares.size();
    }
}
