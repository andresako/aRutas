package asako.clase.rutas.Tools;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.R;

public class AdaptadorSalida extends RecyclerView.Adapter<AdaptadorSalida.ViewHolder>{

    private final ArrayList<Punto> listaPuntos;

    public AdaptadorSalida(ArrayList<Punto> listaPuntos){
        this.listaPuntos = listaPuntos;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_punto_salida, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Punto p = listaPuntos.get(position);
        Log.d("AdpSalida", "time: "+ p.getTiempoMedio());
        holder.titulo.setText(p.getNombre());
        if (!p.getComentarios().equals("")) holder.detalle.setBackgroundResource(R.drawable.detalle_green);
        if (p.getTiempoMedio() != 0) holder.tiempo.setBackgroundResource(R.drawable.clock_green);

        holder.detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPuntos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo;
        public ImageButton detalle, tiempo;

        public ViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.item_salida_titulo);
            detalle = (ImageButton) v.findViewById(R.id.item_salida_detalles);
            tiempo = (ImageButton) v.findViewById(R.id.item_salida_tiempo);
        }
    }
}
