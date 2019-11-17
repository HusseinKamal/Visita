package com.emergency.app.fragments.guest;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.emergency.app.R;
import com.emergency.app.adapters.OrdersPagerAdapter;
import com.emergency.app.databinding.FragmentOrderBinding;
import com.google.android.material.tabs.TabLayout;

public class OrderGuestFragment extends Fragment {
    FragmentOrderBinding bind;
    public OrderGuestFragment() {
    }

    public static OrderGuestFragment newInstance() {
        OrderGuestFragment fragment = new OrderGuestFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        View view = bind.getRoot();
        setUpViewPager();
        return view;
    }
    private void setUpViewPager()
    {
        try {
            //Add Tabs Items
            bind.mainTabs.removeAllTabs();
            bind.mainTabs.addTab(bind.mainTabs.newTab().setText(getString(R.string.incomplete_order)),0);
            bind.mainTabs.addTab(bind.mainTabs.newTab().setText(getString(R.string.complete_order)),1);
            bind.mainTabs.setTabGravity(TabLayout.GRAVITY_FILL);
            bind.mainTabs.setSelected(true);

            OrdersPagerAdapter adapter = new OrdersPagerAdapter(getFragmentManager(), bind.mainTabs.getTabCount(),true);
            bind.tabPager.setAdapter(adapter);
            bind.tabPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bind.mainTabs));
            bind.tabPager.setCurrentItem(0);
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }

    }

}
