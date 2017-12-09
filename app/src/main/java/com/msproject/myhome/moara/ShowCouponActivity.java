package com.msproject.myhome.moara;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowCouponActivity extends AppCompatActivity {
    ListView listView;
    ProductAdapter adapter;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coupon);

        listView = (ListView) findViewById(R.id.productList);

        adapter = new ProductAdapter();

        adapter.addItems(new Product("커피", 5, R.drawable.product_00, null));
        adapter.addItems(new Product("빵", 3, R.drawable.product_01, null));
        adapter.addItems(new Product("케잌", 5, R.drawable.product_02, null));
        adapter.addItems(new Product("토스트", 5, R.drawable.product_03, null));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                buyProduct();
            }
        });
    }

    public void buyProduct(){
        Product selectedProduct = (Product) adapter.getItem(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(selectedProduct.getName());
        builder.setMessage("구매하시겠습니까? \n사용 쿠폰 : " + selectedProduct.getPrice() +" 개");
        builder.setIcon(selectedProduct.getImage());

        builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogI, int whichButton){
                
                Toast.makeText(getApplicationContext(), "구매 완료", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogI, int whichButton){
                Toast.makeText(getApplicationContext(), "구매 실패", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class ProductAdapter extends BaseAdapter {
        ArrayList<Product> items = new ArrayList<Product>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItems(Product item){items.add(item);}

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
            ProductView view = new ProductView(getApplicationContext());

            Product item = items.get(i);
            view.setName(item.getName());
            view.setPrice(item.getPrice());
            view.setImage(item.getImage());

            return view;
        }
    }
}
