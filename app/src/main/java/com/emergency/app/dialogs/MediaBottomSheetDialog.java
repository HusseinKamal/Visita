package com.emergency.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.emergency.app.R;
import com.emergency.app.util.appconfighelper.AppConfigHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaBottomSheetDialog extends BottomSheetDialogFragment {

    private MediaBottomSheetListener mListener;

    public static MediaBottomSheetDialog newInstance(MediaBottomSheetListener mListener) {
        MediaBottomSheetDialog objDailog = new MediaBottomSheetDialog();
        objDailog.mListener = mListener;
        return objDailog;
    }

    @BindView(R.id.lyCamera)
    LinearLayout lyCamera;

    @BindView(R.id.lyGallery)
    LinearLayout lyGallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        ButterKnife.bind(this,view);
        lyCamera.setOnClickListener(v -> {
            mListener.onBottomClicked(AppConfigHelper.CAMERA_PERMISSION_REQUEST_CODE);
            dismiss();
        });
        lyGallery.setOnClickListener(v -> {
            mListener.onBottomClicked(AppConfigHelper.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            dismiss();
        });
        return view;
    }
    public interface MediaBottomSheetListener{
        void onBottomClicked(int media);
    }

}
