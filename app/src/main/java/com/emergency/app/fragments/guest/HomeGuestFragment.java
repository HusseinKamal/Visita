package com.emergency.app.fragments.guest;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.emergency.app.R;
import com.emergency.app.adapters.guest.EmployeeAdapter;
import com.emergency.app.adapters.guest.RecommendAdapter;
import com.emergency.app.databinding.CategoryFragmentBinding;
import com.emergency.app.dialogs.ReviewsDialog;
import com.emergency.app.interfaces.OnEmployeeListener;
import com.emergency.app.models.User;
import com.emergency.app.ui.guest.AddRequestActivity;
import com.emergency.app.ui.guest.EmployeeActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeGuestFragment extends Fragment implements OnEmployeeListener,View.OnClickListener {

    private GridLayoutManager mLayoutManager;
    private RecommendAdapter adapter;
    private DatabaseReference mData;
    private ArrayList<User> listEmployee=new ArrayList<>();
    CategoryFragmentBinding bind;

    public static HomeGuestFragment newInstance() {
        return new HomeGuestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.category_fragment, container, false);
        View view = bind.getRoot();
        initViews();
        getData();
        return view;
    }
    private void initViews()
    {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                bind.rlContainer.setBackgroundResource(R.drawable.ic_bg);
            } else{
                bind.rlContainer.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.ic_bg));
            }
            bind.lyRecommend.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions(getActivity()).y * .40);
            if(AppConfigHelper.screenDimensions(getActivity()).x>700) {
                bind.viewPagerImage.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions(getActivity()).y * .15);
            }
            else
            {
                bind.viewPagerImage.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions(getActivity()).y * .3);
            }

            bind.lyBabySitter.setOnClickListener(this);
            bind.lyChiropractic.setOnClickListener(this);
            bind.lyNursing.setOnClickListener(this);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRecommendClick(int id) {
        try {
            Intent objIntent = new Intent(getActivity(), AddRequestActivity.class);
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
            objIntent.putExtra(AppConfigHelper.EMPLOYEE_ID_INTENT,id);
            startActivity(objIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onEmployeeClick(int id) {
    }

    @Override
    public void onEmployeeReviewClick(int id) {
        try {
            ReviewsDialog reviewsDialog = ReviewsDialog.newInstance(id);
            reviewsDialog.show(getActivity().getSupportFragmentManager(),"reviewsDialog");
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.lyBabySitter:
                try {
                    Intent objIntent = new Intent(getActivity(), EmployeeActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    objIntent.putExtra(AppConfigHelper.EMPLOYEE_NAME_INTENT,getResources().getString(R.string.babysitter));
                    objIntent.putExtra(AppConfigHelper.EMPLOYEE_JOB_ID_INTENT,AppConfigHelper.BABY_SITTER_JOB_ID);
                    startActivity(objIntent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.lyNursing:
                try {
                    Intent objIntent = new Intent(getActivity(), EmployeeActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    objIntent.putExtra(AppConfigHelper.EMPLOYEE_NAME_INTENT,getResources().getString(R.string.nursing));
                    objIntent.putExtra(AppConfigHelper.EMPLOYEE_JOB_ID_INTENT,AppConfigHelper.NURSE_JOB_ID);
                    startActivity(objIntent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.lyChiropractic:
                try {
                    Intent objIntent = new Intent(getActivity(), EmployeeActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    objIntent.putExtra(AppConfigHelper.EMPLOYEE_NAME_INTENT,getResources().getString(R.string.physiotherapy));
                    objIntent.putExtra(AppConfigHelper.EMPLOYEE_JOB_ID_INTENT,AppConfigHelper.PHYSIOTHERAPY_JOB_ID);
                    startActivity(objIntent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void getData()
    {
        try {
            mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        if ((boolean)uniqueKeySnapshot.child(AppConfigHelper.RECOMMEND_FIELD).getValue()) {
                            String name = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                            int orders = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ORDER_FIELD).getValue()));
                            int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                            String rate = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.RATE_FIELD).getValue());
                            int reviews = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REVIEWS_FIELD).getValue()));
                            String type = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TYPE_FIELD).getValue());
                            String photo = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                            String job = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_FIELD).getValue());
                            String price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_FIELD).getValue());
                            User objData = new User(id, name,job, type, photo,orders,reviews,rate,price);
                            listEmployee.add(objData);
                            //fill viewpager
                            bindData();
                        }
                    }
                    if(listEmployee.size()==0)
                    {
                        hideViews();
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
    private void bindData()
    {
        try {
            if(listEmployee.size()==0)
            {
                hideViews();
            }
            else {
                bind.lyRecommend.setVisibility(View.VISIBLE);
                adapter = new RecommendAdapter(getActivity(), this, listEmployee);
                adapter.notifyDataSetChanged();
                bind.viewPagerImage.setAdapter(adapter);
                bind.mIndicator.setViewPager(bind.viewPagerImage);
                if (listEmployee.size() <= 1) {
                    bind.mIndicator.setVisibility(View.GONE);
                }
                else
                {
                    bind.mIndicator.setVisibility(View.VISIBLE);
                }
                // Set a PageTransformer (Zoom Out Animation)
                bind.viewPagerImage.setPageTransformer(false, (page, position) -> {
                    final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    page.setScaleX(normalizedposition / 2 + 0.5f);
                    page.setScaleY(normalizedposition / 2 + 0.5f);

                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void hideViews()
    {
        try {
            bind.lyRecommend.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
