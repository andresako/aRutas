package asako.clase.rutas.Tools;

public class Datos {

    private static Datos ourInstance = new Datos();
    public static Datos getInstance() {
        return ourInstance;
    }

    private Datos() {

    }
}
