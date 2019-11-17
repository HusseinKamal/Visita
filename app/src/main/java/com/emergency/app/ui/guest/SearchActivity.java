package com.emergency.app.ui.guest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.emergency.app.R;
import com.emergency.app.adapters.guest.EmployeeAdapter;
import com.emergency.app.databinding.ActivitySearchBinding;
import com.emergency.app.dialogs.ReviewsDialog;
import com.emergency.app.interfaces.OnEmployeeListener;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, OnEmployeeListener {
    ActivitySearchBinding bind;
    private DatabaseReference mData;
    private ArrayList<User> listEmployee=new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private EmployeeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_search);
        bind.executePendingBindings();
        initViews();
    }
    private void initViews()
    {
        try {

            AppConfigHelper.changeBackArrow(this,bind.ivBack);
            bind.pbarLoad.setVisibility(View.GONE);
            bind.edtSearch.requestFocus();
            bind.ivBack.setOnClickListener(this);
            ViewCompat.setNestedScrollingEnabled(bind.rvEmployee, false);
            mLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false)
            {
                @Override
                public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position)
                {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext())
                    {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition)
                        {
                            int yDelta = AppConfigHelper.calculateCurrentDistanceToPosition(mLayoutManager,targetPosition);
                            return new PointF(0, yDelta);
                        }
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
            bind.edtSearch.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(ValidateData.isValid(s.toString())) {
                       //search here
                        getData();
                    }
                    else
                    {
                        listEmployee.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                public void afterTextChanged(Editable s) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivBack:
                try {
                    AppConfigHelper.finishActivity(this);
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
                        if (String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue()).contains(bind.edtSearch.getText().toString())) {
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
                            listEmployee.clear();
                            listEmployee.add(objData);
                            //fill recycler view
                            bind.rvEmployee.setLayoutManager(mLayoutManager);
                            adapter=new EmployeeAdapter(SearchActivity.this,SearchActivity.this,listEmployee);
                            bind.rvEmployee.setAdapter(adapter);
                            bind.rvEmployee.setVisibility(View.VISIBLE);
                            bind.pbarLoad.setVisibility(View.GONE);
                            bind.lyNoData.lyNoData.setVisibility(View.GONE);
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

    private void hideViews()
    {
        try {
            bind.rvEmployee.setVisibility(View.GONE);
            bind.pbarLoad.setVisibility(View.GONE);
            bind.lyNoData.lyNoData.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRecommendClick(int id) {
    }
    @Override
    public void onEmployeeClick(int id) {
        try {
            Intent objIntent = new Intent(this, AddRequestActivity.class);
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
    public void onEmployeeReviewClick(int id) {
        try {
            ReviewsDialog reviewsDialog = ReviewsDialog.newInstance(id);
            reviewsDialog.show(getSupportFragmentManager(),"reviewsDialog");
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }
    }
}
