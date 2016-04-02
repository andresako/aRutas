package asako.clase.rutas.Clases;

import java.io.Serializable;

public class Historial implements Serializable{

    private String fecha;
    private Ruta ruta;

    public Historial() {
    }

    public Historial(Ruta ruta, String fecha) {
        this.ruta = ruta;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }
    public Ruta getRuta(){
        return this.ruta;
    }
}
