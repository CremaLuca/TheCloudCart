package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.fragments.ListsFragment;

public class ListsFragmentsActivity extends FragmentActivity implements ListsFragment.OnListFragmentInteractionListener{

    private String username;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_fragments);
        //recuperiamo nome utente e password dall'intent
        username = getIntent().getExtras().getString("username");
        userID = getIntent().getExtras().getString("userID");
        aggiungiFragment(savedInstanceState);
    }

    private void aggiungiFragment(Bundle savedInstanceState){
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_list_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            //Questo costruttore statico permette di passare i parametri direttamente
            ListsFragment firstFragment = ListsFragment.newInstance(userID);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_list_container, firstFragment).commit();
        }
    }

    @Override
    public void OnListFragmentInteractionListener(int listID) {
        //Questa funzione viene chiamata dal fragment della lista quando viene cliccato qualcosa
        //Nuovo intent per aprire la activity per visualizzare i prodotti in questa lista
        Intent i = new Intent(this, ProdottiActivity.class);
        i.putExtra("IDLista", listID);
        i.putExtra("username", username);
        i.putExtra("userID", userID);
        startActivity(i);
    }
}
