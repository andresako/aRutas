package asako.clase.rutas.Tools;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaUtils {

    private final Context context;

    public MapaUtils(Context context) {
        this.context = context;
    }

    public String getNomPosicion(LatLng latLng) {
        String pos;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();

        pos = addresses.get(0).getAddressLine(0) + ", " + city + ", " + country;

        return pos;
    }

}
