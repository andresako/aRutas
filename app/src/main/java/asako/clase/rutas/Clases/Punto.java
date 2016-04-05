package asako.clase.rutas.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Punto implements Serializable , Parcelable{

    private String nombre;
    private LatLng posicion;
    private String detalles = "";
    private int tiempoMedio = 0;

    public Punto() {
    }
    public Punto(String nombre, LatLng posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }
    public LatLng getPosicion() {
        return posicion;
    }

    public void setDetalles(String detalles){
        this.detalles = detalles;
    }
    public String getDetalles(){
        return this.detalles;
    }

    public void setTiempoMedio(int tiempoMedio){
        this.tiempoMedio = tiempoMedio;
    }
    public int getTiempoMedio(){
        return this.tiempoMedio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
