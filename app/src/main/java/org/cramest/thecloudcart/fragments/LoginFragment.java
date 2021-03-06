package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.network.Connettore;
import org.cramest.thecloudcart.network.DataHandler;
import org.cramest.thecloudcart.network.LoginApp;
import org.cramest.utils.DataSaver;

/**
 * Created by User on 08/05/2017.
 */

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    private OnLoginFragmentListener mListener;

    static GoogleApiClient mGoogleApiClient;

    public LoginFragment() {
        // Required empty public constructor
    }

    /** TODO : documentazione da rifare
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage((AppCompatActivity) getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Quando abbiamo almeno una view gli diciamo cosa fare
        getActivity().findViewById(R.id.sign_in_button).setOnClickListener(this);
        getActivity().findViewById(R.id.loginButton).setOnClickListener(this);
        getActivity().findViewById(R.id.text_view_no_account).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnLoginFragmentListener) {
            mListener = (OnLoginFragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentListener) {
            mListener = (OnLoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.loginButton:
                System.out.println("LoginFragment - Bottone login premuto");
                //recuperiamo nome utente e password dalle caselle e li diamo in pasto alla LoginApp che si arrangerà con le connessioni
                String username = ((EditText)getActivity().findViewById(R.id.editUsername)).getText().toString();
                String password = ((EditText)getActivity().findViewById(R.id.editPassword)).getText().toString();
                if(!username.equals("") && !password.equals("")) {
                    LoadingOverlayHandler.mostraLoading(getActivity());
                    System.out.println("LoginFragment - Chiamo loginApp("+username+","+password+")");
                    new LoginApp(getActivity(), username, password);
                }else{
                    System.out.println("LoginFragment - Nessun nome utente o password inseriti");
                    Toast.makeText(getActivity(),"Mancano i dati di accesso",Toast.LENGTH_SHORT).show();
                }
                //Da qui in poi si arrangia con i listener
                break;
            case R.id.text_view_no_account:
                mListener.OnCreateNewUser();
                break;
        }
    }

    private void signIn() {
        System.out.println("LoginFragment - Richiedo il signin con google");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 5);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 5) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        System.out.println("LoginFragment - handleSignInResult:" + result.isSuccess() + " status : " + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            //Estraiamo l'username dal display name
            System.out.println("LoginFragment - Benvenuto " + acct.getEmail());
            String usern = acct.getEmail().split("@")[0];
            final String username = usern;
            System.out.println("LoginFragment - Benvenuto " + usern + " con auth code " + acct.getId());
            String[] parametri = {"req", "username", "name", "email", "googleID"};
            String[] valori = {"googleLogin", username, acct.getGivenName(), acct.getEmail(), acct.getId()};

            Connettore.getInstance(getActivity()).GetDataFromWebsite(new DataHandler() {
                @Override
                public void HandleData(String nome, boolean success, String data) {
                    if (success) {
                        System.out.println("LoginFragment - Login con google con successo anche al sito, data : " + data);
                        SaveLoginData(getActivity(), username, data, acct.getId());
                        String userID = data;
                        //Notifichiamo il fragment che siamo riusciti a fare l'accesso il quale chiamera l'activity che ne caricherà un altra
                        mListener.OnLogin(username, userID, acct.getGivenName());
                    }
                }
            }, "googleLogin", parametri, valori);
        } else {
            System.out.println("LoginFragment - Non oke, errore del codice della app, non registrata :" + result.getStatus().getStatusMessage());
        }
    }

    public static void SaveLoginData(Context c, String username, String userID, String password) {
        DataSaver.getInstance().saveDataString(c, "username", username);
        DataSaver.getInstance().saveDataString(c, "userID", userID);
        DataSaver.getInstance().saveDataString(c, "password", password);//Serve per rifare l'accesso
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public interface OnLoginFragmentListener {
        void OnLogin(String username, String userID, String nomeUser);
        void OnCreateNewUser();
    }

}
