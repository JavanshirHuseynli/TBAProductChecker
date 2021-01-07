package com.agencytba.tba;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.agencytba.tba.dbhelpers.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminPanelActivity extends AppCompatActivity {

    DBHelper myDB;
    private ListView obj;
    EditText et_market_name, et_company_name, et_created_at, et_type, et_bn, et_pt, et_sn, et_mn, et_status, et_createdAt;
    Button btn_filter;
    RelativeLayout filter, viewData;
    View viewEl;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        myDB = new DBHelper(this);
        filter = findViewById(R.id.filter);
        viewData = findViewById(R.id.viewData);
        viewEl = findViewById(R.id.view);

        et_market_name = findViewById(R.id.et_market_name);
        et_company_name = findViewById(R.id.et_company_name);
        et_created_at = findViewById(R.id.et_created_at);
        et_type = findViewById(R.id.et_type);
        et_bn = findViewById(R.id.et_bn);
        et_pt = findViewById(R.id.et_pt);
        et_sn = findViewById(R.id.et_sn);
        et_mn = findViewById(R.id.et_mn);
        et_status = findViewById(R.id.et_status);
        et_createdAt = findViewById(R.id.et_createdAt);

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        et_created_at.setText(timeStamp);

        btn_filter = findViewById(R.id.btn_filter);

        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipBoard.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        if (item.getText().toString().contains("#")) {
            et_market_name.setText(item.getText().toString().split("#")[0]);
            et_company_name.setText(item.getText().toString().split("#")[1]);
        }

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String where = "";

                if (!et_market_name.getText().toString().equals(""))
                    where += "market_name like '%" + et_market_name.getText().toString() + "%' and ";
                if (!et_type.getText().toString().equals(""))
                    where += "type = '" + et_type.getText().toString() + "' and ";
                if (!et_created_at.getText().toString().equals(""))
                    where += "date(created_at) = '" + et_created_at.getText().toString() + "' and ";

                where += "company_name = '" + et_company_name.getText().toString() + "'";

                ArrayList array_list = myDB.getAllData(where);
                ArrayAdapter arrayAdapter=new ArrayAdapter(AdminPanelActivity.this,android.R.layout.simple_list_item_1, array_list);

                obj = (ListView)findViewById(R.id.listView1);
                obj.setAdapter(arrayAdapter);
                obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        filter.setVisibility(View.GONE);
                        obj.setVisibility(View.GONE);
                        viewEl.setVisibility(View.GONE);
                        viewData.setVisibility(View.VISIBLE);

                        Cursor rs = myDB.getData(arg2 + 1, et_market_name.getText().toString(), et_company_name.getText().toString());

                        rs.moveToFirst();

                        String brand_name = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_BRAND_NAME));
                        String product_type = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_PRODUCT_TYPE));
                        String sku_name = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_SKU_NAME));
                        String market_name = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_MARKET_NAME));
                        String status = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_STATUS));
                        String total_size = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_TOTAL_SIZE));
                        String company_size = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_COMPANY_SIZE));
                        String percentage = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_PERCENTAGE));
                        String created_at = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CREATED_AT));

                        if (!rs.isClosed())  {
                            rs.close();
                        }

                        et_bn.setText((CharSequence)brand_name);
                        et_bn.setFocusable(false);
                        et_bn.setClickable(false);

                        et_pt.setText((CharSequence)product_type);
                        et_pt.setFocusable(false);
                        et_pt.setClickable(false);

                        et_sn.setText((CharSequence)sku_name);
                        et_sn.setFocusable(false);
                        et_sn.setClickable(false);

                        et_mn.setText((CharSequence)market_name);
                        et_mn.setFocusable(false);
                        et_mn.setClickable(false);

                        if (et_type.getText().toString().equals("SOS") || et_type.getText().toString().equals("SOD"))
                            et_status.setText((CharSequence)"TS: " + total_size + " | CS: " + company_size + " | PER: " + percentage);
                        else
                            et_status.setText((CharSequence)status);
                        et_status.setFocusable(false);
                        et_status.setClickable(false);

                        et_createdAt.setText((CharSequence)created_at);
                        et_createdAt.setFocusable(false);
                        et_createdAt.setClickable(false);

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {

        i++;
        Handler handler = new Handler();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                i = 0;
            }
        };

        if (i == 1) {
            if (viewData.getVisibility() == View.VISIBLE) {
                viewData.setVisibility(View.GONE);
                viewEl.setVisibility(View.VISIBLE);
                filter.setVisibility(View.VISIBLE);
                obj.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(r, 500);
        } else if (i == 2) {

            super.onBackPressed();
            i = 0;

        }
    }
}