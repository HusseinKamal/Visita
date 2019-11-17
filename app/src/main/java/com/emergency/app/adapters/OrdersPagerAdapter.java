package com.emergency.app.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.emergency.app.fragments.emploee.CompleteOrdersEmployeeFragment;
import com.emergency.app.fragments.emploee.InCompleteOrdersEmployeeFragment;
import com.emergency.app.fragments.guest.CompleteOrdersGuestFragment;
import com.emergency.app.fragments.guest.InCompleteOrdersGuestFragment;

public class OrdersPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    boolean isGuest;
    public OrdersPagerAdapter(FragmentManager fm, int NumOfTabs, boolean isGuest) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.isGuest=isGuest;
    }

    @Override
    public Fragment getItem(int position) {
        if(isGuest) {
            switch (position) {
                case 0:
                    InCompleteOrdersGuestFragment incomplete = InCompleteOrdersGuestFragment.newInstance();
                    return incomplete;
                case 1:
                    CompleteOrdersGuestFragment complete = CompleteOrdersGuestFragment.newInstance();
                    return complete;
                default:
                    return null;
            }
        }
        else
        {
            switch (position) {
                case 0:
                    InCompleteOrdersEmployeeFragment incomplete = InCompleteOrdersEmployeeFragment.newInstance();
                    return incomplete;
                case 1:
                    CompleteOrdersEmployeeFragment complete = CompleteOrdersEmployeeFragment.newInstance();
                    return complete;
                default:
                    return null;
            }
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
