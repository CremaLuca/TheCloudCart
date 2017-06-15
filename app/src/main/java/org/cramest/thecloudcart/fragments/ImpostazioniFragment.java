package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.cramest.thecloudcart.R;

public class ImpostazioniFragment extends Fragment {

    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "nameUser";

    private String nameUser;
    private String userID;

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
        EditText nome = (EditText) view.findViewById(R.id.edit_nome);
        nome.setText(nameUser);
        System.out.println("DEBUG ImpostazioniFragment - Ciao " + nameUser);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

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
