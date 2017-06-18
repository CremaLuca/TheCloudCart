package org.cramest.thecloudcart.network;

import android.content.Context;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.utils.DataSaver;

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
                    System.out.println("LoginApp - Nome utente e password corretti");

                    //Salviamo nella memoria i nostri dati
                    String userID = data.split("♦")[0];
                    String nameUser = data.split("♦")[1];
                    SaveLoginData(c, username, userID, nameUser, password);
                    //Notifichiamo il fragment che siamo riusciti a fare l'accesso il quale chiamera l'activity che ne caricherà un altra
                    mListener.OnLoginSuccess(username, userID, nameUser);
                }
            }else{
                Toast.makeText(c, c.getString(R.string.Error) + " : " + data, Toast.LENGTH_SHORT).show();
                mListener.OnLoginFailed();

            }
        }
    }

    public static void SaveLoginData(Context c, String username, String userID, String nameUser, String password) {
        DataSaver.getInstance().saveDataString(c, "username", username);
        DataSaver.getInstance().saveDataString(c, "userID", userID);
        DataSaver.getInstance().saveDataString(c, "nameUser", nameUser);
        DataSaver.getInstance().saveDataString(c, "password", password);//Serve per rifare l'accesso
    }

    public interface OnLoginAppListener{
        void OnLoginSuccess(String username, String userID, String nameUser);
        void OnLoginFailed();
    }
}
