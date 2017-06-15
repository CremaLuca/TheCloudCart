package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

public class ImpostazioniFragment extends Fragment {

    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "nameUser";

    private String nameUser;
    private String userID;

    private OnImpostazioniFragmentListener mListener;

    public ImpostazioniFragment() {
    }

    public static ImpostazioniFragment newInstance(String userID, String nameUser) {
        ImpostazioniFragment fragment = new ImpostazioniFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putString(ARG_PARAM2, nameUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM1);
            nameUser = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_impostazioni, container, false);
        final EditText nome = (EditText) view.findViewById(R.id.edit_nome);
        nome.setText(nameUser);
        view.findViewById(R.id.btn_salva_impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nomeUtente = nome.getText().toString();

                String[] parametri = {"req", "userID", "name"};
                String[] valori = {"changeName", userID, nomeUtente};


                Connettore.getInstance(getActivity()).GetDataFromWebsite(new DataHandler() {
                    @Override
                    public void HandleData(String nome, boolean success, String data) {
                        if (success) {
                            nameUser = nomeUtente;
                            Toast.makeText(getActivity(), "Modificato nome con successo", Toast.LENGTH_SHORT).show();
                            mListener.OnCambioNomeEseguito(nomeUtente);
                        } else {
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, "cambioNome", parametri, valori);
                mListener.OnRichiestoCambioNome(nomeUtente);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnImpostazioniFragmentListener) {
            mListener = (OnImpostazioniFragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnImpostazioniFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImpostazioniFragmentListener) {
            mListener = (OnImpostazioniFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImpostazioniFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnImpostazioniFragmentListener {
        void OnRichiestoCambioNome(String nome);

        void OnCambioNomeEseguito(String nome);
    }

}
