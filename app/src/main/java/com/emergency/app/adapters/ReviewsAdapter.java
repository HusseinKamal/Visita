package com.emergency.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.app.R;
import com.emergency.app.adapters.guest.EmployeeAdapter;
import com.emergency.app.interfaces.OnEmployeeListener;
import com.emergency.app.models.Review;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.views.CustomAppButton;
import com.emergency.app.views.CustomTextView;
import com.emergency.app.views.CustomTextViewBold;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Review> listReviews;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        CustomTextViewBold tvName;

        @BindView(R.id.tvTime)
        CustomTextView tvTime;

        @BindView(R.id.tvDesc)
        CustomTextView tvDesc;

        @BindView(R.id.line)
        View line;

        @BindView(R.id.profileImage)
        CircleImageView profileImage;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public ReviewsAdapter(Context mContext,ArrayList<Review> listEmployee) {
        this.mContext = mContext;
        this.listReviews =listEmployee;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reviews_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Review user= listReviews.get(position);
            //photo
            if(ValidateData.isValid(user.getPhoto()))
            {
                Picasso.with(mContext).load(user.getPhoto()).placeholder(R.drawable.ic_girl).into(holder.profileImage);
            }
            else
            {
                if(user.getPhoto().equals(mContext.getResources().getString(R.string.female))) {
                    holder.profileImage.setImageResource(R.drawable.ic_girl);
                }
                else
                {
                    holder.profileImage.setImageResource(R.drawable.ic_users);
                }
            }
            //name
            holder.tvName.setText(user.getName());
            //message
            if(ValidateData.isValid(user.getMesssage())) {
                holder.tvDesc.setText(user.getMesssage());
                holder.tvDesc.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.tvDesc.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }
            //time
            String dateFormat="dd-MM-yyyy hh:mm a";
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(user.getTime()));
            holder.tvTime.setText(formatter.format(calendar.getTime()));

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return listReviews.size();
    }
}
