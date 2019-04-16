package com.example.priskompis0;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, CartAdapter.RefreshPriceInterface {

    private LinearLayout top_bar;
    private ListView listView;
    private CheckBox all_checkbox;
    private TextView price;
    private TextView delete;
    private TextView tv_go_to_pay;



    //  List<user> goodsList;
    // private UserDao userDao;
    private List<HashMap<String, String>> listmap = new ArrayList<>( );
    private CartAdapter adapter;

    private double totalPrice = 0.00;
    private int totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView( );
    }

    private void initView() {
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listView = (ListView) findViewById(R.id.listview);
        all_checkbox = (CheckBox) findViewById(R.id.all_chekbox);
        price = (TextView) findViewById(R.id.tv_total_price);
        delete = (TextView) findViewById(R.id.tv_delete);
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);

        all_checkbox.setOnClickListener(this);
        delete.setOnClickListener(this);
        tv_go_to_pay.setOnClickListener(this);

        /* initDate( );*/
        adapter = new CartAdapter(CartActivity.this, listmap);
        listView.setAdapter(adapter);
        adapter.setRefreshPriceInterface(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId( )) {
            case R.id.all_chekbox:
                AllTheSelected( );
                break;
            case R.id.tv_delete:
                checkDelete(adapter.getPitchOnMap( ));
                break;
            case R.id.tv_go_to_pay:
                if (totalCount <= 0) {
                    Toast.makeText(this, "Choose which to pay~", Toast.LENGTH_SHORT).show( );
                } else {
                    Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show( );
                }
                break;
        }
    }

    /**
     * data
     */
    /*private void initDate() {
        //create data
        goodsList = (List<user>) new ArrayList<>( );
        //database
        userDao = MyApplication.getInstances( ).getDaoSession( ).getUserDao( );
        userDao.DeleteAll( );
        //data from
        for (int i = 0; i < 10; i++) {
            //向数据库存放数据
            User user = new User((long) i,
                    "The total amount" + (i + 1) + "product",
                    (i + 20) + "Amount",
                    "10",
                    "10");
            userDao.insert(user);
        }
        //integrate all data into the collection
        goodsList = userDao.loadAll( );
        //keep all data into HashMap
        for (int i = 0; i < goodsList.size( ); i++) {
            HashMap<String, String> map = new HashMap<>( );
            map.put("id", goodsList.get(i).getId( ) + "");
            map.put("name", goodsList.get(i).getName( ));
            map.put("type", (goodsList.get(i).getType( )));
            map.put("price", goodsList.get(i).getPrice( ) + "");
            map.put("count", goodsList.get(i).getCount( ) + "");
            listmap.add(map);
        }
    }
    */

    @Override
    public void refreshPrice(HashMap<String, Integer> pitchOnMap) {
        priceControl(pitchOnMap);
    }

    /**
     * control price, show total
     */
    private void priceControl(Map<String, Integer> pitchOnMap) {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < listmap.size( ); i++) {
            if (pitchOnMap.get(listmap.get(i).get("id")) == 1) {
                totalCount = totalCount + Integer.valueOf(listmap.get(i).get("count"));
                double goodsPrice = Integer.valueOf(listmap.get(i).get("count")) * Double.valueOf(listmap.get(i).get("price"));
                totalPrice = totalPrice + goodsPrice;
            }
        }
        price.setText(" Kr " + totalPrice);
        tv_go_to_pay.setText("Pay(" + totalCount + ")");
    }

    /**
     *delete control total amount
     *
     * @param map
     */
    private void checkDelete(HashMap<String, Integer> map) {
        List<HashMap<String, String>> waitDeleteList = new ArrayList<>( );
        Map<String, Integer> waitDeleteMap = new HashMap<>( );
        for (int i = 0; i < listmap.size( ); i++) {
            if (map.get(listmap.get(i).get("id")) == 1) {
                waitDeleteList.add(listmap.get(i));
                waitDeleteMap.put(listmap.get(i).get("id"), map.get(listmap.get(i).get("id")));
            }
        }
        listmap.removeAll(waitDeleteList);
        map.remove(waitDeleteMap);
        priceControl(map);
        adapter.notifyDataSetChanged( );
    }

    /**
     * select all or not
     */
    private void AllTheSelected() {
        HashMap<String, Integer> map = adapter.getPitchOnMap( );
        boolean isCheck = false;
        boolean isUnCheck = false;
        Iterator iter = map.entrySet( ).iterator( );
        while (iter.hasNext( )) {
            Map.Entry entry = (Map.Entry) iter.next( );

            if (Integer.valueOf(entry.getValue( ).toString( )) == 1) {
                isCheck = true;
            } else {
                isUnCheck = true;
            }
        }
        if (isCheck == true && isUnCheck == false) {//if select all, can uncollect
            for (int i = 0; i < listmap.size( ); i++) {
                map.put(listmap.get(i).get("id"), 0);
            }
            all_checkbox.setChecked(false);
        } else if (isCheck == true && isUnCheck == true) {//partds choose, choose all
            for (int i = 0; i < listmap.size( ); i++) {
                map.put(listmap.get(i).get("id"), 1);
            }
            all_checkbox.setChecked(true);
        } else if (isCheck == false && isUnCheck == true) {//none choose, choose all
            for (int i = 0; i < listmap.size( ); i++) {
                map.put(listmap.get(i).get("id"), 1);
            }
            all_checkbox.setChecked(true);
        }
        priceControl(map);
        adapter.setPitchOnMap(map);
        adapter.notifyDataSetChanged( );
    }
}