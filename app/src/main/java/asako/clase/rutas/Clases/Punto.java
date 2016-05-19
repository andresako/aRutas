package asako.clase.rutas.Clases;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Punto implements Parcelable {

    private int ID = 0;
    private String nombre = "";
    private String descripcion = "";
    private LatLng posicion;
    private String comentarios = "";
    private int tiempoMedio = 0;

    public Punto() {
    }

    public Punto(int id){
        this.ID = id;
    }
    public Punto(int id, String nombre, LatLng posicion) {
        this.ID = id;
        this.nombre = nombre;
        this.posicion = posicion;
    }

    protected Punto(Parcel in) {
        ID = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        posicion = in.readParcelable(LatLng.class.getClassLoader());
        comentarios = in.readString();
        tiempoMedio = in.readInt();
    }

    public static final Creator<Punto> CREATOR = new Creator<Punto>() {
        @Override
        public Punto createFromParcel(Parcel in) {
            return new Punto(in);
        }

        @Override
        public Punto[] newArray(int size) {
            return new Punto[size];
        }
    };

    public int getID(){
        return this.ID;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){this.nombre = nombre;}

    public LatLng getPosicion() {
        return posicion;
    }
    public void setPosicion(LatLng latLng){this.posicion = latLng;}

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setComentarios(String detalles){
        this.comentarios = detalles;
    }
    public String getComentarios(){
        return this.comentarios;
    }

    public void setTiempoMedio(int tiempoMedio){
        this.tiempoMedio = tiempoMedio;
    }
    public int getTiempoMedio(){
        return this.tiempoMedio;
    }

    public String getNomPosicion(Context context)  {
        String pos;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(posicion.latitude,posicion.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();

        pos = addresses.get(0).getAddressLine(0) + "," + city + "," + country;

        return pos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeParcelable(posicion, flags);
        dest.writeString(comentarios);
        dest.writeInt(tiempoMedio);
    }
}
