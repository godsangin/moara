package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    int REQUEST_CODE_ADD = 100;
    int RESULT_CODE_ADD = 100;

    int REQUEST_CODE_MODIFY = 101;
    ListView productView;
    StoreProductAdapter adapter;
    FloatingActionButton addButton;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    int index;

    LinearLayout noProduct;

    private int SELECT_PICTURE =1;
    public ProductFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        productView = (ListView) view.findViewById(R.id.ProductStoreList);
        addButton = (FloatingActionButton) view.findViewById(R.id.addProduct);
        noProduct = (LinearLayout) view.findViewById(R.id.NoProduct);

        noProduct.setVisibility(View.VISIBLE);

        adapter= new StoreProductAdapter(inflater);

        productView.setAdapter(adapter);

        productView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
         }
        });

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mdatabase.child("stores/" + MainActivity.uid + "/products");
        Log.d("MainActivity.uid==", MainActivity.uid);
        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StoreProduct item = new StoreProduct(snapshot.child("name").getValue().toString(), snapshot.child("price").getValue().toString(), snapshot.child("until").getValue().toString(), snapshot.child("comment").getValue().toString());
                    Log.d("name==", snapshot.child("name").getValue().toString());
                    item.setImage(MainActivity.uid + "/product/" + snapshot.child("name").getValue().toString() + ".jpg");
                    adapter.addItems(item);
                    noProduct.setVisibility(View.INVISIBLE);

                    productView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoupon();
            }
        });
        return view;
    }

    public void addCoupon(){
        Intent intent = new Intent(getActivity().getApplicationContext(), ModifyItem.class);

        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD){
            if(resultCode == RESULT_CODE_ADD){
                Toast.makeText(getActivity().getApplicationContext(), " 상품 추가 완료 " , Toast.LENGTH_SHORT).show();
                getActivity().recreate();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), " 상품 추가 실패 " , Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_CODE_MODIFY){
            if(resultCode == RESULT_CODE_ADD){
                Toast.makeText(getActivity().getApplicationContext(), " 상품 수정 완료 " , Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), " 상품 수정 실패 " , Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public void onAttach(Context context) { super.onAttach(context);  }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    class StoreProduct{
        String name;
        String cost;
        String validity;
        String info;
        String imageSrc;

        public StoreProduct(String productName, String productCost, String productValidity, String productInfo){
            this.name = productName;
            this.cost = productCost;
            this.validity = productValidity;
            this.info = productInfo;
            //this.image = image;
        }
        public String getName(){return this.name;}
        public void setName(String name){ this.name = name; }

        public String getCost(){return this.cost;}
        public void setCost(String cost){this.cost = cost;}

        public String getValidity(){return this.validity;}
        public void setValidity(String validity){this.validity = validity;}

        public String getInfo(){return this.info;}
        public void setInfo(String info){this.info = info;}

        public String getImage(){return this.imageSrc;}
        public void setImage(String imageSrc){this.imageSrc = imageSrc;}
    }

    class StoreProductAdapter extends BaseAdapter {
        ArrayList<StoreProduct> items;
        LayoutInflater layoutInflater;

        public StoreProductAdapter(LayoutInflater layoutInflater) {
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

        public void addItems(StoreProduct item){items.add(item);}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.store_item, null, false);
            TextView name = (TextView) view.findViewById(R.id.store_product_name);
            TextView cost = (TextView) view.findViewById(R.id.store_product_cost);
            TextView validity = (TextView) view.findViewById(R.id.store_product_validity);
            TextView info = (TextView) view.findViewById(R.id.store_product_info);
            ImageView imageView = (ImageView) view.findViewById(R.id.store_product_image);

            TextView modify = (TextView) view.findViewById(R.id.store_product_modify);

            name.setText(items.get(position).getName());
            cost.setText(items.get(position).getCost());
            validity.setText(items.get(position).getValidity());
            info.setText(items.get(position).getInfo());
            StorageReference islandRef = storageRef.child(items.get(position).getImage());
            Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ModifyItem.class);

                    startActivityForResult(intent, REQUEST_CODE_MODIFY);
                }
            });
            return view;
        }
    }

}
