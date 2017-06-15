package org.cramest.thecloudcart.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.cramest.thecloudcart.R;

public class ListsFragment extends Fragment{

    private static final String ARG_PARAM = "userID";

    private String userID;
    private OnListFragmentInteractionListener mListener;

    private AdView mAdView;

    public ListsFragment() {
        // Required empty public constructor
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
        //Lo chiamo qui così viene chiamato una volta sola teoricamente
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_PARAM);
        }

    }

    private void mostraFragmentListeMie() {
        if (getActivity().findViewById(R.id.fragment_liste_container) != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction().addToBackStack("ListeMie");
            transaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
            transaction.replace(R.id.fragment_liste_container, ListeMieFragment.newInstance(userID));
            transaction.commit();
        } else {
            System.out.println("ListsActivity - Manca il fragment container");
        }
    }

    private void mostraFragmentListCondivise() {
        if (getActivity().findViewById(R.id.fragment_liste_container) != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction().addToBackStack("ListeCondivise");
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);
            transaction.replace(R.id.fragment_liste_container, ListeCondiviseFragment.newInstance(userID));
            transaction.commit();
        } else {
            System.out.println("ListsActivity - Manca il fragment container");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mostraFragmentListeMie();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ((Button) view.findViewById(R.id.button_mie)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraFragmentListeMie();
            }
        });
        ((Button) view.findViewById(R.id.button_condivise)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraFragmentListCondivise();
            }
        });
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("FE3059E0490D901C37DCB894C93DFCC0")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mAdView.getVisibility() == View.GONE) {
                    mAdView.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        System.out.println("ListFragment - Chiamato onAttach(Activity)");
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            System.out.println("ListFragment - impostato mListener");
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("ListFragment - Chiamato onAttach(context)");
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public interface OnListFragmentInteractionListener {

    }
}
