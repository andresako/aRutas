package asako.clase.rutas.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Historial implements Serializable, Parcelable {

    private String fecha;
    private Ruta ruta;

    public Historial() {
    }

    public Historial(Ruta ruta, String fecha) {
        this.ruta = ruta;
        this.fecha = fecha;
    }

    protected Historial(Parcel in) {
        fecha = in.readString();
    }

    public static final Creator<Historial> CREATOR = new Creator<Historial>() {
        @Override
        public Historial createFromParcel(Parcel in) {
            return new Historial(in);
        }

        @Override
        public Historial[] newArray(int size) {
            return new Historial[size];
        }
    };

    public String getFecha() {
        return fecha;
    }

    public Ruta getRuta() {
        return this.ruta;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fecha);
    }
}
