package com.example.ahmed.selfiecompetition.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.ahmed.selfiecompetition.DetailsActivity;
import com.example.ahmed.selfiecompetition.ImageUploadInfo;
import com.example.ahmed.selfiecompetition.R;
import com.example.ahmed.selfiecompetition.SearchActivity;
import com.example.ahmed.selfiecompetition.model.SelfieItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ahmed on 22/08/17.
 */

public class FragmentAllSelfies extends Fragment /*implements ViewPager.OnPageChangeListener */ {

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


        viewRoot = inflater.inflate(R.layout.frag_all_selfies, container, false);
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


    ListView listViewAllShowSelfies = null;
    ArrayList<SelfieItem> selfieItems = new ArrayList<SelfieItem>();
    SelfieItem selectedSelfieItem = null;

    private void init() {

        listViewAllShowSelfies = (ListView) viewRoot.findViewById(R.id.lvAllSelfies);
        checkIfRefExistsForAvoidingError();
        listViewAllShowSelfies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSelfieItem = selfieItems.get(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("imgUrl", selectedSelfieItem.getSelfieUrl());
                startActivity(intent);

            }
        });


    }

    private void checkIfRefExistsForAvoidingError() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //check if the ref exists
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("allSelfiesComponents")) {
                    selfieItems.clear();
                    getAllSelfiesForListView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getAllSelfiesForListView() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("allSelfiesComponents");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SelfieItem selfieItem = postSnapshot.getValue(SelfieItem.class);
                    selfieItems.add(selfieItem);
                }

                ViewAdapter viewAdapter = new ViewAdapter(getContext(), selfieItems);
                listViewAllShowSelfies.setAdapter(viewAdapter);
                viewAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });


    }


    public class ViewAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<SelfieItem> selfieItems = null;

        // Gets the context so it can be used later
        public ViewAdapter(Context context, ArrayList<SelfieItem> selfieItems) {
            mContext = context;
            this.selfieItems = selfieItems;

        }

        @Override
        public int getCount() {
            return this.selfieItems.size();
        }

        @Override
        public SelfieItem getItem(int position) {
            return this.selfieItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SelfieItem selfieItem = getItem(position);

            View view = LayoutInflater.from(context).inflate(R.layout.selfie_list_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivSelfie);
            //Loading image from Glide library.
            Glide.with(context).load(selfieItem.getSelfieUrl()).into(imageView);


            TextView tvUploadedDate = (TextView) view.findViewById(R.id.tvUploadedDate);
            tvUploadedDate.setText(selfieItem.getImgUploadedDate());


            return view;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_all_selfies, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.searchForSelfie:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
