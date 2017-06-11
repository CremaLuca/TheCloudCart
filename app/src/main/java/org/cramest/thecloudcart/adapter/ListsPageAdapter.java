package org.cramest.thecloudcart.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import org.cramest.thecloudcart.fragments.ListsCondiviseFragment;
import org.cramest.thecloudcart.fragments.ListsMieFragment;

/**
 * Created by User on 08/06/2017.
 */

public class ListsPageAdapter extends FragmentPagerAdapter {

    ListsMieFragment listsMieFragment;
    ListsCondiviseFragment listsCondiviseFragment;
    String userID = "1";

    public ListsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("ListsPageAdapter - Mi hanno chiesto l'item");
        //Possibile farlo anche con uno switch case
        if (listsMieFragment == null) {
            listsMieFragment = ListsMieFragment.newInstance(userID);
            listsCondiviseFragment = ListsCondiviseFragment.newInstance(userID);
        }
        return (position == 0) ? listsMieFragment : listsCondiviseFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mie";
            case 1:
                return "Condivise con me";
            default:
                return null;
        }
    }
}
