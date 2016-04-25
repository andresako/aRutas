package asako.clase.rutas.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ruta implements Parcelable {
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
    private String titulo;
    private int ID = 0;
    private int tiempo = 0;
    private int distancia = 0;
    private List<Punto> listaLugaresVisitados = new ArrayList<>();

    public Ruta(int id) {
        this.ID = id;
    }

    public Ruta(String titulo, int distancia, List<Punto> listaLugaresVisitados) {
        this.titulo = titulo;
        this.distancia = distancia;
        this.listaLugaresVisitados = listaLugaresVisitados;
        for (Punto pt : listaLugaresVisitados) {
            this.tiempo += pt.getTiempoMedio();
        }
    }

    protected Ruta(Parcel in) {
        titulo = in.readString();
        ID = in.readInt();
        tiempo = in.readInt();
        distancia = in.readInt();
        in.readTypedList(listaLugaresVisitados, Punto.CREATOR);
    }

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

    public int getTiempo() {
        return tiempo;
    }

    public int getDistancia() {
        return distancia;
    }

    public List<Punto> getListaLugaresVisitados() {
        return listaLugaresVisitados;
    }

    public void addTiempo(int tiempo) {
        this.tiempo += tiempo;
    }

    public void addDistancia(int distancia) {
        this.distancia += distancia;
    }

    public void addLugar(Punto punto) {
        this.listaLugaresVisitados.add(punto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeInt(ID);
        dest.writeInt(tiempo);
        dest.writeInt(distancia);
        dest.writeTypedList(listaLugaresVisitados);
    }

    public JSONObject toJsonObject() {
        JSONObject ct = new JSONObject();
        JSONObject jarray = new JSONObject();
        try {
            for (int i = 0; i < this.listaLugaresVisitados.size(); i++) {
                jarray.put(listaLugaresVisitados.get(i).getID()+"", listaLugaresVisitados.get(i).getTiempoMedio());
            }
            ct.put("titulo", this.titulo);
            ct.put("puntos", jarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return ct;
    }
}
