package com.example.ahmed.selfiecompetition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] animalNameList;
    ArrayList<AnimalNames> arraylist = new ArrayList<AnimalNames>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);


        // Generate sample data

        animalNameList = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow", "Donkey", "Monkey"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < animalNameList.length; i++) {
            AnimalNames animalNames = new AnimalNames(animalNameList[i]);
            // Binds all strings into an array
            arraylist.add(animalNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);


    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }


    public class ListViewAdapter extends BaseAdapter {

        // Declare Variables

        Context mContext;
        LayoutInflater inflater;
        private List<AnimalNames> animalNamesList = null;
        private ArrayList<AnimalNames> arraylist;

        public ListViewAdapter(Context context, List<AnimalNames> animalNamesList) {
            mContext = context;
            this.animalNamesList = animalNamesList;
            inflater = LayoutInflater.from(mContext);
            this.arraylist = new ArrayList<AnimalNames>();
            this.arraylist.addAll(animalNamesList);
        }

        @Override
        public int getCount() {
            return animalNamesList.size();
        }

        @Override
        public AnimalNames getItem(int position) {
            return animalNamesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.listview_item, null);
                // Locate the TextViews in listview_item.xml
                holder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Set the results into TextViews
            holder.name.setText(animalNamesList.get(position).getAnimalName());
            return view;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            animalNamesList.clear();
            if (charText.length() == 0) {
                animalNamesList.addAll(arraylist);
            } else {
                for (AnimalNames wp : arraylist) {
                    if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        animalNamesList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public class ViewHolder {
            TextView name;
        }

    }

    public class AnimalNames {
        private String animalName;

        public AnimalNames(String animalName) {
            this.animalName = animalName;
        }

        public String getAnimalName() {
            return this.animalName;
        }

    }


}
