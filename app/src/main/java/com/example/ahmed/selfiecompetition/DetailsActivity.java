package com.example.ahmed.selfiecompetition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    String imgUrl = null;
    Intent intent = null;
    ImageView ivSend = null;
    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference databaseReference = null;
    ListView listViewComments = null;
    ImageView ivSelectedSelfie = null;
    EditText etComment = null;
    ArrayList<String> comments = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();
        getAllCommentsForThisSelfie();

    }

    private void getAllCommentsForThisSelfie() {
        if (imgUrl != null) {
            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("comments")) {
                        FirebaseDatabase.getInstance().getReference().child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(imgUrl)) {
                                    FirebaseDatabase.getInstance().getReference().child("comments").child(imgUrl).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                comments.add(dataSnapshot1.getValue().toString());
                                            }

                                            ListAdapter listAdapter = new ListAdapter(getApplicationContext(), comments);
                                            listViewComments.setAdapter(listAdapter);
                                            listAdapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void init() {
        ivSelectedSelfie = (ImageView) findViewById(R.id.ivSelectedSelfie);
        listViewComments = (ListView) findViewById(R.id.lvComments);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!TextUtils.isEmpty(etComment.getText().toString())) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("comments").child(imgUrl);
                        String key = ref.push().getKey();
                        ref.child(key).setValue(etComment.getText().toString());
                        etComment.setText("");


                    }
                } catch (Exception e) {
                    showAlert(e.getMessage().toString());
                }
            }
        });
        etComment = (EditText) findViewById(R.id.etText);
        /////////////////////////////////////////////////////////////
        intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
        //Loading image from Glide library.
        Glide.with(getApplicationContext()).load(imgUrl).into(ivSelectedSelfie);
        ////////////////////////////////////////////////////////////


    }

    public void showAlert(String message) {
        new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public class ListAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<String> comments = null;

        public ListAdapter(Context context, ArrayList<String> comments) {
            this.context = context;
            this.comments = comments;
        }

        @Override
        public int getCount() {
            return this.comments.size();
        }

        @Override
        public String getItem(int position) {
            return this.comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.ivUser);
            //Loading image from Glide library.
            Glide.with(context).load(R.drawable.user).into(imageView);


            TextView tvUserComment = (TextView) view.findViewById(R.id.tvComment);
            tvUserComment.setText(this.comments.get(position));


            return view;

        }
    }


}
