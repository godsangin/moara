package com.msproject.myhome.moara;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SaveFragment extends Fragment {
    Button add_product;
    Button save_stamp;
    TextView sum_stamp;

    SaveAdapter adapter;

    ListView product_list;

    CustomDialog customDialog;
    CustomDialog2 customDialog2;
    int position;
  
    public SaveFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance() {
        SaveFragment fragment = new SaveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);

        add_product = (Button) view.findViewById(R.id.save_add_product);
        save_stamp = (Button) view.findViewById(R.id.save_stamp);
        sum_stamp = (TextView) view.findViewById(R.id.sumStamp);

        product_list = (ListView) view.findViewById(R.id.save_product_list);
        adapter = new SaveAdapter(inflater);
        product_list.setAdapter(adapter);

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                customDialog = new CustomDialog(getContext(), new CustomDialog.ICustomDialogEventListener() {
                    public void customDialogEvent(String name, String count, String stamp) {
                        if(!name.equals("") && !count.equals("") && !stamp.equals("")){
                            adapter.addItem(new SaveItem(name, count, stamp));
                            adapter.notifyDataSetChanged();
                            sum_stamp.setText(adapter.getSum());
                        }
                    }
                });
                customDialog.show();
            }
        });

        save_stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog2 = new CustomDialog2(getContext(), Integer.parseInt(sum_stamp.getText().toString().substring(0,1)), new CustomDialog2.ICustomDialogEventListener() {
                    public void customDialogEvent(String tel, int count) {

                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */
                        /* 여기다 만드셈 */

                    }
                });
                customDialog2.show();
            }
        });
        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                showDeleteMessage();
            }
        });
        return view;
    }

    private void showDeleteMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("상품 삭제");
        builder.setMessage("상품을 삭제하시겠습니까?");

        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.removeItem(position);
                sum_stamp.setText(adapter.getSum());
                adapter.notifyDataSetChanged();
            }
        });

        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class SaveItem{
        String name;
        String count;
        String stamp;

        public SaveItem(String name, String count, String stamp){
            this.name = name;
            this.count = count;
            this.stamp = stamp;
        }

        public String getName(){
            return this.name;
        }
        public String getCount(){
            return this.count;
        }
        public String getStamp(){
            return this.stamp;
        }
    }
    class SaveAdapter extends BaseAdapter {
        ArrayList<SaveItem> items = new ArrayList<SaveItem>();
        LayoutInflater layoutInflater;

        public SaveAdapter(LayoutInflater layoutInflater) {
            this.items = new ArrayList<>();
            this.layoutInflater = layoutInflater;
        }

        @Override
        public int getCount() {return items.size();}

        public void removeItem(int position){
            items.remove(position);
        }

        public void addItem(SaveItem item){items.add(item);}

        public String getSum(){
            if(items.size() == 0){
                return "0 개";
            }
            else{
                int sum = 0;
                for(int i = 0; i < items.size(); i++){
                    sum = sum + Integer.parseInt(items.get(i).getStamp());
                }

                return String.valueOf(sum) + " 개";
            }

        }
        @Override
        public Object getItem(int position) {return items.get(position);}

        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = layoutInflater.inflate(R.layout.save_item, null, false);

            SaveItem item = (SaveItem) getItem(position);

            TextView name = (TextView) view.findViewById(R.id.save_item_name);
            TextView count = (TextView) view.findViewById(R.id.save_item_count);
            TextView stamp = (TextView) view.findViewById(R.id.save_item_stamp);

            name.setText(item.getName());
            count.setText(item.getCount());
            stamp.setText(item.getStamp());

            return view;
        }
    }

    public static class CustomDialog extends Dialog {
        String name;
        String count;
        String stamp;

        EditText product_name;
        EditText product_count;
        EditText product_stamp;

        Button mLeftButton;
        Button mRightButton;

        public interface ICustomDialogEventListener {
            public void customDialogEvent(String name, String count, String stamp);
        }
        private ICustomDialogEventListener onCustomDialogEventListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_save_dialog);

            product_name = (EditText) findViewById(R.id.dialog_product_name) ;
            product_count = (EditText) findViewById(R.id.dialog_product_count) ;
            product_stamp = (EditText) findViewById(R.id.dialog_product_stamp) ;

            mLeftButton = (Button) findViewById(R.id.btn_left);
            mRightButton = (Button) findViewById(R.id.btn_right);

            mLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = product_name.getText().toString();
                    count = product_count.getText().toString();
                    stamp = product_stamp.getText().toString();

                    onCustomDialogEventListener.customDialogEvent(name, count, stamp);
                    dismiss();
                }
            });

            mRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancel();
                }
            });
        }

        public CustomDialog(Context context, ICustomDialogEventListener onCustomDialogEventListener) {
            super(context);
            this.onCustomDialogEventListener = onCustomDialogEventListener;
        }
    }

    public static class CustomDialog2 extends Dialog {
        TextView save_stamp;
        EditText user_tel;

        int count;

        Button mLeftButton;
        Button mRightButton;

        public interface ICustomDialogEventListener {
            public void customDialogEvent(String tel, int stamp);
        }
        private ICustomDialogEventListener onCustomDialogEventListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_save_dialog2);

            save_stamp = (TextView) findViewById(R.id.save_sum_stamp);
            user_tel = (EditText) findViewById(R.id.save_user_tel);

            mLeftButton = (Button) findViewById(R.id.btn_left2);
            mRightButton = (Button) findViewById(R.id.btn_right2);

            save_stamp.setText(String.valueOf(count) + " 개의 스탬프가 적립됩니다.");
            mLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCustomDialogEventListener.customDialogEvent(user_tel.getText().toString(), count);
                    dismiss();
                }
            });

            mRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancel();
                }
            });
        }

        public CustomDialog2(Context context, int count, ICustomDialogEventListener onCustomDialogEventListener) {
            super(context);
            this.onCustomDialogEventListener = onCustomDialogEventListener;
            this.count = count;
        }
    }
}
