package asako.clase.rutas.Clases;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Lugar implements Serializable {

    private String nombre;
    private LatLng posicion;
    private String detalles = "";
    private int tiempoMedio = 0;

    public Lugar() {
    }
    public Lugar(String nombre, LatLng posicion) {
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
}
