package com.example.ahmed.selfiecompetition.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahmed.selfiecompetition.HomeActivity;
import com.example.ahmed.selfiecompetition.ImageUploadInfo;
import com.example.ahmed.selfiecompetition.MainActivity;
import com.example.ahmed.selfiecompetition.R;
import com.example.ahmed.selfiecompetition.model.SelfieItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ahmed on 22/08/17.
 */

public class FragmentAddNewSelfies extends Fragment /*implements ViewPager.OnPageChangeListener */ {

    View viewRoot = null;
    Context context = null;
    ViewPager viewPager = null;
    private ArrayList<Fragment> listFragments = new ArrayList<Fragment>();
    ActionBar actionBar = null;
    String[] indicators = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        context = getActivity();
        context = container.getContext();

        viewRoot = inflater.inflate(R.layout.frag_my_selfies, container, false);
        setHasOptionsMenu(true);
        init();

        //indicators = getActivity().getResources().getStringArray(R.array.frag_say_hi);

        //TabLayout tabLayout = (TabLayout) viewRoot.findViewById(R.id.tabs);
//       for (int i = 0; i < indicators.length; i++){
//           tabLayout.addTab(tabLayout.newTab().setText(indicators[i]));
//       }

//        listFragments.add(new FragmentFind());
//        listFragments.add(new FragmentChats());
//        listFragments.add(new FragmentVideos());
//        listFragments.add(new FragmentGroups());
//
//        viewPager = (ViewPager) viewRoot.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), listFragments, new String[]{"Find", "Chats", "video", "groups"}));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        tabLayout.setupWithViewPager(viewPager);
//        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
////        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                //change the title
//                tab.getText();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


        return viewRoot;

    }


//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_add_new_selfies, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_image:
                // for fragment (DO NOT use `getActivity()`)
                CropImage.activity()
                        .start(getContext(), this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    LinearLayout llImagesForUploading = null, llNoSelfiesFound = null;

    private void init() {

        llImagesForUploading = (LinearLayout) viewRoot.findViewById(R.id.scrollView).findViewById(R.id.linearLayout);
        llNoSelfiesFound = (LinearLayout) viewRoot.findViewById(R.id.llNoSelfiesFound);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == HomeActivity.RESULT_OK) {
                //ivUploadImage.setImageURI(result.getUri());
                Uri filePath = result.getUri();
                new AddingNewItem(getActivity(), llImagesForUploading, filePath);
                llNoSelfiesFound.setVisibility(LinearLayout.GONE);
//                try {
//                    Bitmap mImageBitmap = null;
//                    InputStream imageStream = null;
//                    imageStream = getActivity().getContentResolver().openInputStream(result.getUri());
//                    mImageBitmap = BitmapFactory.decodeStream(imageStream);
//                    new AddingNewItem(getActivity(), llImagesForUploading,);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//
//                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    String keyImagesUploaded = "", keyFavImages = "";
    String enteredPhoneNumber = null;
    String imgDownloadedUrl = "";

    class AddingNewItem implements View.OnClickListener {
        LinearLayout linearLayout = null;
        Context context = null;
        FirebaseStorage storage = null;
        StorageReference storageRef = null;
        ProgressDialog pdUploadingImageToStorage = null;
        Uri filePath = null;
        Button btnUpload = null;
        DatabaseReference databaseReference = null;
        String fileName = null;
        ImageUploadInfo imageUploadInfo = null;

        public AddingNewItem(Context context, LinearLayout linearLayout, Uri filePath) {
            this.context = context;
            this.linearLayout = linearLayout;
            this.filePath = filePath;

            addingNewItemToUpload();
        }


        private void addingNewItemToUpload() {
            View view = LayoutInflater.from(context).inflate(R.layout.image_item, null);
            ImageView ivUploadedImg = (ImageView) view.findViewById(R.id.ivToUpload);
            btnUpload = (Button) view.findViewById(R.id.btnUpload);
            //ivUploadedImg.setImageBitmap(fi);
            ivUploadedImg.setImageURI(filePath);
            btnUpload.setOnClickListener(this);
            linearLayout.addView(view);
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));

        }

        public ProgressDialog createProgressDialog(Context context, String message) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            return progressDialog;
        }

        @Override
        public void onClick(View v) {
            uploadImg();
        }


        private void uploadImg() {
            pdUploadingImageToStorage = createProgressDialog(context, "Uploading .... ");
            pdUploadingImageToStorage.show();

            //creating reference to firebase storage
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReferenceFromUrl("gs://selfie-competition-app-201300.appspot.com/");    //change the url according to your firebase app
            if (filePath != null) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                fileName = "image_" + dateFormat.format(date) + ".jpg";
                //StorageReference childRef = storageRef.child(enteredPhoneNumber).child("image_" + fileName + ".jpg");
                StorageReference childRef = storageRef.child(fileName);
                //uploading the image
                UploadTask uploadTask = childRef.putFile(filePath);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgDownloadedUrl = taskSnapshot.getDownloadUrl().toString();
                        imageUploadInfo = new ImageUploadInfo(fileName, imgDownloadedUrl);
                        addImgUrlToDbRef();//for the user
                        addImgUrlToDbRefFroAllUser();//shown for all users
                        if (pdUploadingImageToStorage.isShowing())
                            pdUploadingImageToStorage.dismiss();
                        Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show();
                        btnUpload.setEnabled(false);
                        btnUpload.setText("Uploaded :) ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (pdUploadingImageToStorage.isShowing())
                            pdUploadingImageToStorage.dismiss();
                        Toast.makeText(context, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        btnUpload.setText("Try again !!!");
                    }
                });


            }

        }

        private void addImgUrlToDbRef() {
            //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sharedPreferences = getActivity().getSharedPreferences(MainActivity.KEY_PHONE_NUMBER, MainActivity.MODE);
            editor = sharedPreferences.edit();
//                        if (sharedPreferences.contains(MainActivity.KEY_IMAGES_UPLOADED)) {
//                            keyImagesUploaded = sharedPreferences.getString(MainActivity.KEY_IMAGES_UPLOADED, "");
//                        }
//                        if (sharedPreferences.contains(MainActivity.KEY_IMAGES_FAV)) {
//                            keyFavImages = sharedPreferences.getString(MainActivity.KEY_IMAGES_FAV, "");
//                        }

            if (sharedPreferences.contains(MainActivity.KEY_PHONE_NUMBER)) {
                enteredPhoneNumber = sharedPreferences.getString(MainActivity.KEY_PHONE_NUMBER, "");
                //if (!TextUtils.isEmpty(keyFavImages)){}
                // Assign FirebaseDatabase instance with root database name.
                //databaseReference = FirebaseDatabase.getInstance().getReference("selfie-competition-app-201300");
                //databaseReference = FirebaseDatabase.getInstance().getReference();//root
                //if (!TextUtils.isEmpty(keyImagesUploaded)) {
                // Getting image upload ID.
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(enteredPhoneNumber).child("uploadedImages");
                String imageUploadId = reference.push().getKey();
                reference.child(imageUploadId).setValue(imageUploadInfo);
                //}
                Toast.makeText(context, enteredPhoneNumber, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Phone number Not found !!! ", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void addImgUrlToDbRefFroAllUser() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("allSelfiesComponents");
        String key = ref.push().getKey();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        ref.child(key).setValue(new SelfieItem(0, imgDownloadedUrl, new ArrayList<String>(), dateFormat.format(date).toString(), "iamge"));

    }


}
