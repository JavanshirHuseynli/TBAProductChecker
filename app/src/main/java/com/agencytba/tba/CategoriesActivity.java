package com.agencytba.tba;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agencytba.tba.adapters.CategoriesAdapter;
import com.agencytba.tba.dbhelpers.DBHelper;
import com.agencytba.tba.modals.Categories;
import com.agencytba.tba.server.Constants;
import com.agencytba.tba.server.RetrofitResponseListener;
import com.agencytba.tba.server.apis.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SharedPreferences mPref,Pref;
    private List<Categories> categoriesList;
    private RecyclerView.Adapter adapter;
    private android.app.AlertDialog dialog;
    private android.app.AlertDialog dialogLoad;
    int doneCategories = 0;
    DBHelper myDB;
    FloatingActionButton fab;
    boolean offlineModeFinished = false;
    boolean allDataUploaded = false;
    int countOfData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        myDB = new DBHelper(this);
        mPref = getSharedPreferences("Constants", 0);
        Pref = getSharedPreferences("Radios", 0);
        categoriesList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        loadUrlData();
        Pref.edit().putString("doneStrings"+mPref.getString(Constants.MARKET_NAME, ""),"").apply();

        fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fab.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.rippleColor)));
        if (Pref.getBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/", false)) fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                if (haveNetworkConnection()) {
                    try {
                        uploadOfflineData();
                    } catch (InterruptedException e) {
                        fab.setEnabled(true);
                        e.printStackTrace();
                    }
                    offlineModeFinished = true;
                } else
                    Toast.makeText(CategoriesActivity.this, "İnternet bağlantısı yoxdur.", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void loadUrlData() {
        String cates = mPref.getString("categories", "");
        SharedPreferences marketAnalyze = getSharedPreferences("MarketAnalyze",0);
        try {
            JSONObject jsonObject = new JSONObject(cates);
            Log.d("whut", String.valueOf(jsonObject));
            if (jsonObject.has("category")){
                JSONArray array = jsonObject.getJSONArray("category");
                Log.d(Constants.TAG, String.valueOf(array));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jo = array.getJSONObject(i);
                    boolean done = Pref.getBoolean(mPref.getString(Constants.MARKET_NAME, "")+"/"+jo.getString("category_name"),false);
                    if (mPref.getString(Constants.CATEGORIES_TO_CHECK,"").equals("ALL")) {
                        if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSA",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPD",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"Planogram",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOS",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSAIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPDIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"PlanogramIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOSIC",0)>0) {
                            Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                            categoriesList.add(ctgrs);
                            if (done) doneCategories++;
                        } else if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSA",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPD",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"Planogram",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOS",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSAIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPDIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"PlanogramIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOSIC",0)>0) {
                            if (!jo.getString("category_name").equals("SOD")) {
                                Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                categoriesList.add(ctgrs);
                                if (done) doneCategories++;
                            }
                        } else if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSA",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPD",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"Planogram",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOS",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSAIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPDIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"PlanogramIC",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOSIC",0)==0) {
                            if (!jo.getString("category_name").equals("SOD")) {
                                Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                categoriesList.add(ctgrs);
                                if (done) doneCategories++;
                            }
                        } else if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSA",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPD",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"Planogram",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOS",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"OSAIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"NPDIC",0)>0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"PlanogramIC",0)==0&&marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOSIC",0)==0) {
                            if (jo.getString("category_name").equals("IC")) {
                                Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                categoriesList.add(ctgrs);
                                if (done) doneCategories++;
                            }
                        } else if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)==0) {
                            if (!jo.getString("category_name").equals("SOD")) {
                                Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                categoriesList.add(ctgrs);
                                if (done) doneCategories++;
                            }
                        } else {
                            Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                            categoriesList.add(ctgrs);
                            if (done) doneCategories++;
                        }
                    } else if (mPref.getString(Constants.CATEGORIES_TO_CHECK,"").equals("IC")) {
                        if (jo.getString("category_name").equals("IC")) {
                            Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                            categoriesList.add(ctgrs);
                            if (done) doneCategories++;
                        }
                    } else {
                        if (!jo.getString("category_name").equals("IC")) {
                            if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)>0) {
                                Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                categoriesList.add(ctgrs);
                                if (done) doneCategories++;
                            } else if (marketAnalyze.getInt(mPref.getString(Constants.MARKET_NAME, "")+"SOD",0)==0){
                                if (!jo.getString("category_name").equals("SOD")) {
                                    if (!mPref.getString(Constants.MARKET_TYPE,"").equals("Pharmacy")) {
                                        Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                        categoriesList.add(ctgrs);
                                        if (done) doneCategories++;
                                    } else {
                                        if (!jo.getString("category_name").equals("HHC - wc & universal")&&!jo.getString("category_name").equals("HHC - kitchen & bath")&&!jo.getString("category_name").equals("IC")&&!jo.getString("category_name").equals("Food")) {
                                            Categories ctgrs = new Categories(jo.getString("category_name"), jo.getString("subcategories"), done, false);
                                            categoriesList.add(ctgrs);
                                            if (done) doneCategories++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                adapterSet();
            }else {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(CategoriesActivity.this, "No category uploaded yet!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void adapterSet(){
        if (doneCategories == categoriesList.size()) Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "")+"/",true).apply();

        adapter = new CategoriesAdapter(categoriesList, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        showDialog();
        Toast.makeText(CategoriesActivity.this, "Yüklənən: " + getSharedPreferences("Radios",0).getInt("uploadedData/"+mPref.getString(Constants.MARKET_NAME, ""), 0) + " Bütün: " + countOfData, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offline, menu);

        MenuItem uploadItem = menu.findItem(R.id.upload);
        uploadItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.upload) {
            if (haveNetworkConnection()) {
                try {
                    uploadOfflineData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                offlineModeFinished = true;
                //Toast.makeText(CategoriesActivity.this, "Məlumatlar Tam Yükləndi.", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(CategoriesActivity.this, "İnternet bağlantısı yoxdur.", Toast.LENGTH_LONG).show();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private boolean haveNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_status", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }
        Log.i("update_status","Network is available : FALSE ");
        return false;
    }

    private void showDialog(){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_market_end, null);
        final Button yes = (Button)view.findViewById(R.id.yes);
        final Button no = (Button)view.findViewById(R.id.no);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        final View v = (View)view.findViewById(R.id.view);
        final View v2 = (View)view.findViewById(R.id.view2);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection()) {
                    if (Pref.getBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/", false)) {
                        if (offlineModeFinished) {
                            if (countOfData == getSharedPreferences("Radios",0).getInt("uploadedData/"+mPref.getString(Constants.MARKET_NAME, ""), 0)) {
                                insertMarketCount();
                                progressBar.setVisibility(View.VISIBLE);
                                v.setVisibility(View.VISIBLE);
                                v2.setVisibility(View.VISIBLE);
                                yes.setEnabled(false);
                                no.setEnabled(false);
                                builder.setCancelable(false);
                            } else Toast.makeText(CategoriesActivity.this, "Zəhmət olmasa bütün məlumatların yüklənib bitməsini gözləyin.", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(CategoriesActivity.this, "Zəhmət olmasa məlumatları serverə yükləyəsiniz.\n Əgər yükləmə prosesində hər hansısa problem yaşanmışdırsa administrasiya ilə əlaqə yaradasınız.", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(CategoriesActivity.this, "Bütün sualları yoxlayıb bitirmədən marketi bitirə bilmərsiniz.", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(CategoriesActivity.this, "Zəhmət olmasa Uçuş Modunu keçirdib yuxarıdakı düyməni klikləyərək məlumatları serverə yükləyəsiniz.", Toast.LENGTH_SHORT).show();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoriesActivity.this, MainActivity.class));
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void insertMarketCount() {
        Call<ResponseBody>call = ApiClient.getInstance().getApi().InsertMarketCount(Constants.INSERT_MARKET_COUNT, mPref.getString(Constants.UNIQUE_ID, ""), mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String resp = "";
                try {
                    resp = response.body().string().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (resp.split(" # ")[0].equalsIgnoreCase(Constants.SUCCESS)) {
                    Pref.edit().putBoolean(mPref.getString(Constants.MARKET_NAME, "") + "/", true).apply();
                    Intent intent = new Intent(CategoriesActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CategoriesActivity.this, resp.split(" # ")[1], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(Constants.TAG, "failed");
                Toast.makeText(CategoriesActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void uploadOfflineData() throws InterruptedException {
        fab.setEnabled(false);
        String marketName = mPref.getString(Constants.MARKET_NAME, "");
        String companyName = mPref.getString(Constants.COMPANY, "");
        countOfData = myDB.numberOfRows(marketName, companyName);
        Cursor cursor = myDB.getStoredData(marketName, companyName);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.progressbar_layout, null);
        final ProgressBar pgsBar = (ProgressBar) view.findViewById(R.id.pBar);
        final TextView txtView = (TextView) view.findViewById(R.id.tView);
        final TextView txtHeader = (TextView) view.findViewById(R.id.header);
        pgsBar.setMax(countOfData);
        txtHeader.setText("Məlumatlar Serverə Yüklənir...");
        builder.setView(view);
        dialogLoad = builder.create();
        dialogLoad.show();
        if (cursor.moveToFirst()) {
            do {
                insertData(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TYPE)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BRAND_NAME)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_TYPE)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SKU_NAME)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_STATUS)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL_SIZE)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COMPANY_SIZE)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PERCENTAGE)), cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CREATED_AT)), cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FINISHED)) != 0, new RetrofitResponseListener() {
                    @Override
                    public void onSuccess() {
                        pgsBar.setProgress(getSharedPreferences("Radios",0).getInt("uploadedData/"+mPref.getString(Constants.MARKET_NAME, ""), 0));
                        txtView.setText(getSharedPreferences("Radios",0).getInt("uploadedData/"+mPref.getString(Constants.MARKET_NAME, ""), 0) + "/" + countOfData);
                    }

                    @Override
                    public void onFailure() {
                        dialogLoad.dismiss();
                        Toast.makeText(CategoriesActivity.this, "Məlumatların yüklənməsində problem yarandı. Zəhmət olmasa yenidən yükləyin.", Toast.LENGTH_LONG).show();
                    }
                });
            } while (cursor.moveToNext());
        }
        myDB.close();
    }

    private void insertData(@NotNull final String type, final String brand_name, final String product_type, final String sku_name, final String status, final String total_size, final String company_size, final String percentage, final String created_at, final boolean finished, final RetrofitResponseListener responseListener) {
        Call<ResponseBody> call = null;
        switch (type) {
            case "OSA":
                call = ApiClient.getInstance().getApi().InsertOSA(Constants.INSERT_OSA_PRODUCTS, brand_name, product_type, sku_name, status, mPref.getString(Constants.MARKET_MANAGERHPCFK, ""), mPref.getString(Constants.MARKET_MANAGERIC, ""), mPref.getString(Constants.MARKET_SUPERVISORHPCFK, ""), mPref.getString(Constants.MARKET_EXPEDITORHPCFK, ""), mPref.getString(Constants.MARKET_SUPERVISORHPCFK2, ""), mPref.getString(Constants.MARKET_EXPEDITORHPCFK2, ""), mPref.getString(Constants.MARKET_SUPERVISORIC, ""), mPref.getString(Constants.MARKET_EXPEDITORIC, ""), mPref.getString(Constants.MARKET_FIELD_SUPERVISOR, ""), mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), created_at);
                break;
            case "NPD":
                call = ApiClient.getInstance().getApi().InsertNPD(Constants.INSERT_NPD_PRODUCTS, brand_name, product_type, sku_name, status, mPref.getString(Constants.MARKET_MANAGERHPCFK, ""), mPref.getString(Constants.MARKET_MANAGERIC, ""), mPref.getString(Constants.MARKET_SUPERVISORHPCFK, ""), mPref.getString(Constants.MARKET_EXPEDITORHPCFK, ""), mPref.getString(Constants.MARKET_SUPERVISORHPCFK2, ""), mPref.getString(Constants.MARKET_EXPEDITORHPCFK2, ""), mPref.getString(Constants.MARKET_SUPERVISORIC, ""), mPref.getString(Constants.MARKET_EXPEDITORIC, ""), mPref.getString(Constants.MARKET_FIELD_SUPERVISOR, ""), mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), created_at);
                break;
            case "Planogram":
                call = ApiClient.getInstance().getApi().InsertPlanogram(Constants.INSERT_PLANOGRAM_PRODUCTS, product_type, sku_name, status, mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), created_at);
                break;
            case "SOS":
                call = ApiClient.getInstance().getApi().InsertSOS(Constants.INSERT_SHELF_SHARE_PRODUCTS, brand_name, product_type, total_size, company_size, percentage, mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), created_at);
                break;
            case "SOD":
                call = ApiClient.getInstance().getApi().InsertSOD(Constants.INSERT_DISPLAY_SHARE_PRODUCTS, brand_name, product_type, total_size, company_size, percentage, mPref.getString(Constants.MARKET_NAME, ""), mPref.getString(Constants.COMPANY, ""), created_at);
                break;
            default:
                Toast.makeText(CategoriesActivity.this, "Naməlum tip", Toast.LENGTH_LONG).show();
                break;
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String resp = "";
                try {
                    if (response.body() != null) {
                        resp = response.body().string().trim();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (resp.contains("success")) {
                    getSharedPreferences("Radios", 0).edit().putInt("uploadedData/" + mPref.getString(Constants.MARKET_NAME, ""), getSharedPreferences("Radios", 0).getInt("uploadedData/" + mPref.getString(Constants.MARKET_NAME, ""), 0) + 1).apply();
                    responseListener.onSuccess();
                    if (getSharedPreferences("Radios",0).getInt("uploadedData/"+mPref.getString(Constants.MARKET_NAME, ""), 0) == countOfData) {
                        allDataUploaded = true;
                        Toast.makeText(CategoriesActivity.this, "Bütün məlumatlar uğurla yükləndi.", Toast.LENGTH_LONG).show();
                        dialogLoad.dismiss();
                    }
                } else {
                    Toast.makeText(CategoriesActivity.this, resp.split(" # ")[1], Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CategoriesActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                responseListener.onFailure();
            }
        });
    }
}
