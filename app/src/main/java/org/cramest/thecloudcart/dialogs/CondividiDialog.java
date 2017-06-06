package org.cramest.thecloudcart.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.admin.SystemUpdatePolicy;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.UtenteAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.classi.Utente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cremaluca on 05/06/2017.
 */

public class CondividiDialog implements Dati.OnRichiesteUtentiListener{
    public static CondividiDialog instance;

    public static ListView listViewUtenti;

    public void showDialog(final Activity activity,final OnCondividiDialogInteractionListener listener, final Lista lista) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_condividi);

        ((TextView)dialog.findViewById(R.id.text_view_titolo_condividi)).setText("Condividi lista " + lista.getNome());
        listViewUtenti = (ListView)dialog.findViewById(R.id.list_condividi_results);

        ((ListView)dialog.findViewById(R.id.list_condividi_results)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("CondividiDialog - Utente selezionato, ora richiedo la condivisione");
                listener.OnRequestCondividiLista(lista,(Utente)listViewUtenti.getSelectedItem());
                dialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("CondividiDialog - Niente selezionato");
            }
        });

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
        setAdapter(listViewUtenti,corrispondenze);
    }

    private void ricercaSconosciuti(String ricerca){
        Dati.findUser(ricerca,this);
    }

    private void setAdapter(ListView listView, ArrayList<Utente> utenti) {
        UtenteAdapter listViewadapter = new UtenteAdapter(listView.getContext(), R.layout.adapter_utente, utenti);
        listView.setAdapter(listViewadapter);
    }

    @Override
    public void OnRicercaUtentiCompletata(ArrayList<Utente> utentiCorrispondenti) {
        System.out.println("CondividiDialog - Sono arrivati gli utenti corrispondenti");
        setAdapter(listViewUtenti,utentiCorrispondenti);
    }

    public interface OnCondividiDialogInteractionListener{
        void OnRequestCondividiLista(Lista lista,Utente user);
    }

}
