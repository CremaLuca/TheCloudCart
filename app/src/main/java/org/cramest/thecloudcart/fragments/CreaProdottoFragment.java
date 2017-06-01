package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Categoria;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreaProdottoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreaProdottoFragment extends Fragment{

    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "listID";

    private int listID;
    private String userID;

    private Prodotto tmpProdotto;

    private OnCreaProdottiListener mListener;
    private int curCategoriaID;

    public CreaProdottoFragment() {
        // Required empty public constructor
    }

    /** TODO : Rifare documentazione
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listID Parameter 1.
     * @return A new instance of fragment AggiungiListaFragment.
     */
    public static CreaProdottoFragment newInstance(String userID, int listID) {
        CreaProdottoFragment fragment = new CreaProdottoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putInt(ARG_PARAM2, listID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM1);
            listID = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        //Prima recuperiamo tutti i dati, l'ID dopo
        String nome = ((EditText) getActivity().findViewById(R.id.edit_nome)).getText().toString();
        Double prezzo = Double.parseDouble(((EditText) getActivity().findViewById(R.id.edit_prezzo)).getText().toString());
        String marca = ((EditText) getActivity().findViewById(R.id.edit_marca)).getText().toString();;
        String dimensione = ((EditText) getActivity().findViewById(R.id.edit_descrizione)).getText().toString();;
        Categoria categoria = Dati.getCategoriaByID(curCategoriaID);
        int quantita = Integer.parseInt(((EditText) getActivity().findViewById(R.id.edit_quantita_crea)).getText().toString());
        String descrizione = ((EditText) getActivity().findViewById(R.id.edit_descrizione_crea)).getText().toString();
        //TODO : Controlli sui dati inseriti
        mListener.OnDevoCreareProdotto(nome,prezzo,marca,dimensione,categoria,quantita,descrizione,listID);

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
        void OnDevoCreareProdotto(String nome,Double prezzo,String marca,String dimensione,Categoria categoria,int quantita,String descrizione, int idLista);
    }
}
