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
import org.cramest.thecloudcart.classi.ProdottoAdapter;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ProdottiActivity extends Activity implements DataHandler {

    private int IDLista;
    private ArrayList<ProdottoInLista> prodottiInLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodotti);
        //recuperiamo l'id della lista corrente
        IDLista = getIntent().getExtras().getInt("IDLista");
        String[] pars = {"req","listID"};
        String[] vals = {"getProductList",IDLista+""};
        Connettore.getInstance(this).GetDataFromWebsite(this,"ProdottiLista",pars,vals);
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success){
            if(nome.equals("ProdottiLista")){
                prodottiInLista = new ArrayList<ProdottoInLista>(Arrays.asList(WebsiteDataManager.getProdottiInLista(data)));
                //Inseriamo nel ListView le liste
                ListView lv = (ListView) findViewById(R.id.listProdotti);
                ProdottoAdapter listViewadapter = new ProdottoAdapter(this, R.layout.list_prodotto, prodottiInLista);
                lv.setAdapter(listViewadapter);
            }
        }
    }

    private String[] ricavaNomeProdotti(ArrayList<ProdottoInLista> prodotti){
        String[] nomi = new String[prodotti.size()];
        for(int i=0;i<prodotti.size();i++){
            nomi[i] = prodotti.get(i).getProdotto().getNome();
        }
        return nomi;
    }
}
