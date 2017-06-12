package org.cramest.thecloudcart.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.UtenteAdapter;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Utente;

import java.util.ArrayList;

public class ListaDialog {

    public static ListaDialog instance;
    private UtenteAdapter listViewAdapter;

    public void showDialog(final Activity activity, final OnListaDialogInteractionListener listener, final Lista lista, final String userID) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_lista);

        ((EditText) dialog.findViewById(R.id.edit_nome_lista)).setText(lista.getNome());

        ((Button)dialog.findViewById(R.id.button_elimina_lista)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnEliminaLista(lista.getID());
                dialog.dismiss();
            }
        });

        ListView lv = (ListView) dialog.findViewById(R.id.list_condivisa_con);
        final Button btnShare = new Button(activity);
        btnShare.setText("Condividi con qualcuno");
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnCondividiLista(lista);
            }
        });

        //Nel caso non ci siano utentei e sia ancora null
        if(lista.getVistaDa() == null) lista.setVistaDa(new ArrayList<Utente>());

        listViewAdapter = new UtenteAdapter(dialog.getContext(), R.layout.adapter_utente, lista.getVistaDa());
        lv.setAdapter(listViewAdapter);

        //Alla fine della lista aggiungiamo questo
        lv.addFooterView(btnShare);

        instance = this;
        dialog.show();
    }

    public interface OnListaDialogInteractionListener {
        void OnEliminaLista(int listID);
        void OnCondividiLista(Lista lista);
    }
}
