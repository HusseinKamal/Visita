package com.emergency.app.adapters.guest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.emergency.app.R;
import com.emergency.app.interfaces.OnEmployeeListener;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.views.CustomAppButton;
import com.emergency.app.views.CustomTextView;
import com.emergency.app.views.CustomTextViewBold;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendAdapter extends PagerAdapter {
    private Context mContext;
    private OnEmployeeListener listener;
    private ArrayList<User> listEmployee;
    LayoutInflater mLayoutInflater;
    public RecommendAdapter(Context mContext, OnEmployeeListener listener,ArrayList<User> listEmployee) {
        this.mContext = mContext;
        this.listener=listener;
        this.listEmployee=listEmployee;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return listEmployee.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_recomend_layout, container, false);
        CardView card=itemView.findViewById(R.id.card);
        CircleImageView profileImage= itemView.findViewById(R.id.profileImage);
        CustomTextView tvJob=itemView.findViewById(R.id.tvJob);
        CustomTextViewBold tvName=itemView.findViewById(R.id.tvName);
        CustomTextView tvOrders=itemView.findViewById(R.id.tvOrders);
        CustomTextView tvRate=itemView.findViewById(R.id.tvRate);
        CustomTextView tvReviews=itemView.findViewById(R.id.tvReviews);
        CustomTextView tvPrice=itemView.findViewById(R.id.tvPrice);
        CustomAppButton btnAdd=itemView.findViewById(R.id.btnRequest);
        LinearLayout lyReviews=itemView.findViewById(R.id.lyReviews);

        if(AppConfigHelper.screenDimensions((Activity) mContext).x>700) {
            card.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions((Activity) mContext).y * .15);
        }
        else
        {
            card.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions((Activity) mContext).y * .3);
        }

        User user=listEmployee.get(position);
        //photo
        if(ValidateData.isValid(user.getPhoto()))
        {
            Picasso.with(mContext).load(user.getPhoto()).placeholder(R.drawable.ic_girl).into(profileImage);
        }
        else
        {
            if(user.getType().equals(mContext.getResources().getString(R.string.female))) {
                profileImage.setImageResource(R.drawable.ic_girl);
            }
            else
            {
                profileImage.setImageResource(R.drawable.ic_man);
            }
        }
        //name
        tvName.setText(user.getName());
        //rate
        tvRate.setText(user.getRate());
        //job
        tvJob.setText(user.getJob());
        //reviews
        tvReviews.setText(String.valueOf(user.getReviews()));
        //order
        tvOrders.setText(user.getOrders()+" "+mContext.getResources().getString(R.string.order));
        //price
        tvPrice.setText(user.getPrice()+" "+mContext.getResources().getString(R.string.kwd));

        btnAdd.setOnClickListener(view -> {
            listener.onRecommendClick(user.getId());
        });
        lyReviews.setOnClickListener(view -> listener.onEmployeeReviewClick(user.getId()));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
