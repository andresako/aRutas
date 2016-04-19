package asako.clase.rutas.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import asako.clase.rutas.R;

public class AdaptadorRutas extends BaseExpandableListAdapter {

    private Context context;
    private List<String> NombresRutas;
    private HashMap<String, List<String>> PuntosdeRutas;

    public AdaptadorRutas(Context context, MiConfig datos){

        this.context = context;
        NombresRutas = new ArrayList<>();
        PuntosdeRutas = new HashMap<>();
        for (int x = 0; x < datos.getListaRutas().size();x++) {
            NombresRutas.add(datos.getListaRutas().get(x).getTitulo());
            ArrayList<String> puntos = new ArrayList<>();
            for (int y = 0; y < datos.getListaRutas().get(x).getListaLugaresVisitados().size(); y++){
                puntos.add(datos.getListaRutas().get(x).getListaLugaresVisitados().get(y).getNombre());
            }
            PuntosdeRutas.put(NombresRutas.get(x),puntos);
        }
    }

    @Override
    public int getGroupCount() {
        return this.NombresRutas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.PuntosdeRutas.get(NombresRutas.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.NombresRutas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.PuntosdeRutas.get(NombresRutas.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragmento_rutas_titulo, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.rutasListaTitulo);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setGravity(Gravity.CENTER);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragmento_rutas_contenido, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.rutasListaDetalles);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

