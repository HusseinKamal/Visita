package com.emergency.app.util.dialogshelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.emergency.app.R;
import com.emergency.app.views.CustomTextView;

public class CustomProgress {
    public static CustomProgress  customProgress ;
    public static Dialog mDialog;
    private static ProgressBar mProgressBar;
    public static CustomProgress getInstance()
    {
        if (customProgress == null) {
            customProgress = new CustomProgress();
        }
        return customProgress;
    }
    public static void showProgress(Context context,String message, Boolean cancelable) {
        try {
            mDialog = new Dialog(context);
            // no tile for the dialog
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.progress_dialog_layout);
            mProgressBar = mDialog.findViewById(R.id.progress);

            CustomTextView progressText = mDialog.findViewById(R.id.tvDialogTitle);
            progressText.setText(message);
            //progressText.visibility = View.GONE;
            mProgressBar.setVisibility(View.VISIBLE);
            // you can change or add this line according to your need
            mDialog.setCancelable(cancelable);
            mDialog.setCanceledOnTouchOutside(cancelable);
            mDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void hideProgress() {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
