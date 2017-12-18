package com.map.develop.rutasaltillov2.JSonParsers;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mario on 17/12/2017.
 */

import android.os.AsyncTask;

import com.map.develop.rutasaltillov2.Java.Test;

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
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;


public class jsonParseRutas extends AsyncTask<Void,Void,Void> {

    String data="";
    String dataParse="";
    static ArrayList listaRutas = new ArrayList();

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            URL url=new URL("https://busmia.herokuapp.com/ruta");
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
                JSONObject jo=ja.getJSONObject(i);

                listaRutas.add(jo.get("nombre"));
               // System.out.println(listaRutas.get(i));
                setListaRutas(listaRutas);

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
        //.data.setText(this.dataParse);
    }

    //Get y Set de Array List
    public static ArrayList getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(ArrayList listaRutas) {
        this.listaRutas = listaRutas;
    }


}