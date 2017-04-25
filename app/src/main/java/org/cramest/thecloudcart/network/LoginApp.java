package org.cramest.thecloudcart.network;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import org.cramest.thecloudcart.activities.ListsFragmentsActivity;
import org.cramest.utils.DataSaver;

/**
 * Created by User on 19/01/2017.
 */

public class LoginApp implements DataHandler {

    private String username;
    private String password;
    Activity a;

    public LoginApp(Activity a, String username, String password){
        String[] parametri = {"req","user","pass"};
        String[] valori = {"login",username,password};
        this.username = username;
        this.password = password;
        this.a = a;
        System.out.println("LoginApp - Richiedo il login");
        Connettore.getInstance(a).GetDataFromWebsite(this,"loginApp",parametri,valori);
    }

    @Override
    public void HandleData(String nome,boolean success,String data) {
        System.out.println("LoginApp - Pagina di login caricata");
        if(nome.equals("loginApp")){
            //Data conterra' il nome utente, quindi controlliamo di averlo salvato giusto
            if(success){
                if(nome.equals("loginApp")) {
                    System.out.println("Nome utente e password corretti");
                    Intent i = new Intent(a, ListsFragmentsActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("userID", data);
                    //Salviamo nella memoria i nostri dati
                    DataSaver.getInstance().saveDataString(a, "username", username);
                    DataSaver.getInstance().saveDataString(a, "userID", data);
                    DataSaver.getInstance().saveDataString(a, "password", password);
                    //System.out.println("Salvato nome utente e password : " + DataSaver.getInstance().getDataString(c,"username") + " - " + DataSaver.getInstance().getDataString(c,"password"));
                    a.startActivity(i);
                    a.finish();
                }
            }else{
                Toast.makeText(a, "Errore : " + data, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
