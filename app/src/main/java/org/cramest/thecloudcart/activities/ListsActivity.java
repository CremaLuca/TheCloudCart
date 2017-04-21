package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.ListaAdapter;
import org.cramest.thecloudcart.classi.ListaCategorie;
import org.cramest.thecloudcart.classi.ListaProdotti;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.WebsiteDataManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ListsActivity extends Activity implements DataHandler {

    private String username;
    private String userID;
    //private ArrayList<Lista> liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        //recuperiamo nome utente e password dall'intent
        username = getIntent().getExtras().getString("username");
        userID = getIntent().getExtras().getString("userID");
        //Inizializziamo categorie e prodotti dell'utente
        InizializzaApplicazione();
        //Carichiamo la lista
        CaricaListaMie();
        setAdapter(R.id.listViewMie,new ArrayList<Lista>(Arrays.asList(new Lista(0,"Caricamento...",-1))));
        CaricaListaCondivise();
        setAdapter(R.id.listViewCondivise,new ArrayList<Lista>(Arrays.asList(new Lista(0,"Caricamento...",-1))));
    }

    private void InizializzaApplicazione(){
        System.out.println("ListsActivity - Recupero le categorie e i prodotti");
        //recuperiamo le categorie
        new ListaCategorie().recuperaCategorie(this);
        //recuperiamo tutti i prodotti
        new ListaProdotti().recuperaProdotti(this);
    }

    /** La richiesta delle liste della spesa dell'utente
     */
    private void CaricaListaMie(){
        System.out.println("ListsActivity - Carico le liste dell'utente " + username);
        //richiesta = "userlist" & user = username
        String[] parametri = {"req","userID"};
        String[] valori = {"getUserList",userID};
        if(Connettore.getInstance(this).isNetworkAvailable()) {
            //Chiediamo al sito le liste
            Connettore.getInstance(this).GetDataFromWebsite(this, "listeSpesaMie", parametri, valori);
        }else{
            //TODO : Carichiamo le liste dal locale
        }
    }

    private void CaricaListaCondivise(){
        System.out.println("ListsActivity - Carico le liste dell'utente " + username);
        //richiesta = "userlist" & user = username
        String[] parametri = {"req","userID"};
        String[] valori = {"getCondivisioniUtente",userID};
        if(Connettore.getInstance(this).isNetworkAvailable()) {
            //Chiediamo al sito le liste
            Connettore.getInstance(this).GetDataFromWebsite(this, "listeSpesaCondivise", parametri, valori);
        }else{
            //non carichiamo niente perchè tanto gli altri non possono visualizzare le modifiche, eliminiamo la scritta liste condivise
            ((TextView)findViewById(R.id.textAltreListe)).setVisibility(View.GONE);
        }
    }

    private void setAdapter(int viewID, final ArrayList<Lista> lista){
        ListaAdapter listViewadapter = new ListaAdapter(this, R.layout.list_lista_prodotti, lista);
        ListView lv = (ListView) findViewById(viewID);
        lv.setAdapter(listViewadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idProdotto = lista.get(position).getID();
                if(idProdotto != -1) {
                    //Nuovo intent per aprire la activity per visualizzare i prodotti in questa lista
                    Intent i = new Intent(ListsActivity.this, ProdottiActivity.class);
                    i.putExtra("IDLista", idProdotto);
                    i.putExtra("username", username);
                    i.putExtra("userID", userID);
                    startActivity(i);
                }else{
                    //Nuovo intent per aggiungere una nuova lista
                    Intent i = new Intent(ListsActivity.this, AggiungiListaActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("userID", userID);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void HandleData(String nome, boolean successo,String data){
        if(successo) {
            //Dato che facciamo solo due richieste ed entrambe tornano un arraylist di liste facciamo il recupero di entrambe fuori
            //Convertiamo i dati in liste
            ArrayList<Lista> liste = new ArrayList<>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
            if (nome.equals("listeSpesaMie")) {
                //Aggiungamo il bottone crea nuova lista
                liste.add(new Lista(-1,"Crea nuova lista",-1));
                //Inseriamo nel ListView le liste
                setAdapter(R.id.listViewMie,liste);
            }
            if(nome.equals("listeSpesaCondivise")){
                //Inseriamo nel ListView le liste
                setAdapter(R.id.listViewCondivise,liste);
            }
        }else{
            Toast.makeText(this, "Errore : " + data, Toast.LENGTH_SHORT).show();
        }
    }
}
