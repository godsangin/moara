package com.msproject.myhome.moara;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    int REQUEST_CODE_ADD = 100;
    int RESULT_CODE_ADD = 100;

    int REQUEST_CODE_MODIFY = 101;

    ListView productView;
    StoreProductAdapter adapter;
    Button addButton;

    int index;

    LinearLayout noProduct;

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
        addButton = (Button) view.findViewById(R.id.addProduct);
        noProduct = (LinearLayout) view.findViewById(R.id.NoProduct);

        noProduct.setVisibility(View.VISIBLE);

        adapter= new StoreProductAdapter(inflater);

        productView.setAdapter(adapter);

        productView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
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
                adapter.addItems(new StoreProduct("상품 이름", "상품 코스트","상품 유효기간","상품 정보"));
                noProduct.setVisibility(View.INVISIBLE);

                productView.setAdapter(adapter);
                Toast.makeText(getActivity().getApplicationContext(), " 상품 추가 완료 " , Toast.LENGTH_SHORT).show();
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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class StoreProduct{
        String name;
        String cost;
        String validity;
        String info;
        int image;

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

        public int getImage(){return this.image;}
        public void setImage(int image){this.image = image;}
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

            TextView modify = (TextView) view.findViewById(R.id.store_product_modify);

            name.setText(items.get(position).getName());
            cost.setText(items.get(position).getCost());
            validity.setText(items.get(position).getValidity());
            info.setText(items.get(position).getInfo());

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
