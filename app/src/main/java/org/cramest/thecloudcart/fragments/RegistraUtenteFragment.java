package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;

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

        registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = ((TextView)getActivity().findViewById(R.id.username_edit_text)).getText().toString();
                final String nome = ((TextView)getActivity().findViewById(R.id.name_edit_text)).getText().toString();
                final String password = ((TextView)getActivity().findViewById(R.id.password_edit_text)).getText().toString();
                String ripetiPassword = ((TextView)getActivity().findViewById(R.id.repeat_password_edit_text)).getText().toString();
                String email = ((TextView)getActivity().findViewById(R.id.email_edit_text)).getText().toString();

                final String[] parametri = {"req","username","name","password","email"};
                final String[] valori = {"register",username,nome,password,email};

                if(password.equals(ripetiPassword)) {
                    LoadingOverlayHandler.mostraLoading(getActivity());
                    Connettore.getInstance(getActivity()).GetDataFromWebsite(new DataHandler() {
                        @Override
                        public void HandleData(String nome, boolean success, String data) {
                            LoadingOverlayHandler.nascondiLoading(getActivity());
                            if(success){
                                Toast.makeText(getActivity(),"Registazione effettuata con successo",Toast.LENGTH_SHORT).show();
                                OnRegistraSuccess(username, data, nome, password);
                            }else{
                                Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                                OnRegistraFailed();
                            }
                        }
                    }, "registrazione", parametri, valori);
                }else{
                    Toast.makeText(getActivity(),"Le due password non coincidono",Toast.LENGTH_SHORT).show();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnRegistraFragmentListener) {
            mListener = (OnRegistraFragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnRegistraFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void OnRegistraSuccess(String username, String userID, String nameUser, String password) {
        if(mListener != null) {
            mListener.OnRegistraSuccess(username, userID, nameUser, password);
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
        void OnRegistraSuccess(String username, String userID, String nameUser, String password);
        void OnRegistraFailed();
    }

}
