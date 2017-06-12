package org.cramest.thecloudcart.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.UtenteAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Utente;

import java.util.ArrayList;


public class CondividiDialog implements Dati.OnRichiesteUtentiListener{
    public static CondividiDialog instance;

    public static ListView listViewUtenti;
    private UtenteAdapter listViewadapter;

    public void showDialog(final Activity activity,final OnCondividiDialogInteractionListener listener, final Lista lista) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_condividi);

        ((TextView)dialog.findViewById(R.id.text_view_titolo_condividi)).setText("Condividi lista " + lista.getNome());
        listViewUtenti = (ListView)dialog.findViewById(R.id.list_condividi_results);

        listViewUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utente utente = (Utente)listViewUtenti.getAdapter().getItem(i);
                System.out.println("CondividiDialog - Utente selezionato, ora richiedo la condivisione con " + utente.getUsername());
                listener.OnRequestCondividiLista(lista,utente);
                dialog.dismiss();
            }
        });

        listViewadapter = new UtenteAdapter(listViewUtenti.getContext(), R.layout.adapter_utente, new ArrayList<Utente>());
        listViewUtenti.setAdapter(listViewadapter);

        SearchView searchView = (SearchView)dialog.findViewById(R.id.search_view_condividi);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println("CondividiDialog - Ora cerco " + s);
                ricercaSconosciuti(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("CondividiDialog - Testo cambiato in " + s);
                ricercaAmici(s);
                return false;
            }
        });
        //Mostriamo già la lista
        ricercaAmici("");
        instance = this;
        dialog.show();
    }

    private void ricercaAmici(String ricerca){
        ArrayList<Utente> corrispondenze = new ArrayList<>();
        //TODO : Rimuovere dalla ricerca gli amici con cui la lista è già condivisa
        for(Utente u : Dati.getUtentiAmici()){
            if(u.getUsername().toLowerCase().startsWith(ricerca.toLowerCase())){
                corrispondenze.add(u);
            }else if(u.getNome().toLowerCase().startsWith(ricerca.toLowerCase())){
                corrispondenze.add(u);
            }
        }
        listViewadapter.clear();
        listViewadapter.addAll(corrispondenze);
    }

    private void ricercaSconosciuti(String ricerca){
        Dati.findUser(ricerca,this);
    }

    @Override
    public void OnRicercaUtentiCompletata(ArrayList<Utente> utentiCorrispondenti) {
        System.out.println("CondividiDialog - Sono arrivati gli utenti corrispondenti");
        listViewadapter.clear();
        listViewadapter.addAll(utentiCorrispondenti);
    }

    public interface OnCondividiDialogInteractionListener{
        void OnRequestCondividiLista(Lista lista,Utente user);
    }

}
