package org.cramest.thecloudcart.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.UtenteAdapter;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Utente;

import java.util.ArrayList;

/**
 * Created by User on 19/05/2017.
 */

public class ListaDialog {

    public static ListaDialog instance;

    public void showDialog(Activity activity, final OnListaDialogInteractionListener listener, final Lista lista) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_lista);

        ((EditText) dialog.findViewById(R.id.edit_nome_lista)).setText(lista.getNome());

        //Solo se è condivisa con quacluno impostiamo l'adapter
        if (lista.getVistaDa() != null) {
            //Impostiamo la lista utenti nella listview
            //TODO : aggiungere il tasto codividi con qualcun'altro
            setAdapter(dialog, lista.getVistaDa());
        }

        instance = this;
        dialog.show();
    }

    public interface OnListaDialogInteractionListener {
        void OnCondividiLista(int userID, Lista lista);
    }

    private void setAdapter(Dialog dialog, final ArrayList<Utente> utenti) {
        UtenteAdapter listViewadapter = new UtenteAdapter(dialog.getContext(), R.layout.adapter_utente, utenti);
        ListView lv = (ListView) dialog.findViewById(R.id.list_condivisa_con);
        lv.setAdapter(listViewadapter);
    }
}
