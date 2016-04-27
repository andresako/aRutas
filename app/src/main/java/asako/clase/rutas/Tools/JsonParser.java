package asako.clase.rutas.Tools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.Clases.Ruta;
import asako.clase.rutas.UI.PantallaNuevaRuta;

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JsonParser(){
    }

    public JSONObject peticionHttp(String url, String metodo, List<NameValuePair> params) {

        // Peticion HTTP
        try {

            // Comprobar si es post o get
            if(metodo.equals("POST")){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(metodo.equals("GET")){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //  Conversion de la respuesta en String
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            json = Html.fromHtml(json).toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error convirtiendo resultado " + e.toString());
        }

        // Conversion de la String en objeto Json
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.d("json",json);
            Log.e("JSON Parser", "Error parseando datos " + e.toString());
        }

        // Return objeto Json
        return jObj;
    }

}
