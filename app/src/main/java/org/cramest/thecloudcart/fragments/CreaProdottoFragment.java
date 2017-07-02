package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Categoria;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;

import java.util.ArrayList;

public class CreaProdottoFragment extends Fragment{

    private static final String ARG_PARAM = "listID";

    private int listID;

    private OnCreaProdottiListener mListener;
    private int curCategoriaID;

    public CreaProdottoFragment() {

    }

    public static CreaProdottoFragment newInstance(int listID) {
        CreaProdottoFragment fragment = new CreaProdottoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listID = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crea_prodotto, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //TODO : Fai qui
        setCategorieSpinner();
        Button aggiungi = (Button)getActivity().findViewById(R.id.crea_prodotto_button);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generaProdotto();
            }
        });
    }

    private void generaProdotto(){

        EditText editNome = (EditText) getActivity().findViewById(R.id.edit_nome);
        EditText editPrezzo = (EditText) getActivity().findViewById(R.id.edit_prezzo);
        EditText editMarca = (EditText) getActivity().findViewById(R.id.edit_marca);
        EditText editDimensione = (EditText) getActivity().findViewById(R.id.edit_descrizione);
        EditText editQuantita = (EditText) getActivity().findViewById(R.id.edit_quantita_crea);
        EditText editDescrizione = (EditText) getActivity().findViewById(R.id.edit_descrizione_crea);

        Double prezzo = 0.0;
        int quantita = 1;

        //Prima recuperiamo tutti i dati
        String nome = editNome.getText().toString();
        String marca = editMarca.getText().toString();
        String dimensione = editDimensione.getText().toString();
        Categoria categoria = Dati.getCategoriaByID(curCategoriaID);
        String descrizione = editDescrizione.getText().toString();
        try {
            prezzo = Double.parseDouble(editPrezzo.getText().toString());
            quantita = Integer.parseInt(editQuantita.getText().toString());
        } catch (Exception e) {
            System.out.println("CreaProdottoFragment - Manca prezzo o quantita, ma non importa");
        }
        //TODO : Controlli sui dati inseriti
        int idCasuale = 66666;
        Prodotto prod = new Prodotto(idCasuale, nome, prezzo, marca, dimensione, categoria);
        ProdottoInLista prodottoInLista = new ProdottoInLista(listID, prod, quantita, descrizione);
        mListener.OnDevoCreareProdotto(prodottoInLista);

    }

    private void setCategorieSpinner(){

        Spinner spinner = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
        //Recuperiamo i nomi delle categorie
        ArrayList<String> stringCategorie = Dati.getCategorieAsString();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stringCategorie);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        //Le categorie sono in ordine di ID quindi l'ID coincide con la posizione+1 all'interno dello spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                curCategoriaID = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnCreaProdottiListener) {
            mListener = (OnCreaProdottiListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnCreaProdottiListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreaProdottiListener) {
            mListener = (OnCreaProdottiListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreaProdottiListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCreaProdottiListener {
        void OnDevoCreareProdotto(ProdottoInLista prodottoInLista);
    }
}
