package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.activities.AggiungiListaCondividiActivity;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AggiungiListaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AggiungiListaFragment extends Fragment implements DataHandler{

    private static final String ARG_PARAM = "userID";

    private String userID;

    private OnAggiungiListaListener mListener;

    public AggiungiListaFragment() {
        // Required empty public constructor
    }

    /** TODO : Rifare documentazione
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userID Parameter 1.
     * @return A new instance of fragment AggiungiListaFragment.
     */
    public static AggiungiListaFragment newInstance(String userID) {
        AggiungiListaFragment fragment = new AggiungiListaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aggiungi_lista, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ((Button)getActivity().findViewById(R.id.buttonCreaLista)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeLista = ((EditText)getActivity().findViewById(R.id.editTextNomeLista)).getText().toString();
                //Generiamo l'intent

                //Aggiungiamo la lista tramite l'API
                String[] parametri = {"req","userID","listName"};
                String[] valori = {"addList",userID,nomeLista};
                if(Connettore.getInstance(getActivity()).isNetworkAvailable()) {
                    //Chiediamo al sito le liste
                    Connettore.getInstance(getActivity()).GetDataFromWebsite(AggiungiListaFragment.this, "aggiungiLista", parametri, valori);
                    //TODO : Aprire la activity condividi per condividere la lista con gli amici
                }else{
                    //TODO : Aggiunta liste in locale e aggiunta alla lista di cose da aggiornare
                }
            }
        });
    }

    public void onListaAggiunta() {
        if (mListener != null) {
            mListener.onAggiungiLista();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAggiungiListaListener) {
            mListener = (OnAggiungiListaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void HandleData(String nome, boolean success, String data) {
        if(success) {
            Toast.makeText(getContext(),data, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),data, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnAggiungiListaListener {
        void onAggiungiLista(/* TODO : IDLista, così da poter aggiornare la lista senza forzare tutto l'aggiornamento*/);
    }
}
