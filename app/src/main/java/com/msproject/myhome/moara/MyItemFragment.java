package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MyItemFragment extends Fragment {
    GridView gift_item_list;
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

        public void addItems(GiftItem item){items.add(item);}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GiftItemView view = new GiftItemView(getActivity().getApplicationContext());
            view.setName(items.get(position).getName());
            view.setDate(items.get(position).getDate());
            view.setFrom(items.get(position).getFrom());
            view.setImage(items.get(position).getStoreUid() + "/product/" + items.get(position).getName() + ".jpg", getContext());
            view.setBackground(position);

            return view;
        }
    }
    public MyItemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyItemFragment newInstance() {
        MyItemFragment fragment = new MyItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_item, container, false);

        gift_item_list = (GridView) view.findViewById(R.id.myItemList);
        gift_item_adapter =  new GiftItemAdapter(inflater);
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mdatabase.child("/users/" + MainActivity.uid + "/giftitem/");

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Log.d("storeUid==", s.child("storeUid").getValue().toString());
                        GiftItem item = s.getValue(GiftItem.class);
                        gift_item_adapter.addItems(item);
                        gift_item_list.setAdapter(gift_item_adapter);
                    }
                }
                gift_item_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 눌렀을 때 바코드 출력
        gift_item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String barcodeString = gift_item_adapter.items.get(position).getBarcode();
                Log.d("barcode", barcodeString);
                final BarcodeDialog barcodeDialog = new BarcodeDialog(barcodeString);
                barcodeDialog.getInstance(getContext(), inflater, R.layout.activity_barcode_dialog);
                barcodeDialog.show();
            }
        });

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


    }
}
