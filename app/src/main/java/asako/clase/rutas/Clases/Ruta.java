package asako.clase.rutas.Clases;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private String titulo;
    private int tiempo = 0;
    private int distancia = 0;
    private List<Punto> listaLugaresVisitados = new ArrayList<>();

    public Ruta() {
    }

    public Ruta(String titulo) {
        this.titulo = titulo;
    }

    public Ruta(String titulo, int tiempo, int distancia, List<Punto> listaLugaresVisitados) {
        this.titulo = titulo;
        this.tiempo = tiempo;
        this.distancia = distancia;
        this.listaLugaresVisitados = listaLugaresVisitados;
    }

    public String getTitulo() {
        return titulo;
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

}
