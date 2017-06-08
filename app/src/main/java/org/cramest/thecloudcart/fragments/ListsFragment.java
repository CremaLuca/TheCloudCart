package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.ListaAdapter;
import org.cramest.thecloudcart.classi.Dati;
import org.cramest.thecloudcart.classi.Lista;

import java.util.ArrayList;

public class ListsFragment extends Fragment{

    private static final String ARG_PARAM = "userID";

    public ListsFragment(){

    }

    public static ListsFragment newInstance(String userID) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

}
