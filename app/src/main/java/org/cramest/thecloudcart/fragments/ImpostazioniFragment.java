package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cramest.thecloudcart.R;

/**
 * Created by CREMALUCA on 12/05/2017.
 */
public class ImpostazioniFragment extends Fragment {

    private static final String ARG_PARAM = "userID";

    public ImpostazioniFragment() {
    }

    public static ImpostazioniFragment newInstance() {
        ImpostazioniFragment fragment = new ImpostazioniFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
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
