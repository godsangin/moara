package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MyItemFragment extends Fragment {
    ListView gift_item_list;
    GiftItemAdapter gift_item_adapter;

    class GiftItemAdapter extends BaseAdapter{
        ArrayList<GiftItem> items;
        LayoutInflater layoutInflater;


        public GiftItemAdapter(LayoutInflater layoutInflater) {
            this.items = new ArrayList<>();
            this.layoutInflater = layoutInflater;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.activity_gift_item, null, false);
            TextView textView =(TextView)view.findViewById(R.id.productName);
            TextView textView2 =(TextView)view.findViewById(R.id.productDate);
            TextView textView3 =(TextView)view.findViewById(R.id.from);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            textView.setText(items.get(position).getName());
            textView2.setText(items.get(position).getDate());
            textView3.setText(items.get(position).getFrom());


            return view;
        }
    }
    public MyItemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyItemFragment newInstance() {
        MyItemFragment fragment = new MyItemFragment();
        fragment.addAllItems();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_item, container, false);

        gift_item_list = (ListView) view.findViewById(R.id.myItemList);
        gift_item_adapter =  new GiftItemAdapter(inflater);

        gift_item_list.setAdapter(gift_item_adapter);

        gift_item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        gift_item_adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addAllItems(){

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mdatabase.child("/users/" + MainActivity.uid + "/giftitem/");

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GiftItem item = snapshot.getValue(GiftItem.class);
                    gift_item_adapter.items.add(item);
                    gift_item_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
