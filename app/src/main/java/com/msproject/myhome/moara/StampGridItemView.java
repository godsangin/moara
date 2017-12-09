package com.msproject.myhome.moara;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class StampGridItemView extends LinearLayout {
    GridView gridView;
    LinearLayout linearLayout;
    ImageView imageView;
    View defaultView;
    public StampGridItemView(Context context) {
        super(context);
        init(context);
    }

    public StampGridItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_stamp_grid_item_view,this, true);
        gridView = (GridView)view.findViewById(R.id.gridView);
        imageView = (ImageView)view.findViewById(R.id.change);
        linearLayout = (LinearLayout) findViewById(R.id.CouponColor);
        StampItemAdapter adapter = new StampItemAdapter();
        Coupon item = new Coupon();
        for(int i = 0; i < 10; i++){
            adapter.addItems(item);
        }
        gridView.setAdapter(adapter);

    }

    public void setGridView(Coupon coupon, final int position){
        StampItemAdapter adapter = new StampItemAdapter();
        Coupon item = new Coupon();
        for(int i = 0; i < 10; i++){
            adapter.addItems(item);
        }
        adapter.setCoupon(coupon);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCouponFragment.adapter.setHashMap(position);
                MyCouponFragment.adapter.notifyDataSetChanged();
                MyCouponFragment.couponView.setAdapter(MyCouponFragment.adapter);
            }
        });
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }

    class StampItemAdapter extends BaseAdapter{
        ArrayList<Coupon> items = new ArrayList<Coupon>();
        Coupon coupon;

        public Coupon getCoupon() {
            return coupon;
        }

        public void setCoupon(Coupon coupon) {
            this.coupon = coupon;
        }

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
            StampGridItem view = new StampGridItem(getContext());
            if(coupon.getImageSrc() != null && coupon.getNum() > i){
                view.setImageView(coupon.getImageSrc());
                return view;
            }
            else {
                return view;
            }
        }
    }
}
