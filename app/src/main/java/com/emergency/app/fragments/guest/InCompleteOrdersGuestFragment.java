package com.emergency.app.fragments.guest;


import android.graphics.PointF;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.adapters.guest.EmployeeAdapter;
import com.emergency.app.adapters.guest.OrderGuestAdapter;
import com.emergency.app.databinding.FragmentInCompleteOrdersGuestBinding;
import com.emergency.app.models.Request;
import com.emergency.app.models.User;
import com.emergency.app.ui.guest.EmployeeActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class InCompleteOrdersGuestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FragmentInCompleteOrdersGuestBinding bind;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mData;
    private OrderGuestAdapter adapter;
    private ArrayList<Request> listOrder=new ArrayList<>();
    private User user;
    private boolean isGetData;

    public InCompleteOrdersGuestFragment() {
        // Required empty public constructor
    }


    public static InCompleteOrdersGuestFragment newInstance() {
        InCompleteOrdersGuestFragment fragment = new InCompleteOrdersGuestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_in_complete_orders_guest, container, false);
        View view = bind.getRoot();
        initViews();
        getData();
        return view;
    }
    private void initViews()
    {
        try {
            user=(User) SharedPrefHelper.getSharedOBJECT(getActivity(),SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            bind.swipe.setOnRefreshListener(this);
            ViewCompat.setNestedScrollingEnabled(bind.rvOrder, false);

            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false)
            {
                @Override
                public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position)
                {
                    // A good idea would be to create this instance in some initialization method, and just set the target position in this method.
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity().getApplicationContext())
                    {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition)
                        {
                            int yDelta = AppConfigHelper.calculateCurrentDistanceToPosition(mLayoutManager,targetPosition);
                            return new PointF(0, yDelta);
                        }
                        // This is the important method. This code will return the amount of time it takes to scroll 1 pixel.
                        // This code will request X milliseconds for every Y DP units.
                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics)
                        {

                            return displayMetrics.widthPixels / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, displayMetrics.heightPixels, displayMetrics);
                        }
                    };
                    smoothScroller.setTargetPosition(position);

                    startSmoothScroll(smoothScroller);
                }
            };

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRefresh() {
        try {
            bind.swipe.setRefreshing(false);
            listOrder.clear();
            adapter.notifyDataSetChanged();
            isGetData=false;
            getData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getData()
    {
        try {
            mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!isGetData) {
                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            String reqStatus=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.STATUS_REQUEST_FIELD).getValue());
                            if (Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_ID_FIELD).getValue())) == user.getId()
                                    &&(!reqStatus.equals(AppConfigHelper.FINISH_STATUS) &&!reqStatus.equals(AppConfigHelper.REJECT_STATUS))) {
                                int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_REQUEST_FIELD).getValue()));
                                int guestID = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_ID_FIELD).getValue()));
                                int empoyeeId = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMPLOYEE_ID_FIELD).getValue()));
                                String address = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ADDRESS_REQUEST_FIELD).getValue());
                                String desc = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DESCRIPTION_REQUEST_FIELD).getValue());
                                String guestType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_TYPE_FIELD).getValue());
                                String requestType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REQUEST_TYPE_FIELD).getValue());
                                String report = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REPORT_FIELD).getValue());
                                String dateTime = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DATETIME_REQUEST_FIELD).getValue());
                                String status = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.STATUS_REQUEST_FIELD).getValue());
                                String price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_REQUEST_FIELD).getValue());
                                String mobile = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_MOBILE_FIELD).getValue());
                                String job = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_JOB_FIELD).getValue());
                                String employeeName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_NAME_FIELD).getValue());
                                String guestName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_NAME_FIELD).getValue());
                                String employeePhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMPLOYEE_PHOTO_FIELD).getValue());
                                String guestPhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_PHOTO_FIELD).getValue());
                                Request objData = new Request(id, requestType, address, guestType, desc, report, dateTime, status, mobile, job, employeeName, guestName, employeePhoto, guestPhoto, guestID, empoyeeId, price);
                                listOrder.add(objData);
                                //fill recycler view
                                bind.rvOrder.setLayoutManager(mLayoutManager);
                                adapter = new OrderGuestAdapter(getActivity(), listOrder);
                                bind.rvOrder.setLayoutManager(mLayoutManager);
                                bind.rvOrder.setAdapter(adapter);
                                bind.progress.setVisibility(View.GONE);
                                bind.lyNoData.lyNoData.setVisibility(View.GONE);
                            }
                        }
                        isGetData=true;
                        if (listOrder.size() == 0) {
                            hideViews();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideViews();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void hideViews()
    {
        try {
            bind.rvOrder.setVisibility(View.GONE);
            bind.progress.setVisibility(View.GONE);
            bind.lyNoData.lyNoData.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
