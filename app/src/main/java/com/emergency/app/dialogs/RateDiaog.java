package com.emergency.app.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.emergency.app.R;
import com.emergency.app.databinding.DialogRateLayoutBinding;
public class RateDiaog extends DialogFragment {
    private OnRateDiaog objOnClickListner;

    DialogRateLayoutBinding bind;

    public interface OnRateDiaog {
        void onReport(String rate,String message);
    }
    public static RateDiaog newInstance(OnRateDiaog objOnClickListener) {
        RateDiaog objDailog = new RateDiaog();
        objDailog.objOnClickListner = objOnClickListener;
        return objDailog;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setCancelable(true);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseDialogTheme);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_rate_layout, container, false);
        View view = bind.getRoot();
        initializeView();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }

    private void initializeView() {
        try {
            bind.ivClose.setOnClickListener(view -> dismiss());
            bind.btnSubmit.setOnClickListener(view -> {
                if(bind.rate.getRating()>0) {
                    objOnClickListner.onReport(String.valueOf(bind.rate.getRating()),bind.edtReport.getText().toString());
                    dismiss();
                }
                else
                {
                    Toast.makeText(getActivity(),getResources().getString(R.string.enter_empty_data),Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

}
