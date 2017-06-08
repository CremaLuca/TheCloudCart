package org.cramest.thecloudcart.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private TabLayout tabLayout;
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

        tabLayout = (TabLayout) result.findViewById(R.id.tabs);

        return result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ListsPageAdapter(getChildFragmentManager(), userID);
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        System.out.println("ListsFragment - Item corrente : " + mViewPager.getCurrentItem());
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ListsFragment - onResume");
        mViewPager.setCurrentItem(0);
        mAdapter.notifyDataSetChanged();
        System.out.println("ListsFragment - " + mAdapter.getItem(1));
    }

}
