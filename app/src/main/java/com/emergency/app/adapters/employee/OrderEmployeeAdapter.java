package com.emergency.app.adapters.employee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.emergency.app.R;
import com.emergency.app.models.Request;
import com.emergency.app.ui.employee.RequestDetailsEmployeeActivity;
import com.emergency.app.ui.guest.RequestDetailsGuestActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.views.CustomTextView;
import com.emergency.app.views.CustomTextViewBold;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderEmployeeAdapter extends RecyclerView.Adapter<OrderEmployeeAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Request> listRequest;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card)
        CardView card;

        @BindView(R.id.tvDateTime)
        CustomTextView tvDateTime;

        @BindView(R.id.tvStatus)
        CustomTextView tvStatus;

        @BindView(R.id.tvDesc)
        CustomTextView tvDesc;

        @BindView(R.id.tvUser)
        CustomTextViewBold tvUser;

        @BindView(R.id.lyContainer)
        LinearLayout lyContainer;

        @BindView(R.id.lyStatus)
        LinearLayout lyStatus;

        @BindView(R.id.profileImage)
        CircleImageView profileImage;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public OrderEmployeeAdapter(Context mContext, ArrayList<Request> listRequest) {
        this.mContext = mContext;
        this.listRequest=listRequest;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_employee_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            Request item=listRequest.get(position);
            //photo
            if(ValidateData.isValid(item.getGuestPhoto()))
            {
                Picasso.with(mContext).load(item.getGuestPhoto()).placeholder(R.drawable.ic_girl).into(holder.profileImage);
            }
            else
            {
                if(item.getGuestType().equals(mContext.getResources().getString(R.string.female))) {
                    holder.profileImage.setImageResource(R.drawable.ic_girl);
                }
                else
                {
                    holder.profileImage.setImageResource(R.drawable.ic_man);
                }
            }
            //status
            holder.tvStatus.setText(AppConfigHelper.getStatus(mContext,item.getStatus()));
            holder.lyStatus.setBackground(AppConfigHelper.getBackground(mContext,item.getStatus()));
            //name
            holder.tvUser.setText(item.getGuestName());
            //time
            holder.tvDateTime.setText(item.getDateTime());
            //description
            holder.tvDesc.setText(item.getRequestDescription());

            holder.card.setOnClickListener(view -> {
                try {
                    Intent objIntent = new Intent(mContext, RequestDetailsEmployeeActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    objIntent.putExtra(AppConfigHelper.REQUEST_ID_INTENT,item.getRequestId());
                    mContext.startActivity(objIntent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return listRequest.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
