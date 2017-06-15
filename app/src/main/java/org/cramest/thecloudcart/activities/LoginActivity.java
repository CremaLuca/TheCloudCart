package org.cramest.thecloudcart.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.classi.LoadingOverlayHandler;
import org.cramest.thecloudcart.fragments.LoginFragment;
import org.cramest.thecloudcart.fragments.RegistraUtenteFragment;
import org.cramest.thecloudcart.network.LoginApp;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener,LoginApp.OnLoginAppListener,RegistraUtenteFragment.OnRegistraFragmentListener{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mostraFragmentLogin();
    }

    private void mostraFragmentLogin(){
        LoginFragment loginFragment = LoginFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, loginFragment);

        // Commit the transaction
        transaction.commit();
    }

    private void mostraFragmentRegistraUtente(){
        RegistraUtenteFragment registerFragment = RegistraUtenteFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, registerFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void OnLogin(String username, String userID, String nameUser) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        i.putExtra("userID", userID);
        i.putExtra("nameUser", nameUser);
        startActivity(i);
        finish();
    }

    @Override
    public void OnCreateNewUser() {
        mostraFragmentRegistraUtente();
    }

    @Override
    public void OnLoginSuccess(String username, String userID, String nameUser) {
        LoadingOverlayHandler.nascondiLoading(this);
        OnLogin(username, userID, nameUser);
    }

    @Override
    public void OnLoginFailed() {
        LoadingOverlayHandler.nascondiLoading(this);
        //TODO : Cosa succede se il login fallisce? Riprova?
    }

    @Override
    public void OnRegistraSuccess(String username, String userID, String nameUser, String password) {
        System.out.println("LoginActivity - Registrato con successo " + username);
        //mostraFragmentLogin(); Non mostriamo la pagina di login ma facciamo direttamente l'accesso
        //Salviamo i dati di accesso così non li chiede più la prossima volta
        LoginApp.SaveLoginData(this, username, userID, nameUser, password);
        OnLogin(username, userID, nameUser);
    }

    @Override
    public void OnRegistraFailed() {
        LoadingOverlayHandler.nascondiLoading(this);
    }
}
