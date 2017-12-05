package com.msproject.myhome.moara;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    List<Integer> event_list = new ArrayList<Integer>();
    HAdapter adapter;
    DatabaseReference mdatabase;
    AdapterViewFlipper avf;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mdatabase = FirebaseDatabase.getInstance().getReference();

        TextView more_coupon = (TextView) view.findViewById(R.id.more_coupon);
        TextView more_item = (TextView) view.findViewById(R.id.more_item);

        more_coupon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, MyCouponFragment.newInstance());
                transaction.commit();

                MainActivity.second.setChecked(true);
            }
        });
        more_item.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, MyItemFragment.newInstance());
                transaction.commit();

                MainActivity.third.setChecked(true);
            }
        });

        /* 이벤트 출력하는 부분 */
        for(int i = 0; i < 4; i++){
            event_list.add(getResources().getIdentifier("event_0"+i, "drawable", "com.msproject.myhome.moara"));
        }
        /* */

        /* 내 스탬프 출력하는 부분 */
        avf = (AdapterViewFlipper) view.findViewById(R.id.event_tab);
        avf.setAdapter(new eventAdapter(view.getContext()));
        /* */


        /* 디비에서 아이템 받아와서 메인 화면에 띄우는거 */
        HorizontalListView item_list = (HorizontalListView) view.findViewById(R.id.item_list);

        adapter = new HAdapter();

        DatabaseReference mConditionRef_item = mdatabase.child("/users/" + MainActivity.uid + "/giftitem/");

        mConditionRef_item.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GiftItem giftItem = snapshot.getValue(GiftItem.class);
                    adapter.addItems(new Item(giftItem.getName(), getResources().getIdentifier("product_0"+i, "drawable", "com.msproject.myhome.moara")));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        item_list.setAdapter(adapter);
        /* */
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        avf.startFlipping();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class eventAdapter extends BaseAdapter{
        private final Context mContext;
        LayoutInflater inflater;

        public eventAdapter(Context context){
            this.mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return event_list.size();
        }

        @Override
        public Object getItem(int i) {
            return event_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void delete(int i){ event_list.remove(i);
        }

        @Override
        public View getView(int i, View contextView, ViewGroup viewGroup) {
            if(contextView == null){
                contextView = inflater.inflate(R.layout.event, viewGroup, false);
            }
            ImageView imageView = (ImageView) contextView.findViewById(R.id.event_view);
            imageView.setImageResource(event_list.get(i));
            return contextView;
        }
    }

    public class Item{
        int image;
        String info;

        public Item(String info, int image){
            this.image = image;
            this.info = info;
        }

        public void setImage(int image){
            this.image = image;
        }
        public int getImage(){
            return this.image;
        }

        public void setInfo(String info){
            this.info = info;
        }
        public String getInfo(){
            return this.info;
        }
    }
    
    public class ItemView extends LinearLayout {
        TextView info;
        ImageView img;

        public ItemView(Context context){
            super(context);

            init(context);
        }

        public ItemView(Context context, AttributeSet attrs){
            super(context, attrs);

            init(context);
        }

        public void init(Context context){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item,this,true);

            info = (TextView) findViewById(R.id.item_info);
            img = (ImageView) findViewById(R.id.item_img);
        }

        public void setInfo(String inform){ info.setText(inform);}
        public void setImage(int image) { img.setImageResource(image);}
    }

    private class  HAdapter extends BaseAdapter {
        ArrayList<Item> items = new ArrayList<Item>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItems(Item item){items.add(item);}

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
            ItemView view = new ItemView(getActivity().getApplicationContext());

            Item item = items.get(i);

            view.setInfo(item.getInfo());
            view.setImage(item.getImage());

            return view;
        }
    };
}
