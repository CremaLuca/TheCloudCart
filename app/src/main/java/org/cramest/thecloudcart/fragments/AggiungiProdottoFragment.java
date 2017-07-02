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
import android.widget.TextView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.classi.Prodotto;
import org.cramest.thecloudcart.classi.ProdottoInLista;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AggiungiProdottoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AggiungiProdottoFragment extends Fragment {

    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "listID";
    private static final String ARG_PARAM3 = "productID";

    private int listID;
    private Lista curList;
    private String userID;

    private OnAggiungiProdottiListener mListener;

    private ArrayList<Prodotto> curProdottiByCategoria;
    private Prodotto curProdottoSel = null;
    private Prodotto initialProdotto = null;

    private ProdottoInLista tmpProdottoInLista;

    public AggiungiProdottoFragment() {
        // Required empty public constructor
    }

    /** TODO : Rifare documentazione
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listID Parameter 1.
     * @return A new instance of fragment AggiungiListaFragment.
     */
    public static AggiungiProdottoFragment newInstance(String userID,int listID) {
        AggiungiProdottoFragment fragment = new AggiungiProdottoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putInt(ARG_PARAM2, listID);
        fragment.setArguments(args);
        return fragment;
    }

    public static AggiungiProdottoFragment newInstance(String userID,int listID,int productID) {
        AggiungiProdottoFragment fragment = new AggiungiProdottoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putInt(ARG_PARAM2, listID);
        args.putInt(ARG_PARAM3, productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM1);
            listID = getArguments().getInt(ARG_PARAM2);
            curList = Dati.getListaByID(listID);
            int productID = getArguments().getInt(ARG_PARAM3);
            if(productID != 0 && productID>0) {
                initialProdotto = Dati.getProdottoByID(getArguments().getInt(ARG_PARAM3));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aggiungi_prodotto_lista, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        TextView titolo =(TextView)getActivity().findViewById(R.id.title_aggiungi_prodotto);
        titolo.setText(getString(R.string.Add_to_list) + ":" + curList.getNome());
        setCategorieSpinner();
        updatePodottoSpinner(0);
        if(initialProdotto != null){
            Spinner spinnerCategorie = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
            spinnerCategorie.setSelection(initialProdotto.getCategoria().getID()-1);
        }

        Button aggiungi = (Button)getActivity().findViewById(R.id.aggiungi_prodotto_button);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggiungiProdottoInLista(curProdottoSel);
            }
        });

        Button crea = (Button)getActivity().findViewById(R.id.button_crea_prodotto);
        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDevoCreareNuovoProdotto(listID);
            }
        });
    }

    private void aggiungiProdottoInLista(Prodotto prodotto){
        if(prodotto != null) {
            String descrizione = ((EditText) getActivity().findViewById(R.id.descrizione)).getText().toString();
            int quantita = 1;
            try {
                quantita = Integer.parseInt(((EditText) getActivity().findViewById(R.id.quantita)).getText().toString());
            } catch (Exception e) {
                System.out.println("AggiungiProdottoFragment - Manca la quantità, non importa");
            }
            tmpProdottoInLista = new ProdottoInLista(listID, prodotto, quantita, descrizione);

            //TODO : Delegare il tutto alla main activity e alla classe dati

            //Aggiungiamo la lista tramite l'API
            String[] parametri = {"req", "userID", "listID", "productID", "quantity", "description"};
            String[] valori = {"addProduct", userID, listID + "", prodotto.getID() + "", quantita + "", descrizione};
            if (Connettore.getInstance(getActivity()).isNetworkAvailable()) {
                LoadingOverlayHandler.mostraLoading(getActivity());
                //Chiediamo al sito di creare il prodotto
                Connettore.getInstance(getActivity()).GetDataFromWebsite(new DataHandler() {
                    @Override
                    public void HandleData(String nomeRichiesta, boolean success, String data) {
                        if (success) {
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                            if (tmpProdottoInLista != null) {
                                onProdottoAggiunto(tmpProdottoInLista);
                            }
                        } else {
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                            if (tmpProdottoInLista != null) {
                                onProdottoNonAggiunto(tmpProdottoInLista);
                            }
                        }
                    }
                }, "aggiungiProdotto", parametri, valori);
            } else {
                //TODO : Aggiunta prodotto in locale alla lista degli aggiornamenti
            }

        }else{
            System.out.println("AggiungiProdottoFragment - Nessun prodotto selezionato");
            Toast.makeText(getActivity(), "Nessun prodotto selezionato", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCategorieSpinner(){

        Spinner spinner = (Spinner)getActivity().findViewById(R.id.categoria_spinner);
        //Recuperiamo i nomi delle categorie
        ArrayList<String> stringCategorie = Dati.getCategorieAsString();
        //Questo sposta di uno la posizione delle categorie
        stringCategorie.add(0, getString(R.string.Recommended));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stringCategorie);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        //Le categorie sono in ordine di ID quindi l'ID coincide con la posizione+1 all'interno dello spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //La categoria sarà posizione+1 ma -1 perchè la 0 è consigliati
                updatePodottoSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void updatePodottoSpinner(int idCategoria){
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.prodotto_spinner);
        //Recuperiamo i nomi delle categorie
        curProdottiByCategoria = Dati.getProdottiByCategoria(idCategoria, listID);
        ArrayList<String> prodotti = Dati.prodottiToString(curProdottiByCategoria);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, prodotti);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

        if(initialProdotto != null){
            for(int i=0;i<prodotti.size();i++){
                if(prodotti.get(i).equals(initialProdotto.getNome())){
                    //Impostiamo il prodotto selezionato dove è stata trovata corrispondenza
                    spinner.setSelection(i);
                }
            }
            //Abbiamo già selezionato, adesso non ci serve più e lo eliminiamo
            initialProdotto = null;
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                curProdottoSel = curProdottiByCategoria.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    public void onProdottoAggiunto(ProdottoInLista prodotto) {
        if (mListener != null) {
            mListener.OnProdottoAggiunto(prodotto);
        }
    }

    public void onProdottoNonAggiunto(ProdottoInLista prodotto) {
        if (mListener != null) {
            mListener.OnProdottoNonAggiunto(prodotto);
        }
    }
    public void onDevoCreareNuovoProdotto(int listID) {
        if (mListener != null) {
            mListener.OnDevoCreareNuovoProdotto(listID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAggiungiProdottiListener) {
            mListener = (OnAggiungiProdottiListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAggiungiProdottiListener");
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof OnAggiungiProdottiListener) {
            mListener = (OnAggiungiProdottiListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnAggiungiProdottiListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAggiungiProdottiListener {
        void OnProdottoAggiunto(ProdottoInLista prodottoInLista);

        void OnProdottoNonAggiunto(ProdottoInLista prodottoInLista);
        void OnDevoCreareNuovoProdotto(int listID);
    }
}
