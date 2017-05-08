package org.cramest.thecloudcart.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.LoginApp;

/**
 * Created by User on 08/05/2017.
 */

public class RegistraUtenteFragment extends Fragment{

    private OnRegistraFragmentListener mListener;

    GoogleApiClient mGoogleApiClient;

    public RegistraUtenteFragment() {
        // Required empty public constructor
    }

    /** TODO : documentazione da rifare
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */

    public static RegistraUtenteFragment newInstance() {
        RegistraUtenteFragment fragment = new RegistraUtenteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrazione, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //TODO : Fai qui
        Button registra = (Button)getActivity().findViewById(R.id.register_button);
        registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegistraFragmentListener) {
            mListener = (OnRegistraFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegistraFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void OnRegistraSuccess(String username, String userID) {
        //TODO : Controllo se mListener esiste e il fragmentActivity contiene il listener OnLoginFragmentListener
        mListener.OnRegistraSuccess(username,userID);
    }

    public interface OnRegistraFragmentListener {
        void OnRegistraSuccess(String username, String userID);
        void OnRegistraFailed();
    }

}
