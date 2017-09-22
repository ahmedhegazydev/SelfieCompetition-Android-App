package com.example.ahmed.selfiecompetition.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.ahmed.selfiecompetition.ImageUploadInfo;
import com.example.ahmed.selfiecompetition.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 22/08/17.
 */

public class FragmentUploadedImages extends Fragment /*implements ViewPager.OnPageChangeListener */ {

    View viewRoot = null;
    Context context = null;
    ViewPager viewPager = null;
    ActionBar actionBar = null;
    String[] indicators = null;
    ListView listViewAllUploadedImgs = null;
    GridView gridViewAllUploadedImgs = null;
    RelativeLayout rlMainContainer = null;
    ProgressDialog pdGettingAllImages = null;
    // Creating List of ImageUploadInfo class.
    ArrayList<ImageUploadInfo> listAllImages = new ArrayList<ImageUploadInfo>();
    DatabaseReference databaseReference = null;
    private ArrayList<Fragment> listFragments = new ArrayList<Fragment>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        context = getActivity();
        context = container.getContext();

        viewRoot = inflater.inflate(R.layout.frag_my_uploaded_selfies, container, false);
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

    private void init() {

        rlMainContainer = (RelativeLayout) viewRoot;


        listViewAllUploadedImgs = new ListView(getContext());
        listViewAllUploadedImgs.setDividerHeight(2);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        //listViewAllUploadedImgs.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listViewAllUploadedImgs.setDivider(new ColorDrawable(getActivity().getResources().getColor(R.color.colorBlue2)));
        rlMainContainer.addView(listViewAllUploadedImgs);
        gettingAllImagesIntoList();


        animLTR = AnimationUtils.loadAnimation(getContext(), R.anim.slide_to_right);
        animRTL = AnimationUtils.loadAnimation(getContext(), R.anim.slide_to_left);


    }

    private void gettingAllImagesIntoList() {
        pdGettingAllImages = createProgressDialog(getActivity(), "Loading Images ...");
        // Showing progress dialog.
        pdGettingAllImages.show();
        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("selfie-competition-app-201300");
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    listAllImages.add(imageUploadInfo);
                }

                ListAndGridAdapter adapter = new ListAndGridAdapter(getContext(), listAllImages);
                listViewAllUploadedImgs.setAdapter(adapter);

                // Hiding the progress dialog.
                pdGettingAllImages.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progress dialog.
                pdGettingAllImages.dismiss();

            }
        });
    }

    private void gettingAllImagesIntoGrid() {
        pdGettingAllImages = createProgressDialog(getActivity(), "Loading Images ...");
        // Showing progress dialog.
        pdGettingAllImages.show();
        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("selfie-competition-app-201300");
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    listAllImages.add(imageUploadInfo);
                }

                ListAndGridAdapter adapter = new ListAndGridAdapter(getContext(), listAllImages);
                gridViewAllUploadedImgs.setAdapter(adapter);

                // Hiding the progress dialog.
                pdGettingAllImages.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progress dialog.
                pdGettingAllImages.dismiss();

            }
        });
    }


    public ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_all_uploaded, menu);

    }

    Animation animRTL, animLTR = null;
    ListAndGridAdapter adapter = new ListAndGridAdapter(getContext(), listAllImages);

    boolean bList = true, bGrid = true;
    boolean bListVisible = true, bGridVisible = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showAsGridView:

                if (bList) {
                    listViewAllUploadedImgs.startAnimation(animRTL);
                    //rlMainContainer.removeView(listViewAllUploadedImgs);
                    bList = false;
                }

                if (bGridVisible) {
                    gridViewAllUploadedImgs = new GridView(getContext());
                    gridViewAllUploadedImgs.setNumColumns(3);
                    //gridView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    rlMainContainer.addView(gridViewAllUploadedImgs);
                    //gridViewAllUploadedImgs.startAnimation(animRTL);
                    listAllImages.clear();
                    gettingAllImagesIntoGrid();

                    bGridVisible = false;
                    bListVisible = true;
                    bGrid = true;
                }

                break;
            case R.id.showAsListView:
                if (bGrid) {
                    gridViewAllUploadedImgs.startAnimation(animRTL);
                    //rlMainContainer.removeView(gridViewAllUploadedImgs);
                    bGrid = false;
                }

                if (bListVisible) {

                    listViewAllUploadedImgs = new ListView(getContext());
                    listViewAllUploadedImgs.setDividerHeight(2);
                    int[] colors = {0, 0xFFFF0000, 0}; // red for the example
                    //listViewAllUploadedImgs.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
                    listViewAllUploadedImgs.setDivider(new ColorDrawable(getActivity().getResources().getColor(R.color.colorBlue2)));
                    rlMainContainer.addView(listViewAllUploadedImgs);
                    //listViewAllUploadedImgs.startAnimation(animRTL);
                    listAllImages.clear();
                    gettingAllImagesIntoList();

                    bListVisible = false;
                    bGridVisible = true;
                    bList = true;


                }
                break;
            case R.id.clearAll:
                confirmDeletion();
                break;
            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog alertDialog = null;
    AlertDialog.Builder builder = null;


    private void confirmDeletion() {
        new AlertDialog.Builder(getContext())
                .setMessage("All your own SELFIES will be deleted, Are u sure ????")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllImages();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }

    private void deleteAllImages() {

    }

    public class ListAndGridAdapter extends BaseAdapter {

        ArrayList<ImageUploadInfo> imageUploadInfos = null;
        Context context = null;

        public ListAndGridAdapter(Context context, ArrayList<ImageUploadInfo> imageUploadInfoArrayList) {
            this.context = context;
            this.imageUploadInfos = imageUploadInfoArrayList;
        }

        @Override
        public int getCount() {
            return this.imageUploadInfos.size();
        }

        @Override
        public ImageUploadInfo getItem(int position) {
            return this.imageUploadInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.list_image_item, null);
            ImageView ivDisplayed = (ImageView) view.findViewById(R.id.downloadedImg);
            ImageUploadInfo imageUploadInfo = getItem(position);

            //Loading image from Glide library.
            Glide.with(context).load(imageUploadInfo.getImageURL()).into(ivDisplayed);

            return view;
        }
    }


}
