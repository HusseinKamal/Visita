package com.emergency.app.dialogs;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import com.emergency.app.R;
import com.emergency.app.databinding.DialogReportLayoutBinding;

public class ReportDialog extends DialogFragment {
    private OnReportDialog objOnClickListner;

    DialogReportLayoutBinding bind;

    public interface OnReportDialog {
        void onReport(String report);
    }
    public static ReportDialog newInstance(OnReportDialog objOnClickListener) {
        ReportDialog objDailog = new ReportDialog();
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
        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_report_layout, container, false);
        View view = bind.getRoot();
        initializeView();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }

    private void initializeView() {
        try {
            bind.ivClose.setOnClickListener(view -> dismiss());
            bind.btnReport.setOnClickListener(view -> {
                objOnClickListner.onReport(bind.edtReport.getText().toString());
                dismiss();
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

}
