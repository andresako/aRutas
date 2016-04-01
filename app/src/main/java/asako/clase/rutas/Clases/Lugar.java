package asako.clase.rutas.Clases;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Lugar implements Serializable {

    private String nombre;
    private LatLng posicion;

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
}
