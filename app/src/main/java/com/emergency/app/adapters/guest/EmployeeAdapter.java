package com.emergency.app.adapters.guest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.app.R;
import com.emergency.app.dialogs.ReviewsDialog;
import com.emergency.app.interfaces.OnEmployeeListener;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.views.CustomAppButton;
import com.emergency.app.views.CustomTextView;
import com.emergency.app.views.CustomTextViewBold;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>{

    private Context mContext;
    private OnEmployeeListener listener;
    private ArrayList<User> listEmployee;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card)
        CardView card;

        @BindView(R.id.tvName)
        CustomTextViewBold tvName;

        @BindView(R.id.tvJob)
        CustomTextView tvJob;

        @BindView(R.id.btnRequest)
        CustomAppButton btnRequest;

        @BindView(R.id.tvOrders)
        CustomTextView tvOrders;

        @BindView(R.id.tvRate)
        CustomTextView tvRate;

        @BindView(R.id.tvReviews)
        CustomTextView tvReviews;

        @BindView(R.id.tvPrice)
        CustomTextView tvPrice;

        @BindView(R.id.profileImage)
        CircleImageView profileImage;

        @BindView(R.id.lyReviews)
        LinearLayout lyReviews;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
    public EmployeeAdapter(Context mContext, OnEmployeeListener listener,ArrayList<User> listEmployee) {
        this.mContext = mContext;
        this.listener=listener;
        this.listEmployee=listEmployee;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            User user=listEmployee.get(position);

            //photo
            if(ValidateData.isValid(user.getPhoto()))
            {
                Picasso.with(mContext).load(user.getPhoto()).placeholder(R.drawable.ic_girl).into(holder.profileImage);
            }
            else
            {
                if(user.getType().equals(mContext.getResources().getString(R.string.female))) {
                    holder.profileImage.setImageResource(R.drawable.ic_girl);
                }
                else
                {
                    holder.profileImage.setImageResource(R.drawable.ic_man);
                }
            }
            //name
            holder.tvName.setText(user.getName());
            //rate
            holder.tvRate.setText(user.getRate());
            //job
            holder.tvJob.setText(user.getJob());
            //reviews
            holder.tvReviews.setText(String.valueOf(user.getReviews()));
            //order
            holder.tvOrders.setText(user.getOrders()+" "+mContext.getResources().getString(R.string.order));
            //price
            holder.tvPrice.setText(user.getPrice()+" "+mContext.getResources().getString(R.string.kwd));

            holder.btnRequest.setOnClickListener(view -> listener.onEmployeeClick(user.getId()));
            holder.lyReviews.setOnClickListener(view -> listener.onEmployeeReviewClick(user.getId()));

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return listEmployee.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
