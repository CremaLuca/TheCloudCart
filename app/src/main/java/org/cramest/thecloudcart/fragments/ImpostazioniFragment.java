package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.cramest.thecloudcart.R;

/**
 * Created by CREMALUCA on 12/05/2017.
 */
public class ImpostazioniFragment extends Fragment {

    private static final String ARG_PARAM = "userID";

    private String userID;

    public ImpostazioniFragment() {
    }

    public static ImpostazioniFragment newInstance(String userID) {
        ImpostazioniFragment fragment = new ImpostazioniFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM,userID);
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
        return inflater.inflate(R.layout.fragment_impostazioni, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Button aggiungi = (Button)getActivity().findViewById(R.id.crea_prodotto_button);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
