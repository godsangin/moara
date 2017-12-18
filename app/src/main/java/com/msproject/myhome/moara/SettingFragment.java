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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class SettingFragment extends Fragment {
    ListView listView;
    SettingAdapter adapter;
    final int SETTING_OK = 2;
    public SettingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView=(ListView) view.findViewById(R.id.SettingList);
        adapter=new SettingAdapter();
        adapter.addItem(new SettingItem(R.drawable.ic_home_black_24dp,"개인정보수정"));
        adapter.addItem(new SettingItem(R.drawable.ic_lightbulb_outline_black_24dp,"구매내역"));
        adapter.addItem(new SettingItem(R.drawable.ic_card_giftcard_black_24dp,"알림설정"));
        adapter.addItem(new SettingItem(R.drawable.ic_settings_black_24dp,"로그아웃"));

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener mItemClickListner = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2://알림설정
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        Alarm alarm = new Alarm(false, false, false);
                        mDatabase.child("users/" + MainActivity.uid + "/alarm").setValue(alarm.toMap());
                        Toast.makeText(getContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                        final CustomDialog customDialog = new CustomDialog();
                        customDialog.getInstance(getContext(), getActivity().getLayoutInflater(), R.layout.submit_dialog);
                        customDialog.show("회원가입이 완료되었습니다.", "확인");
                        customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        });
                        Intent intent = new Intent(getActivity(), MyService.class);
                        getActivity().startService(intent);

                        break;
                    case 3:
                        FirebaseAuth.getInstance().signOut();
                        Intent mintent = new Intent(getActivity(), MainActivity.class);
                        startActivity(mintent);
                        getActivity().finish();
                    default:
                        return;
                }
            }
        };
        listView.setOnItemClickListener(mItemClickListner);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == SETTING_OK){
            Toast.makeText(getActivity(),"설정완료!",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
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
