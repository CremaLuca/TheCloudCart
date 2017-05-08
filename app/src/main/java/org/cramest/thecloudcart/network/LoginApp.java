package org.cramest.thecloudcart.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.cramest.thecloudcart.activities.MainActivity;
import org.cramest.thecloudcart.fragments.AggiungiProdottoFragment;
import org.cramest.utils.DataSaver;

/**
 * Created by User on 19/01/2017.
 */

public class LoginApp implements DataHandler {

    private String username;
    private String password;
    Context c;


    OnLoginAppListener mListener;

    public LoginApp(Context c, String username, String password){
        if (c instanceof OnLoginAppListener) {
            mListener = (OnLoginAppListener) c;
        } else {
            throw new RuntimeException(c.toString()
                    + " must implement OnLoginAppListener");
        }
        String[] parametri = {"req","user","pass"};
        String[] valori = {"login",username,password};
        this.username = username;
        this.password = password;
        this.c = c;
        System.out.println("LoginApp - Richiedo il login");
        Connettore.getInstance(c).GetDataFromWebsite(this,"loginApp",parametri,valori);
    }

    @Override
    public void HandleData(String nome,boolean success,String data) {
        System.out.println("LoginApp - Pagina di login caricata");
        if(nome.equals("loginApp")){
            //Data conterra' il nome utente, quindi controlliamo di averlo salvato giusto
            if(success){
                if(nome.equals("loginApp")) {
                    System.out.println("Nome utente e password corretti");

                    //Salviamo nella memoria i nostri dati
                    DataSaver.getInstance().saveDataString(c, "username", username);
                    DataSaver.getInstance().saveDataString(c, "userID", data);
                    DataSaver.getInstance().saveDataString(c, "password", password);
                    //Notifichiamo il fragment che siamo riusciti a fare l'accesso il quale chiamera l'activity che ne caricherà un altra
                    mListener.OnLoginSuccess(username,data);
                }
            }else{
                Toast.makeText(c, "Errore : " + data, Toast.LENGTH_SHORT).show();
                mListener.OnLoginFailed();

            }
        }
    }

    public interface OnLoginAppListener{
        void OnLoginSuccess(String username, String userID);
        void OnLoginFailed();
    }
}
