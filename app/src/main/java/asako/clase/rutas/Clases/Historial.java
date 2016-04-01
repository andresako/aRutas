package asako.clase.rutas.Clases;

import java.util.List;

public class Historial {

    private String fecha;
    private List<Lugar> listaLugaresVisitados;
    private String tiempo;
    private String distancia;

    public Historial() {
    }

    public Historial(String fecha, List<Lugar> listaLugaresVisitados, String tiempo, String distancia) {
        this.fecha = fecha;
        this.listaLugaresVisitados = listaLugaresVisitados;
        this.tiempo = tiempo;
        this.distancia = distancia;
    }

    public String getFecha() {
        return fecha;
    }

    public List<Lugar> getListaLugaresVisitados() {
        return listaLugaresVisitados;
    }

    public String getTiempo() {
        return tiempo;
    }

    public String getDistancia() {
        return distancia;
    }
}
