package com.emergency.app.dialogs;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.emergency.app.R;
import com.emergency.app.adapters.ReviewsAdapter;
import com.emergency.app.databinding.DialogReviewsLayoutBinding;
import com.emergency.app.models.Review;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ReviewsDialog extends BottomSheetDialogFragment {
    DialogReviewsLayoutBinding bind;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mData;
    private ReviewsAdapter adapter;
    private ArrayList<Review> listReviews=new ArrayList<>();
    private int employeeID;
    public static ReviewsDialog newInstance(int employeeID) {
        ReviewsDialog objDailog = new ReviewsDialog();
        objDailog.employeeID=employeeID;
        return objDailog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_reviews_layout, container, false);
        View view = bind.getRoot();
        initializeView();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getData();
        return view;
    }

    private void initializeView() {
        try {
            bind.ivClose.setOnClickListener(view -> dismiss());
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
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    private void getData()
    {
        try {
            mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(employeeID)).child(AppConfigHelper.COMMENT_CHILD);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                                int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                                String message =String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.MESSAGE_FIELD).getValue());
                                String name = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                                String photo = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                                String time = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TIME_FIELD).getValue());
                                Review objData = new Review(id,message,name,time,photo);
                                listReviews.add(objData);
                                //fill recycler view
                                bind.rvOrder.setLayoutManager(mLayoutManager);
                                adapter = new ReviewsAdapter(getActivity(), listReviews);
                                bind.rvOrder.setLayoutManager(mLayoutManager);
                                bind.rvOrder.setAdapter(adapter);
                                bind.progress.setVisibility(View.GONE);
                                bind.lyNoData.lyNoData.setVisibility(View.GONE);
                        }
                        if (listReviews.size() == 0) {
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
