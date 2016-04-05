package asako.clase.rutas.Clases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import asako.clase.rutas.R;
import asako.clase.rutas.UI.FragmentoPunto;

public class AdaptadorPunto extends RecyclerView.Adapter<AdaptadorPunto.ViewHolder> implements View.OnClickListener{

    private final List<Punto> listaLugares;
    private final boolean editable;
    private final FragmentManager fragmentManager;

    public AdaptadorPunto(List<Punto> listaLugares, FragmentManager fragmentManager, boolean editable){
        this.listaLugares = listaLugares;
        this.fragmentManager = fragmentManager;
        this.editable = editable;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView nombre;
        public TextView posicion;
        public TextView tiempo;
        public TextView detalles;
        public ImageView imgDetalles;
        boolean editable;

        public ViewHolder(View v, boolean editable) {
            super(v);
            cardView = (CardView)v;
            nombre = (TextView) v.findViewById(R.id.texto_nombre);
            posicion = (TextView) v.findViewById(R.id.texto_direccion);
            tiempo = (TextView) v.findViewById(R.id.texto_tiempo);
            detalles = (TextView) v.findViewById(R.id.texto_detalles);
            imgDetalles = (ImageView) v.findViewById(R.id.imageView4);
            this.editable = editable;
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta, parent, false);
        final ViewHolder vh = new ViewHolder(v,editable);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Punto punto = listaLugares.get(position);
        holder.nombre.setText(punto.getNombre());
        holder.tiempo.setText(punto.getTiempoMedio()+" min");
        holder.posicion.setText(punto.getPosicion().toString());


        if (!punto.getDetalles().equals("")){
            holder.detalles.setText(punto.getDetalles());
        }else{
            holder.detalles.setVisibility(View.GONE);
            holder.imgDetalles.setVisibility(View.GONE);
        }
        if (holder.editable){
            holder.cardView.setTag(position);
            holder.cardView.setOnClickListener(this);

        }
    }

    @Override
    public int getItemCount() {
        return listaLugares.size();
    }


    @Override
    public void onClick(View v) {
         Log.d("CardView", "Editable" + v.getTag());
        int pos = (int) v.getTag();

        Bundle args = new Bundle();
        args.putSerializable("punto", listaLugares.get(pos));
        Fragment fg = new FragmentoPunto();
        fg.setArguments(args);

        FragmentTransaction fT = fragmentManager.beginTransaction();
        fT.replace(R.id.contenedor_principal, fg,"puntoActivo");
        fT.addToBackStack(null);
        fT.commit();

    }
}
