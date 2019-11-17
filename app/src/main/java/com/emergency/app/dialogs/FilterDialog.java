package com.emergency.app.dialogs;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import com.emergency.app.R;
import com.emergency.app.adapters.CustomArrayAdapter;
import com.emergency.app.databinding.DialogFilterLayoutBinding;
import com.emergency.app.models.Filter;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.gpshelper.GPSHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FilterDialog extends BottomSheetDialogFragment implements View.OnClickListener,GPSHelper.OnLocationListenerEvent{

    private OnFilterDialog objOnClickListner;
    private ArrayList<String> listArea=new ArrayList<>();
    private ArrayList<String> listCategory=new ArrayList<>();
    private CustomArrayAdapter adapterArea,adapterCategory;
    private boolean isSelect=false;
    private Location myLocation;
    private Filter filter;
    private String areaLocation;
    private int price;
    DialogFilterLayoutBinding bind;

    @Override
    public void getLocation(Location location) {
        try {
            myLocation=location;
            areaLocation=getAreaLocation(location.getLatitude(),location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public interface OnFilterDialog {
        void onFilter(Filter filter);
    }
    public static FilterDialog newInstance(OnFilterDialog objOnClickListner) {
        FilterDialog objDailog = new FilterDialog();
        objDailog.objOnClickListner = objOnClickListner;
        return objDailog;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setCancelable(true);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseDialogTheme);
            getArea();
            new GPSHelper(getActivity(),this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_filter_layout, container, false);
        View view = bind.getRoot();
        initializeView();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }

    private void initializeView() {
        try {
            //set offset for spinner
            AppConfigHelper.setOffetsSpinner(bind.spLocation);
            AppConfigHelper.setOffetsSpinner(bind.spCategory);
            //job type spinner
            String[] jobs = getResources().getStringArray(R.array.job_type);
            for (int i = 0; i < jobs.length; i++) {
                listCategory.add(jobs[i]);
            }
            adapterCategory= new CustomArrayAdapter(R.layout.item_spinner_layout,getActivity(),listCategory);
            adapterCategory.setDropDownViewResource(R.layout.item_spinner_layout);
            bind.spCategory.setAdapter(adapterCategory);

            bind.pbCategory.setVisibility(View.GONE);
            bind.pbLocation.setVisibility(View.GONE);
            bind.spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i == 0 && isSelect) {
                            new GPSHelper(getActivity(),(GPSHelper.OnLocationListenerEvent) getActivity());
                        }
                        areaLocation=bind.spLocation.getSelectedItem().toString();
                        isSelect = true;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //listener
            bind.ivClose.setOnClickListener(this);
            bind.btnDone.setOnClickListener(this);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }
    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case R.id.btnDone:
                if(myLocation!=null) {
                    if(!ValidateData.isValid(bind.edtPrice.getText().toString()))
                    {
                        price=0;
                    }
                    else
                    {
                        price=Integer.parseInt(bind.edtPrice.getText().toString());
                    }
                    filter=new Filter(bind.edtName.getText().toString(),areaLocation,bind.spCategory.getSelectedItem().toString(),price);
                    objOnClickListner.onFilter(filter);
                }
                else
                {
                    new GPSHelper(getActivity(),this);
                }
                dismiss();
                break;
            case R.id.ivClose:
                dismiss();
                break;
        }
    }
    public String getAreaLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            /*String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/
            return obj.getSubAdminArea();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private void getArea(){
        try {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.AREA_CHILD);
            listArea.add(0,getResources().getString(R.string.current_location));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Map<String,Object> area =(Map<String,Object>) postSnapshot.getValue();
                        listArea.add(area.get("name").toString());
                        fillArea();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listArea.add(getResources().getString(R.string.current_location));
                    listArea.add("Hawally");
                    listArea.add("Faroniea");
                    fillArea();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void fillArea()
    {
        try {
            adapterArea=new CustomArrayAdapter(R.layout.item_spinner_layout,getActivity().getApplicationContext(),listArea);
            bind.spLocation.setAdapter(adapterArea);
            bind.pbLocation.setVisibility(View.GONE);
            bind.spLocation.setSelection(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

