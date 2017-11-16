package com.msproject.myhome.moara;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MyCouponFragment extends Fragment {
    int REQUEST_CODE_ADD = 100;

    ListView couponView;
    CouponAdapter adapter;

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

        adapter= new CouponAdapter();

        adapter.addItems(new Coupon(msg_coupon_add, (R.drawable.coupon_add)));
        couponView.setAdapter(adapter);

        couponView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapter.getCount()-1 == i){
                    addCoupon();
                }
                else{
                    index = i;
                    showCoupon();
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == REQUEST_CODE_ADD){
            adapter.delete(adapter.getCount()-1);
            adapter.addItems(new Coupon(String.valueOf(adapter.getCount()), (R.drawable.coupon_add)));
            adapter.addItems(new Coupon(msg_coupon_add, (R.drawable.coupon_add)));

            couponView.setAdapter(adapter);
            Toast.makeText(getActivity().getApplicationContext(), " 쿠폰 추가 완료 " , Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), " 쿠폰 추가 실패 " , Toast.LENGTH_SHORT).show();
        }
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
            CouponView view = new CouponView(getActivity().getApplicationContext());

            Coupon item = items.get(i);

            view.setName(item.coupon_name);
            view.setImg(item.getCoupon_img());

            return view;
        }
    }
}
