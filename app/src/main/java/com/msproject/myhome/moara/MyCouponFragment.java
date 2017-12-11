package com.msproject.myhome.moara;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;


public class MyCouponFragment extends Fragment {
    int REQUEST_CODE_ADD = 100;

    static ListView couponView;
    static CouponAdapter adapter;
    Button addButton;

    LinearLayout noCoupon;

    int index;

    String msg_coupon_add = "쿠폰 추가하기";

    public MyCouponFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyCouponFragment newInstance() {
        MyCouponFragment fragment = new MyCouponFragment();
        return fragment;
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        couponView = (ListView) view.findViewById(R.id.couponList);
        addButton = (Button) view.findViewById(R.id.AddCoupon);
        noCoupon = (LinearLayout) view.findViewById(R.id.NoCoupon);

        noCoupon.setVisibility(View.VISIBLE);

        adapter= new CouponAdapter();

        couponView.setAdapter(adapter);

//        couponView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                index = i;
//                showCoupon();
//            }
//        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoupon();
            }
        });

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mdatabase.child("users/" + MainActivity.uid + "/stamps/");

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("snapshot==", snapshot.getValue().toString());
                    adapter.addItems(new Coupon(snapshot.child("name").getValue().toString(), Integer.parseInt(snapshot.child("num").getValue().toString()), snapshot.child("storeUid").getValue().toString()));
//                    Coupon stamp = snapshot.getValue(Coupon.class);
//                    adapter.addItems(stamp);


                    couponView.setAdapter(adapter);
                    noCoupon.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        couponView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("NUM==" + position, adapter.items.get(position).getNum() + "");
                adapter.setHashMap(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addCoupon(){
        Intent intent = new Intent(getActivity().getApplicationContext(), AddCouponActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    public void showCoupon(){
        Intent intent = new Intent(getActivity().getApplicationContext(), ShowCouponActivity.class);
        startActivity(intent);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_coupon, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class CouponAdapter extends BaseAdapter {
        ArrayList<Coupon> items = new ArrayList<Coupon>();
        int[] color = {R.drawable.round_00,R.drawable.round_01,R.drawable.round_02,R.drawable.round_03,R.drawable.round_04,R.drawable.round_05};
        HashMap<String, String> hashMap = new HashMap();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItems(Coupon item){items.add(item);}

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void delete(int i){
            this.items.remove(i);
        }

        @Override
        public View getView(int i, View contextView, ViewGroup viewGroup) {
            String temp = Integer.toString(i);
            if(hashMap.containsKey(Integer.toString(i))){
                StampGridItemView view = new StampGridItemView(getContext());
                view.setGridView(items.get(i), i);
                view.linearLayout.setBackgroundResource(color[i%6]);

                return view;
            }
            else{
                Coupon item = items.get(i);

                CouponView view = new CouponView(getActivity().getApplicationContext());

                view.setName(item.coupon_name);
                view.setImg(item.getImageSrc());

                view.linearLayout.setBackgroundResource(color[i%6]);
                return view;
            }
        }

        public void setHashMap(int i){
            if(!hashMap.containsKey(Integer.toString(i))){
                hashMap.put(Integer.toString(i), "grid");
            }
            else{
                hashMap.remove(Integer.toString(i));
            }
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
