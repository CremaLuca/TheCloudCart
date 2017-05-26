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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
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
        Button registra = (Button)getActivity().findViewById(R.id.register_button);
        final String[] parametri = {"req","username","password","email"};
        final String[] valori = {"register",};
        registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = ((TextView)getActivity().findViewById(R.id.username_edit_text)).getText().toString();
                final String password = ((TextView)getActivity().findViewById(R.id.password_edit_text)).getText().toString();
                String ripetiPassword = ((TextView)getActivity().findViewById(R.id.repeat_password_edit_text)).getText().toString();
                String email = ((TextView)getActivity().findViewById(R.id.email_edit_text)).getText().toString();
                if(password.equals(ripetiPassword)) {
                    Connettore.getInstance(getContext()).GetDataFromWebsite(new DataHandler() {
                        @Override
                        public void HandleData(String nome, boolean success, String data) {
                            if(success){
                                Toast.makeText(getContext(),"Registazione effettuata con successo",Toast.LENGTH_SHORT).show();
                                OnRegistraSuccess(username,data,password);
                            }else{
                                Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();
                                OnRegistraFailed();
                            }
                        }
                    }, "registrazione", parametri, valori);
                }else{
                    Toast.makeText(getContext(),"Le due password non coincidono",Toast.LENGTH_SHORT).show();
                }
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

    public void OnRegistraSuccess(String username, String userID,String password) {
        if(mListener != null) {
            mListener.OnRegistraSuccess(username, userID,password);
        }else{
            throw new RuntimeException("missing OnRegistraFragmentListener");
        }
    }
    public void OnRegistraFailed(){
        if(mListener != null) {
            mListener.OnRegistraFailed();
        }else{
            throw new RuntimeException("missing OnRegistraFragmentListener");
        }
    }

    public interface OnRegistraFragmentListener {
        void OnRegistraSuccess(String username, String userID,String password);
        void OnRegistraFailed();
    }

}
