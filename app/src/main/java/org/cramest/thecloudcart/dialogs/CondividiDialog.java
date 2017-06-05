package org.cramest.thecloudcart.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.UtenteAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.classi.Utente;

import java.util.ArrayList;

/**
 * Created by cremaluca on 05/06/2017.
 */

public class CondividiDialog {
    public static CondividiDialog instance;

    public void showDialog(final Activity activity, final Lista lista) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_condividi);

        ((TextView)activity.findViewById(R.id.text_view_titolo_condividi)).setText("Condividi lista " + lista.getNome());
        SearchView searchView = (SearchView)activity.findViewById(R.id.search_view_condividi);
        instance = this;
        dialog.show();
    }

    public interface OnCondividiDialogInteraction{
        void OnListaCondivisa(Utente user);
    }

}
