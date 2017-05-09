package org.cramest.thecloudcart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import org.cramest.thecloudcart.fragments.LoginFragment;
import org.cramest.thecloudcart.fragments.RegistraUtenteFragment;
import org.cramest.thecloudcart.network.LoginApp;
import org.cramest.thecloudcart.R;

public class LoginActivity extends FragmentActivity implements LoginFragment.OnLoginFragmentListener,LoginApp.OnLoginAppListener,RegistraUtenteFragment.OnRegistraFragmentListener{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mostraFragmentLogin();
    }

    private void mostraFragmentLogin(){
        LoginFragment loginFragment = LoginFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void mostraFragmentRegistraUtente(){
        RegistraUtenteFragment registerFragment = RegistraUtenteFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, registerFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void OnLogin(String username, String userID) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        i.putExtra("userID", userID);
        startActivity(i);
        finish();
    }

    @Override
    public void OnCreateNewUser() {
        mostraFragmentRegistraUtente();
    }

    @Override
    public void OnLoginSuccess(String username, String userID) {
        //TODO : Controllo se mListener esiste e il fragmentActivity contiene il listener OnLoginFragmentListener
        OnLogin(username,userID);
    }

    @Override
    public void OnLoginFailed() {
        //TODO : Cosa succede se il login fallisce? Riprova?
    }

    @Override
    public void OnRegistraSuccess(String username, String userID) {
        mostraFragmentLogin();
    }

    @Override
    public void OnRegistraFailed() {

    }
}
