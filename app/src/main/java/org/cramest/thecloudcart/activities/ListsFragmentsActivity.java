package org.cramest.thecloudcart.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.fragments.ListsFragment;

public class ListsFragmentsActivity extends FragmentActivity implements ListsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_fragments);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
