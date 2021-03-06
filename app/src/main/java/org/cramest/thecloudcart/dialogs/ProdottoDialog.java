package org.cramest.thecloudcart.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.classi.ProdottoInLista;


public class ProdottoDialog{

    public static ProdottoDialog instance;

    public void showDialog(final Activity activity, final OnProdottoDialogInteractionListener listener, final ProdottoInLista prodotto) {

         final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_prodotto);

        ((TextView) dialog.findViewById(R.id.nome_prodotto)).setText(prodotto.getProdotto().getNome());
        ((EditText) dialog.findViewById(R.id.edit_marca)).setText(prodotto.getProdotto().getMarca());
        ((EditText) dialog.findViewById(R.id.edit_prezzo)).setText(String.valueOf(prodotto.getProdotto().getPrezzo()));
        ((EditText) dialog.findViewById(R.id.edit_dimensione)).setText(prodotto.getProdotto().getDimensione());
        ((EditText) dialog.findViewById(R.id.edit_quantita)).setText(String.valueOf(prodotto.getQuantita()));
        ((EditText) dialog.findViewById(R.id.edit_descrizione)).setText(prodotto.getDescrizione());


        //Impostiamo cosa deve fare il bottone compra
        Button compraButton = (Button) dialog.findViewById(R.id.button_comprato);
        compraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnCompraProdotto(prodotto);
                dialog.dismiss();
            }
        });
        //E anche cosa deve fare il bottone elimina
        Button eliminaButton = (Button) dialog.findViewById(R.id.button_elimina);
        eliminaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnEliminaProdotto(prodotto);
                dialog.dismiss();
            }
        });

        //TODO : informazioni prodotto modificabili

        instance = this;
        dialog.show();
    }

    public interface OnProdottoDialogInteractionListener{
        void OnCompraProdotto(ProdottoInLista prodotto);
        void OnEliminaProdotto(ProdottoInLista prodotto);
    }
}
