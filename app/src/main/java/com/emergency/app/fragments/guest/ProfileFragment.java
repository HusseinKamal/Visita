package com.emergency.app.fragments.guest;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.emergency.app.R;
import com.emergency.app.adapters.CustomArrayAdapter;
import com.emergency.app.databinding.FragmentProfileBinding;
import com.emergency.app.dialogs.MediaBottomSheetDialog;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.dialogshelper.CustomProgress;
import com.emergency.app.util.imagehelper.ImageHelper;
import com.emergency.app.util.permissionhepler.AndroidPermission;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener,MediaBottomSheetDialog.MediaBottomSheetListener{

    FragmentProfileBinding bind;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int CAPTURE_IMAGE_REQUEST = 2;
    private Uri filePath;
    private AndroidPermission objAndroidPermission;
    private boolean isChooseImage;
    private File mediaStorageDir,mediaFile;
    private CustomArrayAdapter adapterAddress;
    private ArrayList<String> addressList=new ArrayList<>();
    private User user;
    //FireBase
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = bind.getRoot();
        setUserData();
        getArea();
        initViews();
        return view;
    }
    private void initViews()
    {
        try {
            bind.lyProfileContainer.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_circle_lite_orange));//enable vector icon in android 4.2 and 4.4
            bind.lyProfile.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_circle_pink));//enable vector icon in android 4.2 and 4.4
            bind.ivlogo.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_circle_white));//enable vector icon in android 4.2 and 4.4
            bind.bmb.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_pencil_with_circle));//enable vector icon in android 4.2 and 4.4
            objAndroidPermission=new AndroidPermission(getActivity());

            if (SharedPrefHelper.getSharedString(getActivity(), SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE)) {
                bind.edtPassword.setGravity(Gravity.RIGHT);
                bind.edtNewPassword.setGravity(Gravity.RIGHT);
            }
            //listener
            bind.bmb.setOnClickListener(this);
            bind.btnUpdate.setOnClickListener(this);
            //radio button change action listeners
            bind.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbMale.setChecked(b);
                bind.rbFemale.setChecked(!b);

            });
            bind.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbMale.setChecked(!b);
                bind.rbFemale.setChecked(b);
            });

            AppConfigHelper.setOffetsSpinner(bind.spCountry);
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
            case R.id.bmb:
                try {
                    MediaBottomSheetDialog bottomSheetDialog = MediaBottomSheetDialog.newInstance(this);
                    bottomSheetDialog.show(getActivity().getSupportFragmentManager(),"mediaBottomSheet");
                }
                catch (Exception objException)
                {
                    objException.printStackTrace();
                }
                break;
            case R.id.btnUpdate:
                try {
                    validateInput();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBottomClicked(int media) {
        try {
            if(media== AppConfigHelper.CAMERA_PERMISSION_REQUEST_CODE)
            {
                openCamera(CAPTURE_IMAGE_REQUEST);
            }
            else if(media==AppConfigHelper.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
            {
                chooseImage(PICK_IMAGE_REQUEST);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void chooseImage(int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
        }
        catch (Exception objExcetion)
        {
            objExcetion.printStackTrace();
        }
    }
    @SuppressLint("CheckResult")
    private void openCamera(int requestCode) {
        try {
           /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, requestCode);//AppConfigHelper.CAMERA_REQUEST*/
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
                    return;
                }
                RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(uri -> {
                    //Get image by uri using one of image loading libraries. I use Glide in sample app.
                    filePath=uri;
                    File objFile= ImageHelper.getResizedFile(getActivity(),ImageHelper.getRealPathFromURI(getActivity(),filePath),filePath.getLastPathSegment());
                    /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    dataUploadedImage = baos.toByteArray();*/
                    bind.ivlogo.setImageURI(Uri.fromFile(objFile));
                });

            }
            //check android version if larger than android 4(api 18)
            else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                // Do something for lollipop and above versions
                //get photo from camera
                RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(uri -> {
                    //Get image by uri using one of image loading libraries. I use Glide in sample app.
                    filePath=uri;
                    File objFile= ImageHelper.getResizedFile(getActivity(),ImageHelper.getRealPathFromURI(getActivity(),filePath),filePath.getLastPathSegment());
                    bind.ivlogo.setImageURI(Uri.fromFile(objFile));
                });
            } else {
                if (!objAndroidPermission.checkPermissionForCamera()) {
                    objAndroidPermission.requestPermissionForCamera();
                } else {
                    if (!objAndroidPermission.checkPermissionForExternalStorage()) {
                        objAndroidPermission.requestPermissionForExternalStorage();
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                        mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Trabiza" + File.separator + "Camera"+File.separator);
                        if (!mediaStorageDir.exists()) {
                            mediaStorageDir.mkdirs();
                        }
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        mediaFile = File.createTempFile(
                                "IMG_" + timeStamp,  /* prefix */
                                ".jpeg",         /* suffix */
                                mediaStorageDir      /* directory */
                        );
                        isChooseImage = false;
                        if (!isChooseImage) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
                            String uriPath = mediaFile.getAbsolutePath();//To capture Image with all android devices {By Hussein 06/08/2017}
                            filePath=Uri.fromFile(mediaFile);
                        }
                        startActivity(takePictureIntent);
                    }
                }
            }
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.use_camera_msg),Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.unuse_camera_msg), Toast.LENGTH_LONG).show();

            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    //compress photo before uploading
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                // Explain to the user why we need to read the contacts
                            }
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 30);
                            return;
                        }
                    }
                    File objFile= ImageHelper.getResizedFile(getActivity(),ImageHelper.getRealPathFromURI(getActivity(),filePath),filePath.getLastPathSegment());
                    bind.ivlogo.setImageURI(Uri.parse(objFile.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            }
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 30);
                            return;
                        }
                    }
                    File objFile= ImageHelper.getResizedFile(getActivity(),ImageHelper.getRealPathFromURI(getActivity(),filePath),filePath.getLastPathSegment());
                    bind.ivlogo.setImageURI(Uri.fromFile(objFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception objException) {
            objException.printStackTrace();
        }
    }
    private void setUserData()
    {
        try
        {
            user=(User)SharedPrefHelper.getSharedOBJECT(getActivity(),SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            if(ValidateData.isValid(user.getPhoto())) {
                Picasso.with(getActivity()).load(user.getPhoto()).placeholder(R.drawable.ic_girl).into(bind.ivlogo);
            }
            else
            {
                bind.ivlogo.setImageResource(R.drawable.ic_girl);
            }
            bind.progressbarPhoto.setVisibility(View.GONE);
            bind.tvUserName.setText(user.getName());
            bind.edtFullName.setText(user.getName());
            bind.edtEmail.setText(user.getEmail());
            bind.edtPhone.setText(user.getMobile());
            if(user.getType().equals(getResources().getString(R.string.male)))
            {
                bind.ivlogo.setImageResource(R.drawable.ic_man);
                bind.rbMale.setChecked(true);
            }
            else
            {
                bind.rbFemale.setChecked(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getArea()
    {
        try {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.AREA_CHILD);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Map<String,Object> area =(Map<String,Object>) postSnapshot.getValue();
                        addressList.add(area.get("name").toString());
                        fillArea();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    addressList.add("Hawally");
                    addressList.add("Faroniea");
                    fillArea();
                }
            });
            bind.spCountry.setSelection(addressList.indexOf(user.getAddress()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void fillArea()
    {
        try {
            adapterAddress=new CustomArrayAdapter(R.layout.item_spinner_layout,getActivity(),addressList);
            bind.spCountry.setAdapter(adapterAddress);
            bind.barLoad.setVisibility(View.GONE);
            for(int i=0;i<addressList.size();i++)
            {
                if(user.getAddress().equals(addressList.get(i)))
                {
                    bind.spCountry.setSelection(i);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void validateInput()
    {
        try
        {
            if(!ValidateData.isValid(bind.edtFullName.getText().toString()))
            {
                bind.edtFullName.requestFocus();
                bind.edtFullName.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if(!ValidateData.isValid(bind.edtPhone.getText().toString()))
            {
                bind.edtPhone.requestFocus();
                bind.edtPhone.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if(!bind.rbMale.isChecked()&&!bind.rbFemale.isChecked())
            {
                bind.rbMale.requestFocus();
                bind.rbFemale.requestFocus();
                Toast.makeText(getActivity(),getResources().getString(R.string.choose_valid_type),Toast.LENGTH_SHORT).show();
            }
            else
            {
                //update
                update();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void update()
    {
        try
        {
            CustomProgress.showProgress(getActivity().getApplicationContext(),getResources().getString(R.string.please_wait),true);
            if(user==null)
            {
                user=(User) SharedPrefHelper.getSharedOBJECT(getActivity(),SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            }
            DatabaseReference mdata=FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.GUEST_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(user.getId()));
            mdata.child(AppConfigHelper.NAME_FIELD).setValue(bind.edtFullName.getText().toString());
            mdata.child(AppConfigHelper.MOBILE_FIELD).setValue(bind.edtPhone.getText().toString());
            mdata.child(AppConfigHelper.ADDRESS_FIELD).setValue(bind.spCountry.getSelectedItem().toString());

            if(bind.rbMale.isChecked())
            {
                mdata.child(AppConfigHelper.TYPE_FIELD).setValue(getResources().getString(R.string.male));
                user.setType(getResources().getString(R.string.male));
            }
            else
            {
                mdata.child(AppConfigHelper.TYPE_FIELD).setValue(getResources().getString(R.string.female));
                user.setType(getResources().getString(R.string.female));
            }
            if(ValidateData.isValid(bind.edtPassword.getText().toString())&&ValidateData.isValid(bind.edtNewPassword.getText().toString()))
            {
                if(bind.edtPassword.getText().toString().length()>=AppConfigHelper.MIN_NUMBER_OF_PSSWORD&&
                        bind.edtNewPassword.getText().toString().length()>=AppConfigHelper.MIN_NUMBER_OF_PSSWORD
                &&bind.edtPassword.getText().toString().equals(user.getPassword())) {
                    mdata.child(AppConfigHelper.PASSWORD_FIELD).setValue(bind.edtNewPassword.getText().toString());
                }
                else
                {
                    Toast.makeText(getActivity(),getResources().getString(R.string.old_password_error),Toast.LENGTH_SHORT).show();
                }
                user.setPassword(bind.edtNewPassword.getText().toString());
            }
            //upload photo if found
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            if(filePath!=null)
            {
                StorageReference ref = storageReference.child(AppConfigHelper.GUEST_CHILD+"/"+user.getId());
                ref.putFile(filePath).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mdata.child(AppConfigHelper.PHOTO_FIELD).setValue(downloadUri.toString());
                    user.setPhoto(downloadUri.toString());
                }
            });
            }
            bind.tvUserName.setText(bind.edtFullName.getText().toString());//update username
            //save data in shared preference
            user.setName(bind.edtFullName.getText().toString());
            user.setMobile(bind.edtPhone.getText().toString());
            user.setAddress(bind.spCountry.getSelectedItem().toString());
            SharedPrefHelper.setSharedOBJECT(getActivity(),SharedPrefHelper.SHARED_PREFERENCE_USER_DATA,user);
            Toast.makeText(getActivity(),getResources().getString(R.string.update_done),Toast.LENGTH_SHORT).show();
            CustomProgress.hideProgress();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
