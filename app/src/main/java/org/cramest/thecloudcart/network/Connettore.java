package org.cramest.thecloudcart.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Connettore {

    static Context context;
    private static Connettore instance = new Connettore();
    private static final String divisoreDocumento = "•";
    private static String appVersion = "2.22";
    private static String lang = "en";

    public static Connettore getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        PackageInfo pInfo = null;
        try {
            pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        lang = Locale.getDefault().getLanguage();
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
                        .appendPath("Android")
                        .appendQueryParameter("appVersion", appVersion)
                        .appendQueryParameter("lang", lang);
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
                } else {
                    System.out.println("Connettore - Non c'è internet, non carico più niente");
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
                        System.out.println("Connettore - ERRORE: " + e.getMessage());
                        FirebaseCrash.log("Errore Connettore " + e.getMessage());
                        return null;
                    }
                }
                return result;
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("Connettore - Caricamento : " + values);

        }

        @Override
        protected void onPostExecute(String result) {
            //Allora questa riga serve a prendere solo la prima parte del documento così la pubblicità non viene processata
            result = result.split(divisoreDocumento)[0];
            if(result != null) {
                String[] risultati = result.split("↔");
                if(risultati.length > 1) {
                    boolean success = false;
                    success = risultati[0].equals("OK");
                    System.out.println("Connettore - " + risultati[0] + ", per la richiesta " + nome + " con dati: " + risultati[1]);
                    handler.HandleData(nome, success, risultati[1]);
                    super.onPostExecute(result);
                }else{
                    System.out.println("Connettore - Result vuoto : " + result + " richiesta con nome : " + nome);
                    handler.HandleData(nome, true, null);
                }
            }else{
                System.out.println("Connettore - Result = null");
            }
        }
    }
}