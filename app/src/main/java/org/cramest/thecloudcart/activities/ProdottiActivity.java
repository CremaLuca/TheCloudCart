package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.ListaProdotti;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoAdapter;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ProdottiActivity extends Activity implements DataHandler {

    private int IDLista;
    private String username;
    private String userID;
    private ArrayList<ProdottoInLista> prodottiInLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodotti);
        //recuperiamo l'id della lista corrente
        IDLista = getIntent().getExtras().getInt("IDLista");
        username = getIntent().getExtras().getString("username");
        userID = getIntent().getExtras().getString("userID");
        String[] pars = {"req","listID"};
        String[] vals = {"getProductList",IDLista+""};
        Connettore.getInstance(this).GetDataFromWebsite(this,"ProdottiLista",pars,vals);
        prodottiInLista = new ArrayList<ProdottoInLista>();
        prodottiInLista.add(new ProdottoInLista(new Prodotto("Caricamento..."),-1,""));
        setListAdapter();

    }

    private void setListAdapter(){
        ListView lv = (ListView) findViewById(R.id.listProdotti);
        ProdottoAdapter listViewadapter = new ProdottoAdapter(this, R.layout.list_prodotto, prodottiInLista);
        lv.setAdapter(listViewadapter);
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("ProdottiLista")){
                if(data != null) {
                    prodottiInLista = new ArrayList<ProdottoInLista>(Arrays.asList(WebsiteDataManager.getProdottiInLista(data)));
                    prodottiInLista.add(new ProdottoInLista(new Prodotto("Aggiungi prodotto"), 0, ""));
                    //Inseriamo nel ListView le liste
                    setListAdapter();
                }else{
                    prodottiInLista.clear();
                }
            }
        }
    }
}
