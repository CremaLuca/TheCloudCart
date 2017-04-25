package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

public class AggiungiListaActivity extends Activity implements View.OnClickListener,DataHandler{

    private String userID;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_lista);
        //recuperiamo nome utente dall'intent
        userID = getIntent().getExtras().getString("userID");
        ((Button)findViewById(R.id.buttonCreaLista)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonCreaLista:
                String nomeLista = ((EditText)findViewById(R.id.editTextNomeLista)).getText().toString();
                //Generiamo l'intent
                i = new Intent(AggiungiListaActivity.this, AggiungiListaCondividiActivity.class);
                i.putExtra("nomeLista", nomeLista);
                //Aggiungiamo la lista tramite l'API
                String[] parametri = {"req","userID","listName"};
                String[] valori = {"addList",userID,nomeLista};
                if(Connettore.getInstance(this).isNetworkAvailable()) {
                    //Chiediamo al sito le liste
                    Connettore.getInstance(this).GetDataFromWebsite(this, "aggiungiLista", parametri, valori);
                    //TODO : Aprire la activity condividi per condividere la lista con gli amici
                }else{
                    //TODO : Aggiunta liste in locale e aggiunta alla lista di cose da aggiornare
                }
                break;
        }
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success) {
            System.out.println("OK : " + data);
            startActivity(i);
        }else{
            Toast.makeText(this,data, Toast.LENGTH_SHORT).show();
        }
    }
}
