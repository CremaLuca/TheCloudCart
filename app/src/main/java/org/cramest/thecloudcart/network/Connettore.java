package org.cramest.thecloudcart.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cremaluca on 03/06/2016.
 */
public class Connettore {

    static Context context;
    private static Connettore instance = new Connettore();
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static Connettore getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void GetDataFromWebsite(DataHandler handler, String nome, String nomeParametro, String valoreParametro){
        BackgroundTask task = new BackgroundTask(handler,nome, new String[]{nomeParametro}, new String[]{valoreParametro});
        task.execute();
    }

    public void GetDataFromWebsite(DataHandler handler, String nome, String[] nomiParametro, String[] valoriParametro) {
        BackgroundTask task = new BackgroundTask(handler,nome, nomiParametro, valoriParametro);
        task.execute();
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, String>
    {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String[] nomiParametro;
        String[] valoriParametro;
        DataHandler handler;
        String nome;

        public BackgroundTask(DataHandler Handler,String Nome, String[] NomiParametro, String[] ValoriParametro) {
            nomiParametro = NomiParametro;
            valoriParametro = ValoriParametro;
            handler = Handler;
            this.nome = Nome;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            String result = null;
            try {
                URL url = null;
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("thecloudcart.altervista.org")
                        .appendPath("Android");
                        //.appendQueryParameter("p", pagina);
                for (int i = 0; i < nomiParametro.length; i++) {
                    builder.appendQueryParameter(nomiParametro[i], valoriParametro[i]);
                }
                url = new URL(builder.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                StringBuilder sb = new StringBuilder();
                if (inputStream == null) {
                    Log.e("Errore", "Database Vuoto");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                if (buffer.length() == 0) {
                    return null;
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch(ConnectException e){
                System.out.println("Connettore - ConnectException : Ora riprovo a caricare la pagina se c'è internet");
                if(isNetworkAvailable()) {
                    doInBackground(); //Riprova a caricare
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        System.out.println("ERRORE");
                        return null;
                    }
                }
                return result;
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("Caricamento : " + values);

        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                String[] risultati = result.split("%");
                if(risultati.length > 1) {
                    boolean success = false;
                    success = risultati[0].equals("OK");
                    System.out.println("res " + risultati[0] + " quindi success: " + success + ", per la richiesta " + nome);
                    handler.HandleData(nome, success, risultati[1]);
                    super.onPostExecute(result);
                }else{
                    System.out.println("Result vuoto : " + result + " richiesta con nome : " + nome);
                    handler.HandleData(nome, true, null);
                }
            }else{
                System.out.println("Result = null");
            }
        }
    }
}