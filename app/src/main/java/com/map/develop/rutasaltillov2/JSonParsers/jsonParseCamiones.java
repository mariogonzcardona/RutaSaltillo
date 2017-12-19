package com.map.develop.rutasaltillov2.JSonParsers;

import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.map.develop.rutasaltillov2.Kotlin.MapsActivity;
import com.map.develop.rutasaltillov2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.map.develop.rutasaltillov2.Java.Test.getSelectionRutas;

/**
 * Created by Mario on 17/12/2017.
 */

/**
 * Created by Abhishek Panwar on 7/14/2017.
 */

public class jsonParseCamiones extends AsyncTask<Void,Void,Void> {

    String data="";
    String dataParse="";
    String singleParse="";
    static ArrayList listaRutas = new ArrayList();
    static ArrayList listaCamiones = new ArrayList();
    static String ruta="1328";
    public AutoCompleteTextView textView;

    MapsActivity mpDatos=new MapsActivity();


    @Override
    protected Void doInBackground(Void... voids) {

        try {

            ruta=mpDatos.getSelectionRutas();
            URL url=new URL("https://busmia.herokuapp.com/"+ruta+"/camiones/");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while (line !=null)
            {
                line=bufferedReader.readLine();
                data+=line;

            }

            JSONArray ja=new JSONArray(data);

            for(int i=0; i<ja.length();i++)
            {
                JSONObject joCam=ja.getJSONObject(i);

                JSONObject jaData=new JSONObject("camiones");
                listaCamiones.add(jaData.get("id"));
                System.out.println(listaCamiones.get(i));
                setListaRutas(listaCamiones);

                listaCamiones.add(joCam.get("id"));
                System.out.println(listaCamiones.get(i));
                setListaCamiones(listaCamiones);

                dataParse+=singleParse+"\n";

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Cambiar a Activity en turno
        //Test es llamado desde manifest cambiar a actividad o clase en turno
        //Test.data.setText(this.dataParse);
    }

    public static ArrayList getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(ArrayList listaRutas) {
        this.listaRutas = listaRutas;
    }

    public static String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public static ArrayList getListaCamiones() {
        return listaCamiones;
    }

    public static void setListaCamiones(ArrayList listaCamiones) {
        jsonParseCamiones.listaCamiones = listaCamiones;
    }
}