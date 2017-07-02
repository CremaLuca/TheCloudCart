package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.Lista;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AggiungiListaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AggiungiListaFragment extends Fragment {

    private static final String ARG_PARAM = "userID";

    private String userID;

    private OnAggiungiListaFragmentInteractionListener mListener;
    private String nomelista;

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
        return inflater.inflate(R.layout.fragment_aggiungi_lista, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ((Button)getActivity().findViewById(R.id.buttonCreaLista)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomelista = ((EditText)getActivity().findViewById(R.id.editTextNomeLista)).getText().toString();
                //Aggiungiamo la lista tramite l'API
                String[] parametri = {"req","userID","listName"};
                String[] valori = {"addList",userID,nomelista};
                if(Connettore.getInstance(getActivity()).isNetworkAvailable()) {
                    //Chiediamo al sito le liste
                    Connettore.getInstance(getActivity()).GetDataFromWebsite(new DataHandler() {
                        @Override
                        public void HandleData(String nomeRichiesta, boolean success, String data) {
                            if (success) {
                                Toast.makeText(getActivity(), "Lista creata con successo", Toast.LENGTH_SHORT).show();
                                //data in questo caso sarà l'id della lista
                                onListaAggiunta(new Lista(Integer.parseInt(data), nomelista, 0));
                            } else {
                                Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                                onListaNonAggiunta();
                            }
                        }
                    }, "aggiungiLista", parametri, valori);
                    LoadingOverlayHandler.mostraLoading(getActivity());
                    //TODO : Aprire la activity condividi per condividere la lista con gli amici
                }else{
                    //TODO : Aggiunta liste in locale e aggiunta alla lista di cose da aggiornare
                }
            }
        });
    }

    public void onListaAggiunta(Lista lista) {
        if (mListener != null) {
            mListener.onListaAggiunta(lista);
        }
    }
    public void onListaNonAggiunta() {
        if (mListener != null) {
            mListener.onListaNonAggiunta();
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof OnAggiungiListaFragmentInteractionListener) {
            mListener = (OnAggiungiListaFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAggiungiListaFragmentInteractionListener) {
            mListener = (OnAggiungiListaFragmentInteractionListener) context;
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

    public interface OnAggiungiListaFragmentInteractionListener {
        void onListaAggiunta(Lista lista);
        void onListaNonAggiunta();
    }
}
