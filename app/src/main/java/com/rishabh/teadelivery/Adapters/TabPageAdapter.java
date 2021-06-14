package com.rishabh.teadelivery.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rishabh.teadelivery.DeliveredTab;
import com.rishabh.teadelivery.OrderedTab;

public class TabPageAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public TabPageAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm, tabCount);
        this.tabCount = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OrderedTab orderedTab = new OrderedTab();
                return orderedTab;
            case 1:
                DeliveredTab deliveredTab = new DeliveredTab();
                return deliveredTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
