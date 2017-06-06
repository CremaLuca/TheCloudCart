package org.cramest.thecloudcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Utente;

import java.util.ArrayList;

/**
 * Created by User on 19/05/2017.
 */

public class UtenteAdapter extends ArrayAdapter<Utente> {

    private ArrayList<Utente> utenti;

    public UtenteAdapter(Context context, int textViewResourceId, ArrayList<Utente> objects) {
        super(context, textViewResourceId, objects);
        this.utenti = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_utente, null);
        }

        Utente i = utenti.get(position);
        if (i != null) {
            //Impostiamo nome e username
            TextView nomeUtente = (TextView) v.findViewById(R.id.text_view_quantita);
            if (nomeUtente != null) {
                nomeUtente.setText(i.getUsername());
            }
            TextView nome = (TextView) v.findViewById(R.id.text_view_nome);
            if (nome != null) {
                nome.setText(i.getNome());
            }
        }

        return v;

    }

}
