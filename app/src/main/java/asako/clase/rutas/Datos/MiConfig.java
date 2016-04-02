package asako.clase.rutas.Datos;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Historial;
import asako.clase.rutas.Clases.Lugar;
import asako.clase.rutas.Clases.Ruta;

public class MiConfig {

    private Lugar pSalida;
    private List<Lugar> listaPuntos;

    // Datos dummy
    public static Lugar SALIDA;
    public static List<Lugar> LISTA_PUNTOS = new ArrayList<>();
    public static List<Ruta> LISTA_RUTAS = new ArrayList<>();
    public static List<Historial> LISTA_HISTORIAL = new ArrayList<>();

    static{
        SALIDA = new Lugar("Salida", new LatLng(38.423, -0.396));

        LISTA_PUNTOS.add(new Lugar("Punto 1", new LatLng(38.367, -0.501)));
        LISTA_PUNTOS.add(new Lugar("Punto 2", new LatLng(38.369, -0.479)));
        LISTA_PUNTOS.add(new Lugar("Punto 3", new LatLng(38.344, -0.507)));

        Ruta ruta1 = new Ruta("Ruta1");
        ruta1.addTiempo(15);
        ruta1.addDistancia(20000);
        ruta1.addLugar(LISTA_PUNTOS.get(0));

        Ruta ruta2 = new Ruta("Ruta2");
        ruta2.addTiempo(30);
        ruta2.addDistancia(35000);
        ruta2.addLugar(LISTA_PUNTOS.get(1));
        ruta2.addLugar(LISTA_PUNTOS.get(2));

        Ruta ruta3 = new Ruta("Ruta3");
        ruta3.addTiempo(85);
        ruta3.addDistancia(75000);
        ruta3.addLugar(LISTA_PUNTOS.get(0));
        ruta3.addLugar(LISTA_PUNTOS.get(1));
        ruta3.addLugar(LISTA_PUNTOS.get(2));

        LISTA_RUTAS.add(ruta1);
        LISTA_RUTAS.add(ruta2);
        LISTA_RUTAS.add(ruta3);

        LISTA_HISTORIAL.add(new Historial(ruta1,"1 Enero"));
        LISTA_HISTORIAL.add(new Historial(ruta2,"2 Enero"));
        LISTA_HISTORIAL.add(new Historial(ruta3,"3 Enero"));
    }



}
