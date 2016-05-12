package asako.clase.rutas.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.R;
import asako.clase.rutas.UI.FragmentoSalida;

public class AdaptadorSalida extends RecyclerView.Adapter<AdaptadorSalida.ViewHolder>{

    private final ArrayList<Punto> listaPuntos;
    private final Context context;
    private final FragmentoSalida padre;

    public AdaptadorSalida(FragmentoSalida activity, ArrayList<Punto> listaPuntos){
        this.padre = activity;
        this.context = activity.getActivity();
        this.listaPuntos = listaPuntos;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_punto_salida, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Punto p = listaPuntos.get(position);
        Log.d("AdpSalida", "time: "+ p.getTiempoMedio());
        holder.titulo.setText(p.getNombre());
        if (!p.getComentarios().equals("")) holder.detalle.setBackgroundResource(R.drawable.detalle_green);
        if (p.getTiempoMedio() != 0) holder.tiempo.setBackgroundResource(R.drawable.clock_green);

        holder.detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Añade detalles");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().trim().length() != 0) {
                            listaPuntos.get(position).setComentarios(input.getText().toString());
                            holder.detalle.setBackgroundResource(R.drawable.detalle_green);
                        }
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelado.
                    }
                });
                alert.show();
            }
        });
        holder.tiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Tiempo en este punto");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().trim().length() != 0) {
                            listaPuntos.get(position).setTiempoMedio(Integer.parseInt(input.getText().toString()));
                            calcularTiempos();
                            holder.tiempo.setBackgroundResource(R.drawable.clock_green);
                        }
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelado.
                    }
                });
                alert.show();
            }
        });
    }

    private void calcularTiempos() {
        int ct = 0;
        for (Punto p: listaPuntos){
            ct += p.getTiempoMedio()*60;
        }
        padre.tiempoRuta = ct;
        padre.refreshDatos();
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
