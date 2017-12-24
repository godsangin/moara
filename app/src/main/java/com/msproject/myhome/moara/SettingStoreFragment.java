package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SettingStoreFragment extends Fragment {
    ListView listView;
    SettingAdapter adapter;

    public SettingStoreFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingStoreFragment newInstance() {
        SettingStoreFragment fragment = new SettingStoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting_store, container, false);

        listView=(ListView) view.findViewById(R.id.SettingStoreList);
        adapter= new SettingAdapter();
        adapter.addItem(new SettingItem(R.drawable.ic_home_black_24dp,"매장 등록"));
        adapter.addItem(new SettingItem(R.drawable.ic_lightbulb_outline_black_24dp,"구매내역"));
        adapter.addItem(new SettingItem(R.drawable.ic_card_giftcard_black_24dp,"알림설정"));
        adapter.addItem(new SettingItem(R.drawable.ic_settings_black_24dp,"로그아웃"));

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener mItemClickListner = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position){
                    case 0:
                        intent = new Intent(getActivity(), SubmitStoreActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(getContext(),"업데이트 중입니다.\n다음버전에서 찾아뵙겠습니다.",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(),"업데이트 중입니다.\n다음버전에서 찾아뵙겠습니다.",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    default:
                        return;
                }
            }
        };
        listView.setOnItemClickListener(mItemClickListner);
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
    class SettingAdapter extends BaseAdapter {
        //        LayoutInflater layoutInflater;
        ArrayList<SettingItem> items = new ArrayList<SettingItem>();

        @Override
        public int getCount() {return items.size();}
        public void removeItem(int position){
            items.remove(position);
        }
        public void addItem(SettingItem item){items.add(item);}
        @Override
        public Object getItem(int position) {return items.get(position);}
        @Override
        public long getItemId(int position) {return position;}
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            SettingItemView view = new SettingItemView(getActivity().getApplicationContext());
            SettingItem item = items.get(position);
            view.setName(item.getSettingName());
            view.setImage(item.getSettingIcon());
//            View view= layoutInflater.inflate(R.layout.activity_item_setting,null,false);
//            TextView textView= (TextView) view.findViewById(R.id.SettingText);
//            ImageView imageView = (ImageView) view.findViewById(R.id.SettingImage);
//            textView.setText(items.get(position).getSettingName());
//            imageView.setImageResource(items.get(position).getSettingIcon());
            return view;
        }
    }

}
