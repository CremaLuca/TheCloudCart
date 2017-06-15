package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.LoginApp;
import org.cramest.utils.DataSaver;

public class LandActivity extends Activity implements LoginApp.OnLoginAppListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("LandActivity - Username salvato : " + DataSaver.getInstance().getDataString(this, "username") + " e password: " + DataSaver.getInstance().getDataString(this, "password"));
        //Controlliamo se c'è rete
        if(Connettore.getInstance(this).isNetworkAvailable()) {
            //Se non ha già effettuato il login in un altro momento
            if (DataSaver.getInstance().getDataString(this, "username") == null) {
                //Mandiamolo alla schermata del login in cui può inserire le credenziali
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                //impediamo di tornare indietro con finish()
                finish();
            } else {
                //Abbiamo già nome utente e password
                setContentView(R.layout.activity_land);
                //recuperiamo nome utente e password dalla memoria e chiediamo al sito se sono giusti
                String username = DataSaver.getInstance().getDataString(this, "username");
                String password = DataSaver.getInstance().getDataString(this, "password");
                new LoginApp(this, username, password);

            }
        }else{
            //Se manca la connessione ad internet facciamo l'accesso con l'ultimo account, altrimenti notifichiamo che e' necessaria una connessione internet al primo accesso
            String username = DataSaver.getInstance().getDataString(this, "username");
            String userID = DataSaver.getInstance().getDataString(this, "userID");
            String nameUser = DataSaver.getInstance().getDataString(this, "nameUser");
            if(username != null) {
                //Passiamo direttamente alla activity in cui mostriamo la lista
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("username", username);
                i.putExtra("userID", userID);
                i.putExtra("nameUser", nameUser);
                startActivity(i);
                //impediamo di tornare indietro con finish()
                finish();
            }else{
                Toast.makeText(this,"Hai bisogno di un accesso ad internet per poter usare l'app",Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void OnLoginSuccess(String username, String userID, String nameUser) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        i.putExtra("userID", userID);
        i.putExtra("nameUser", nameUser);
        startActivity(i);
        //impediamo di tornare indietro con finish()
        finish();
    }

    @Override
    public void OnLoginFailed() {
        //Rifacciamogli fare il login
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }
}
