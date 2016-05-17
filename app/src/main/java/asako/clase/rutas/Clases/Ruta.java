package asako.clase.rutas.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ruta implements Parcelable {
    private int ID = 0;
    private String titulo;
    private ArrayList<Punto> listaPuntos = new ArrayList<>();

    public Ruta(int id) {
        this.ID = id;
    }

    public Ruta(String titulo, ArrayList<Punto> listaPuntos) {
        this.titulo = titulo;
        this.listaPuntos = listaPuntos;
    }

    protected Ruta(Parcel in) {
        titulo = in.readString();
        ID = in.readInt();
        in.readTypedList(listaPuntos, Punto.CREATOR);
    }

    public static final Creator<Ruta> CREATOR = new Creator<Ruta>() {
        @Override
        public Ruta createFromParcel(Parcel in) {
            return new Ruta(in);
        }

        @Override
        public Ruta[] newArray(int size) {
            return new Ruta[size];
        }
    };

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Punto> getListaPuntos() {
        return listaPuntos;
    }
    public void addLugar(Punto punto) {
        this.listaPuntos.add(punto);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeInt(ID);
        dest.writeTypedList(listaPuntos);
    }

    public JSONObject toJsonObject() {
        JSONObject ct = new JSONObject();
        JSONArray jarray = new JSONArray();
        try {
            for (int i = 0; i < this.listaPuntos.size(); i++) {
                JSONObject jobj = new JSONObject();
                jobj.put("tiempo", listaPuntos.get(i).getTiempoMedio());
                jobj.put("id", listaPuntos.get(i).getID());
                jobj.put("detalles", listaPuntos.get(i).getComentarios());
                jarray.put(jobj);
            }
            ct.put("titulo", this.titulo);
            ct.put("puntos",jarray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ct;
    }
}
