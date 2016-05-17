package asako.clase.rutas.Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.Clases.Punto;
import asako.clase.rutas.Clases.Ruta;

public class MiConfig implements Serializable {

    private static MiConfig MC;
    private static Punto SALIDA;
    private static LinkedHashMap<Integer, Punto> HASH_PUNTOS;
    private static LinkedHashMap<Integer, Ruta> HASH_RUTAS;
    private static LinkedHashMap<Integer, Historial> HASH_HISTORIAL;

    private MiConfig() {
        HASH_PUNTOS = new LinkedHashMap<>();
        HASH_RUTAS = new LinkedHashMap<>();
        HASH_HISTORIAL = new LinkedHashMap<>();
        SALIDA = null;
    }

    public synchronized static MiConfig get() {
        if (MC == null) MC = new MiConfig();
        return MC;
    }
    public void vaciar(){
        MC = new MiConfig();
    }

    public boolean isSalidaSet() {
        return SALIDA != null;
    }
    public Punto getSalida() {
        return SALIDA;
    }
    public void setSalida(Punto pt) {
        SALIDA = pt;
    }

    public void addPunto(int id, Punto punto) {
        HASH_PUNTOS.put(id, punto);
    }
    public Punto getPunto(int id) {
        return HASH_PUNTOS.get(id);
    }

    public void addRuta(int id, Ruta ruta) {
        HASH_RUTAS.put(id, ruta);
    }
    public Ruta getRuta(int id) {
        return HASH_RUTAS.get(id);
    }
    public void removeRuta(int id) {
        HASH_RUTAS.remove(id);
    }

    public void addHistorial(int id, Historial ht) {
        HASH_HISTORIAL.put(id, ht);
    }
    public ArrayList<Historial> getHistorial() {
        ArrayList<Historial> ct = new ArrayList<>();
        LinkedList<Historial> ct2 = new LinkedList<>(HASH_HISTORIAL.values());

        for(int x = ct2.size()-1; x >= 0;x--){
            ct.add(ct2.get(x));
        }

        return ct;
    }

    public ArrayList<Ruta> getListaRutas() {
        return new ArrayList<>(HASH_RUTAS.values());
    }
    public ArrayList<String> getNombreRutas() {
        ArrayList<String> lista = new ArrayList<>();
        for (Ruta r : HASH_RUTAS.values()) {
            lista.add(r.getTitulo());
        }

        return lista;
    }

    public ArrayList<Punto> getListaPuntos() {
        return new ArrayList<>(HASH_PUNTOS.values());
    }
    public ArrayList<String> getNombrePuntos() {
        ArrayList<String> lista = new ArrayList<>();
        for (Punto p : HASH_PUNTOS.values()) {
            lista.add(p.getNombre());
        }

        return lista;
    }
}