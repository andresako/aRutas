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
        holder.titulo.setText(tituloOrdenado(item));
        holder.fecha.setText(fechaOrdenada(item.getFecha()));

    }

    private String tituloOrdenado(Historial h){
        String ordenado = "";
        ordenado += h.getRuta().getTitulo() + "     ";
        int horas = h.getTiempo() / 3600;
        int restante = h.getTiempo() - horas * 3600;
        int mins = restante / 60;
        String min = mins + "";
        if (mins < 10) min = "0"+min;
        ordenado += horas+":"+min+"h";
        return ordenado;
    }
    private String fechaOrdenada(String fecha) {
        String ordenada = "";           //fecha = YYYY-MM-DD hh:mm:ss
        String[] todo,dia, hora;
        todo = fecha.split(" ");        // 0 = fecha, 1 hora
        dia = todo[0].split("-");       // 0 = año, 1 = mes, 2 = dia
        hora = todo[1].split(":");      // 0 = hora, 1 = min, 2 = secs
        ordenada += dia[2] + " de ";
        String mes = "";
        switch (Integer.valueOf(dia[1])){
            case 1:
                mes = "Enero";
                break;
            case 2:
                mes = "Febrero";
                break;
            case 3:
                mes = "Marzo";
                break;
            case 4:
                mes = "Abril";
                break;
            case 5:
                mes = "Mayo";
                break;
            case 6:
                mes = "Junio";
                break;
            case 7:
                mes = "Julio";
                break;
            case 8:
                mes = "Agosto";
                break;
            case 9:
                mes = "Septiembre";
                break;
            case 10:
                mes = "Octubre";
                break;
            case 11:
                mes = "Noviembre";
                break;
            case 12:
                mes = "Diciembre";
                break;
        }
        ordenada += mes + "     " + hora[0]+":"+hora[1];

        return ordenada;
    }

    @Override
    public int getItemCount() {
        return listaHistoriales.size();
    }
}
