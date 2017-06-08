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
    private ListsPageAdapter mAdapter;
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
        System.out.println("ListsFragment - onCreate");
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_lists, container, false);

        mViewPager = (ViewPager) result.findViewById(R.id.viewpager);
        mAdapter = new ListsPageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout) result.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return result;
    }



}
