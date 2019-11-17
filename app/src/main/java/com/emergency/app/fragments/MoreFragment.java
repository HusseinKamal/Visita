package com.emergency.app.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emergency.app.R;
import com.emergency.app.databinding.FragmentMoreBinding;
import com.emergency.app.models.User;
import com.emergency.app.ui.LoginActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;
import com.emergency.app.viewmodel.HomeViewModel;

public class MoreFragment extends Fragment implements View.OnClickListener {

    FragmentMoreBinding bind;
    HomeViewModel model;
    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        View view = bind.getRoot();
        model = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        initViews();
        return view;
    }
    private void initViews()
    {
        try {

            PackageInfo pInfo = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            bind.tvVersion.setText(getResources().getString(R.string.version)+" "+version);

            if(SharedPrefHelper.getSharedString(getActivity(),SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE))
            {
                bind.ivArrowAbout.setImageResource(R.drawable.ic_left_arrow_gray);
                bind.ivArrowContact.setImageResource(R.drawable.ic_left_arrow_gray);
                bind.ivArrowLogout.setImageResource(R.drawable.ic_left_arrow_gray);
                bind.ivArrowEmail.setImageResource(R.drawable.ic_left_arrow_gray);
                bind.ivArrowProfile.setImageResource(R.drawable.ic_left_arrow_gray);
            }
            bind.lyAboutUs.setOnClickListener(this);
            bind.lyContact.setOnClickListener(this);
            bind.lyEmailUs.setOnClickListener(this);
            bind.lyLogout.setOnClickListener(this);
            bind.lyPersonalAccount.setOnClickListener(this);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.lyAboutUs:
                try {
                    model.setData(AppConfigHelper.ABOUT_ID);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.lyContact:
                try {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getResources().getString(R.string.call_msg))
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                // Continue with delete operation
                                Intent intent =new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + AppConfigHelper.PHONE_NMBER));
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(
                                                getActivity(),
                                                new String[]{Manifest.permission.CALL_PHONE},
                                                123
                                        );
                                    } else {
                                        startActivity(intent);
                                    }
                                } else {
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.lyPersonalAccount:
                try {
                    model.setData(AppConfigHelper.PROFILE_ID);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.lyEmailUs:
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.email_address_value), null));
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_email)));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.lyLogout:
                try {
                    SharedPrefHelper.setSharedOBJECT(getActivity(),SharedPrefHelper.SHARED_PREFERENCE_USER_DATA,new User());
                    AppConfigHelper.gotoActivity(getActivity(), LoginActivity.class,true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }
}
