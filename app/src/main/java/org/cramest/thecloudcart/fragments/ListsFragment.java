package org.cramest.thecloudcart.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cramest.thecloudcart.R;
import org.cramest.thecloudcart.adapter.ListsPageAdapter;

public class ListsFragment extends Fragment{

    private static final String ARG_PARAM = "userID";
    private ListsPageAdapter mListsPageAdapter;
    private ViewPager mViewPager;
    private String userID;

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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lists, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        ListsPageAdapter pageAdapter = new ListsPageAdapter
                (getActivity().getFragmentManager());
        mViewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
    }

}
