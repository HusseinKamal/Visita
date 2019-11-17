package com.emergency.app.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.emergency.app.R;
import com.emergency.app.databinding.DialogMessageLayoutBinding;
public class MessageDialog extends DialogFragment {

    DialogMessageLayoutBinding bind;
    private String message;

    public static MessageDialog newInstance(String message) {
        MessageDialog objDailog = new MessageDialog();
        objDailog.message=message;
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
        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_message_layout, container, false);
        View view = bind.getRoot();
        initializeView();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }

    private void initializeView() {
        try {
            bind.tvMessage.setText(message);
            bind.ivClose.setOnClickListener(view -> dismiss());
            bind.btnOk.setOnClickListener(view -> {
                dismiss();
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

}
