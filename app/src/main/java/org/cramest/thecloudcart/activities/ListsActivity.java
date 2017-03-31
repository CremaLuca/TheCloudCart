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
    private String password;
    private ArrayList<Lista> liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        //recuperiamo nome utente e password dall'intent
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        //recuperiamo il testo di benvenuto e mettiamo il nome dell'utente
        TextView textBenvenuto = (TextView) findViewById(R.id.textBenvenuto);
        textBenvenuto.setText("Benvenuto, " + username);
        //Inizializziamo categorie e prodotti dell'utente
        InizializzaApplicazione();
        //Carichiamo la lista
        CaricaLista();
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
    private void CaricaLista(){
        System.out.println("ListsActivity - Carico le liste dell'utente " + username);
        //richiesta = "userlist" & user = username
        String[] parametri = {"req","user"};
        String[] valori = {"getUserList",username};
        //Chiediamo al sito le liste
        Connettore.getInstance(this).GetDataFromWebsite(this,"listeSpesaMie",parametri,valori);
    }

    @Override
    public void HandleData(String nome, boolean successo,String data){
        if(successo) {
            //Controlliamo che siano tornati i miei dati e non altri
            if (nome.equals("listeSpesaMie")) {
                //Convertiamo i dati in liste
                liste = new ArrayList<>(Arrays.asList(WebsiteDataManager.getListeUtente(data)));
                //Inseriamo nel ListView le liste
                ArrayAdapter<String> listViewadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ricavaNomeListe(liste));
                ListView lv = (ListView) findViewById(R.id.listViewMie);
                lv.setAdapter(listViewadapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Nuovo intent per aprire la activity per visualizzare i prodotti in questa lista
                        Intent i = new Intent(ListsActivity.this,ProdottiActivity.class);
                        i.putExtra("IDLista",liste.get(position).getID());
                        startActivity(i);
                    }
                });
            }
            if(nome.equals("listeSpesaCondivise")){

            }
        }else{
            Toast.makeText(this, "Errore : " + data, Toast.LENGTH_SHORT).show();
        }
    }

    private String[] ricavaNomeListe(ArrayList<Lista> liste){
        String[] nomi = new String[liste.size()];
        for(int i=0;i<liste.size();i++){
            nomi[i] = liste.get(i).getNome();
        }
        return nomi;
    }
}
