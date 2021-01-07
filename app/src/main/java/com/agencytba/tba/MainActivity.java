package com.agencytba.tba;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agencytba.tba.adapters.CompaniesAdapter;
import com.agencytba.tba.adapters.MarketsAdapter;
import com.agencytba.tba.dbhelpers.DBHelper;
import com.agencytba.tba.dbhelpers.UploadDBAsync;
import com.agencytba.tba.dbhelpers.UploadFileAsync;
import com.agencytba.tba.modals.Markets;
import com.agencytba.tba.modals.User;
import com.agencytba.tba.server.Constants;
import com.agencytba.tba.server.RequestInterface;
import com.agencytba.tba.server.RetrofitResponseListener;
import com.agencytba.tba.server.ServerRequest;
import com.agencytba.tba.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.agencytba.tba.adapters.CompaniesAdapter.retrieveTasks;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    public static SharedPreferences mPref, Pref, firstLaunch;
    private String[] markets;
    private List<Markets> marketsList;
    private MarketsAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean searchOpened;
    private SearchView searchView;
    public static int countFinished = 0;
    public static int countRemaining = 0;
    private String marketCount;
    LinearLayout waitingLayout;
    android.app.AlertDialog dialogLoad;
    String unique_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mPref = getSharedPreferences("Constants", 0);
        Pref = getSharedPreferences("Radios", 0);
        firstLaunch = getSharedPreferences("firstLaunch", 0);
        marketsList = new ArrayList<>();
        waitingLayout = findViewById(R.id.waitingLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        unique_id = mPref.getString(Constants.UNIQUE_ID, "");
        loadUrlDataMarketCount(unique_id, mPref.getString(Constants.COMPANY, ""));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void loadUrlData(String unique_id, String company) {

        if (haveNetworkConnection()) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestInterface requestInterface = retrofit.create(RequestInterface.class);

            User user = new User();
            user.setUnique_id(unique_id);
            user.setCompany(company);
            ServerRequest request = new ServerRequest();
            request.setOperation(Constants.MARKETS_OPERATION);
            request.setUser(user);
            Call<ServerResponse> response = requestInterface.operation(request);
            response.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                    ServerResponse resp = response.body();
                    //Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    try {

                        JSONObject jsonObject = new JSONObject(gson.toJson(resp));
                        Log.d("whut", String.valueOf(jsonObject));
                        if (jsonObject.has("markets")) {
                            JSONArray array = jsonObject.getJSONArray("markets");
                            Log.d(Constants.TAG, String.valueOf(array));
                            countRemaining = array.length();
                            countFinished = 0;
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject jo = array.getJSONObject(i);

                                boolean done = Pref.getBoolean(jo.getString("market_name")+"/",false);

                                Markets mrkts = new Markets(jo.getString("market_name"), jo.getString("market_address"), jo.getString("city"), jo.getString("categories_to_check"), jo.getString("manager"), jo.getString("manager2"), jo.getString("supervisor"), jo.getString("supervisor2"), jo.getString("supervisor3"), jo.getString("expeditor"), jo.getString("expeditor2"), jo.getString("expeditor3"), jo.getString("field_supervisor"), jo.getString("location"), jo.getString("company"), jo.getString("market_type"), jo.getString("created_at"), done, false);
                                marketsList.add(mrkts);

                                SharedPreferences marketPref = getSharedPreferences("MarketAnalyze", 0);

                                if (!marketCount.contains(jo.getString("market_name"))) {
                                    mPref.edit().putInt(jo.getString("market_name") + "/count", 0).apply();
                                }

                                setCounts(jo.getString("market_name"), jo.getString("city"), jo.getString("market_type"), jo.getString("categories_to_check"));

                                if (marketPref.getInt(jo.getString("market_name") + "OSA", 100) == 0) {
                                    if (marketPref.getInt(jo.getString("market_name") + "NPD", 100) == 0) {
                                        if (marketPref.getInt(jo.getString("market_name") + "Planogram", 100) == 0) {
                                            if (marketPref.getInt(jo.getString("market_name") + "SOD", 100) == 0) {
                                                if (marketPref.getInt(jo.getString("market_name") + "OSAIC", 100) == 0) {
                                                    if (marketPref.getInt(jo.getString("market_name") + "NPDIC", 100) == 0) {
                                                        if (marketPref.getInt(jo.getString("market_name") + "PlanogramIC", 100) == 0) {
                                                            if (marketPref.getInt(jo.getString("market_name") + "SOSIC", 100) == 0) {
                                                                marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", true).apply();
                                                                countFinished++;
                                                                countRemaining--;
                                                            } else
                                                                marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                                        } else
                                                            marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                                    } else
                                                        marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                                } else
                                                    marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                            } else
                                                marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                        } else
                                            marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                    } else
                                        marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                                } else
                                    marketPref.edit().putBoolean(jo.getString("market_name") + "/monthly", false).apply();
                            }
                            firstLaunch.edit().putBoolean("firstLaunch", false).apply();
                            firstLaunch.edit().putBoolean("firstLaunchMonth", false).apply();
                            adapterSet();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            waitingLayout.setVisibility(View.GONE);
                            Objects.requireNonNull(getSupportActionBar()).show();
                            Toast.makeText(MainActivity.this, "No markets uploaded yet!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(Constants.TAG, "failed");
                    Log.d("er", t.getLocalizedMessage());
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "İnternet Bağlantısı yoxdur!", Toast.LENGTH_LONG).show();
        }
    }

    public void adapterSet(){
        adapter = new MarketsAdapter(marketsList, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(getSupportActionBar()).show();
        recyclerView.setVisibility(View.VISIBLE);
        waitingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem admin_panel = menu.findItem(R.id.admin_panel);
        /*if (!mPref.getString(Constants.UNIQUE_ID, "").equals("3138cC"))
            admin_panel.setVisible(false);*/
        searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchOpened = true;
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchOpened = false;
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        switch (item.getItemId()) {
            case R.id.logout:
                if (!mPref.getString(Constants.UNIQUE_ID, "").equals("3138cC")) {
                    new UploadDBAsync(MainActivity.this).execute("");
                    getSharedPreferences("ADMIN", 0).edit().putString("UNIQUE_ID_EMPLOYEE", mPref.getString(Constants.UNIQUE_ID, "")).apply();
                }
                getSharedPreferences("Constants",0).edit().clear().apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.new_data:
                showDialogLoading();
                break;
            case R.id.download_images:
                LoginActivity.downloadImages(MainActivity.this);
                break;
            case R.id.remove_pref:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Məlumatları silmək")
                        .setMessage("Həftə ərzində girdiyiniz bütün cavablar və məlumatlar silinəcək. Davam edilsin?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getSharedPreferences("Radios",0).edit().clear().apply();
                                new UploadDBAsync(MainActivity.this).execute("");
                                startActivity(new Intent(MainActivity.this,MainActivity.class));

                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(R.string.no, null)
                        .setIcon(R.drawable.ic_warning)
                        .show();
                break;
            case R.id.admin_panel:
                startActivity(new Intent(MainActivity.this, AdminPanelActivity.class));
                break;
            case R.id.upload_db:
                new UploadDBAsync(MainActivity.this).execute("");
                break;
            case R.id.upload_xml:
                new UploadFileAsync(MainActivity.this).execute("");
                break;
            case R.id.remove_db:
                DBHelper myDB = new DBHelper(this);
                myDB.close();
                getApplicationContext().deleteDatabase(DBHelper.DATABASE_NAME);
                Toast.makeText(MainActivity.this, "Arxiv məlumatları silindi.", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onRefresh() {
        loadUrlDataMarketCount(unique_id, mPref.getString(Constants.COMPANY, ""));
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }

    public boolean isSearchOpened() {
        return searchOpened;
    }

    private void loadUrlDataMarketCount(final String unique_id, String company) {

        if (haveNetworkConnection()) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestInterface requestInterface = retrofit.create(RequestInterface.class);

            User user = new User();
            user.setUnique_id(unique_id);
            user.setCompany(company);
            ServerRequest request = new ServerRequest();
            request.setOperation(Constants.MARKET_COUNT);
            request.setUser(user);
            Call<ServerResponse> response = requestInterface.operation(request);
            response.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                    ServerResponse resp = response.body();
                    //Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    try {

                        JSONObject jsonObject = new JSONObject(gson.toJson(resp));

                        //Toast.makeText(MainActivity.this, String.valueOf(jsonObject.get("market_count").toString().split("STAR mart",-1).length-1), Toast.LENGTH_SHORT).show();
                        if (jsonObject.has("market_count")) {
                            JSONArray array = jsonObject.getJSONArray("market_count");
                            marketCount = String.valueOf(jsonObject);
                            String[] arrayMarketName = new String[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
                                Log.d(Constants.TAG, String.valueOf(jsonObject));
                                mPref.edit().putInt(jo.getString("market_name") + "/count", jo.getInt("market_count")).apply();
                            }
                            loadUrlData(unique_id, mPref.getString(Constants.COMPANY, ""));
                        } else {
                            if (resp != null) {
                                marketCount = "empty";
                                loadUrlData(unique_id, mPref.getString(Constants.COMPANY, ""));
                            } else {
                                Toast.makeText(MainActivity.this, "Sistemdə tənzimləmələr aparılır. Zəhmət olmasa birazdan yenidən yoxlayın.", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    //progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(Constants.TAG, "failed");
                    Log.d("er", t.getLocalizedMessage());

                }
            });
        } else {
            Toast.makeText(MainActivity.this, "İnternet Bağlantısı yoxdur!", Toast.LENGTH_LONG).show();
        }
    }

    public void setCounts(String market_name, @NotNull String city, String market_type, String categories_to_check) {
        SharedPreferences marketPref = getSharedPreferences("MarketAnalyze", 0);
        SharedPreferences.Editor editor = marketPref.edit();
        if (city.equals("Bakı") || city.equals("Sumqayıt") || city.equals("Xırdalan")) {
            switch (market_type) {
                case "Modern Trade":
                    switch (categories_to_check) {
                        case "ALL":
                            editor.putInt(market_name + "OSA", 4);
                            editor.putInt(market_name + "NPD", 1);
                            editor.putInt(market_name + "Planogram", 1);
                            editor.putInt(market_name + "SOS", 1);
                            editor.putInt(market_name + "SOD", 1);
                            editor.putInt(market_name + "OSAIC", 4);
                            editor.putInt(market_name + "NPDIC", 4);
                            editor.putInt(market_name + "PlanogramIC", 1);
                            editor.putInt(market_name + "SOSIC", 1);
                            break;
                        case "IC":
                            editor.putInt(market_name + "OSA", 0);
                            editor.putInt(market_name + "NPD", 0);
                            editor.putInt(market_name + "Planogram", 0);
                            editor.putInt(market_name + "SOS", 0);
                            editor.putInt(market_name + "SOD", 0);
                            editor.putInt(market_name + "OSAIC", 4);
                            editor.putInt(market_name + "NPDIC", 4);
                            editor.putInt(market_name + "PlanogramIC", 1);
                            editor.putInt(market_name + "SOSIC", 1);
                            break;
                        case "HPCFK":
                            editor.putInt(market_name + "OSA", 4);
                            editor.putInt(market_name + "NPD", 1);
                            editor.putInt(market_name + "Planogram", 1);
                            editor.putInt(market_name + "SOS", 1);
                            editor.putInt(market_name + "SOD", 1);
                            editor.putInt(market_name + "OSAIC", 0);
                            editor.putInt(market_name + "NPDIC", 0);
                            editor.putInt(market_name + "PlanogramIC", 0);
                            editor.putInt(market_name + "SOSIC", 0);
                            break;
                    }
                    break;
                case "Traditional Trade":
                    switch (categories_to_check) {
                        case "ALL":
                            editor.putInt(market_name + "OSA", 1);
                            editor.putInt(market_name + "NPD", 1);
                            editor.putInt(market_name + "Planogram", 0);
                            editor.putInt(market_name + "SOS", 0);
                            editor.putInt(market_name + "SOD", 0);
                            editor.putInt(market_name + "OSAIC", 2);
                            editor.putInt(market_name + "NPDIC", 2);
                            editor.putInt(market_name + "PlanogramIC", 1);
                            editor.putInt(market_name + "SOSIC", 1);
                            break;
                        case "IC":
                            editor.putInt(market_name + "OSA", 0);
                            editor.putInt(market_name + "NPD", 0);
                            editor.putInt(market_name + "Planogram", 0);
                            editor.putInt(market_name + "SOS", 0);
                            editor.putInt(market_name + "SOD", 0);
                            editor.putInt(market_name + "OSAIC", 2);
                            editor.putInt(market_name + "NPDIC", 2);
                            editor.putInt(market_name + "PlanogramIC", 1);
                            editor.putInt(market_name + "SOSIC", 1);
                            break;
                        case "HPCFK":
                            editor.putInt(market_name + "OSA", 1);
                            editor.putInt(market_name + "NPD", 1);
                            editor.putInt(market_name + "Planogram", 0);
                            editor.putInt(market_name + "SOS", 0);
                            editor.putInt(market_name + "SOD", 0);
                            editor.putInt(market_name + "OSAIC", 0);
                            editor.putInt(market_name + "NPDIC", 0);
                            editor.putInt(market_name + "PlanogramIC", 0);
                            editor.putInt(market_name + "SOSIC", 0);
                            break;
                    }
                    break;
                case "Pharmacy":
                    editor.putInt(market_name + "OSA", 1);
                    editor.putInt(market_name + "NPD", 1);
                    editor.putInt(market_name + "Planogram", 0);
                    editor.putInt(market_name + "SOS", 0);
                    editor.putInt(market_name + "SOD", 0);
                    editor.putInt(market_name + "OSAIC", 0);
                    editor.putInt(market_name + "NPDIC", 0);
                    editor.putInt(market_name + "PlanogramIC", 0);
                    editor.putInt(market_name + "SOSIC", 0);
                    break;
                case "HORECA":
                    editor.putInt(market_name + "OSA", 0);
                    editor.putInt(market_name + "NPD", 0);
                    editor.putInt(market_name + "Planogram", 0);
                    editor.putInt(market_name + "SOS", 0);
                    editor.putInt(market_name + "SOD", 0);
                    editor.putInt(market_name + "OSAIC", 1);
                    editor.putInt(market_name + "NPDIC", 0);
                    editor.putInt(market_name + "PlanogramIC", 0);
                    editor.putInt(market_name + "SOSIC", 0);
                    break;
            }
        } else {
            if (market_type.equals("Modern Trade")) {
                editor.putInt(market_name + "OSA", 1);
                editor.putInt(market_name + "NPD", 1);
                editor.putInt(market_name + "Planogram", 1);
                editor.putInt(market_name + "SOS", 1);
                editor.putInt(market_name + "SOD", 1);
                editor.putInt(market_name + "OSAIC", 0);
                editor.putInt(market_name + "NPDIC", 0);
                editor.putInt(market_name + "PlanogramIC", 0);
                editor.putInt(market_name + "SOSIC", 0);
            } else if (market_type.equals("Traditional Trade")) {
                editor.putInt(market_name + "OSA", 1);
                editor.putInt(market_name + "NPD", 1);
                editor.putInt(market_name + "Planogram", 0);
                editor.putInt(market_name + "SOS", 0);
                editor.putInt(market_name + "SOD", 0);
                editor.putInt(market_name + "OSAIC", 0);
                editor.putInt(market_name + "NPDIC", 0);
                editor.putInt(market_name + "PlanogramIC", 0);
                editor.putInt(market_name + "SOSIC", 0);
            }
        }
        editor.apply();

        if (marketPref.getInt(market_name + "OSA", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "OSA", marketPref.getInt(market_name + "OSA", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "OSA", 0).apply();
        if (marketPref.getInt(market_name + "NPD", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "NPD", marketPref.getInt(market_name + "NPD", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "NPD", 0).apply();
        if (marketPref.getInt(market_name + "Planogram", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "Planogram", marketPref.getInt(market_name + "Planogram", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "Planogram", 0).apply();
        if (marketPref.getInt(market_name + "SOS", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "SOS", marketPref.getInt(market_name + "SOS", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "SOS", 0).apply();
        if (marketPref.getInt(market_name + "SOD", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "SOD", marketPref.getInt(market_name + "SOD", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "SOD", 0).apply();
        if (marketPref.getInt(market_name + "OSAIC", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "OSAIC", marketPref.getInt(market_name + "OSAIC", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "OSAIC", 0).apply();
        if (marketPref.getInt(market_name + "NPDIC", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "NPDIC", marketPref.getInt(market_name + "NPDIC", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "NPDIC", 0).apply();
        if (marketPref.getInt(market_name + "PlanogramIC", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "PlanogramIC", marketPref.getInt(market_name + "PlanogramIC", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "PlanogramIC", 0).apply();
        if (marketPref.getInt(market_name + "SOSIC", 0) >= mPref.getInt(market_name+"/count", 0))
            marketPref.edit().putInt(market_name + "SOSIC", marketPref.getInt(market_name + "SOSIC", 0) - mPref.getInt(market_name+"/count", 0)).apply();
        else
            marketPref.edit().putInt(market_name + "SOSIC", 0).apply();
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

    private void showDialogLoading(){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater =getLayoutInflater();
        View view = inflater.inflate(R.layout.loading_data_dialog, null);
        builder.setView(view);
        retrieveTasks(MainActivity.this, unique_id, mPref.getString(Constants.COMPANY, ""), new RetrofitResponseListener() {
                    @Override
                    public void onSuccess() {
                        CompaniesAdapter.loadUrlData(MainActivity.this, unique_id, mPref.getString(Constants.COMPANY, ""), new RetrofitResponseListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(MainActivity.this, "Məlumatlar uğurla yeniləndi.", Toast.LENGTH_LONG).show();
                                dialogLoad.dismiss();
                            }

                            @Override
                            public void onFailure() {
                                dialogLoad.dismiss();
                                Toast.makeText(MainActivity.this, "Məlumatlar yüklənmədi. Yenidən yoxlayın.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        dialogLoad.dismiss();
                        Toast.makeText(MainActivity.this, "Məlumatlar yüklənmədi. Yenidən yoxlayın.", Toast.LENGTH_LONG).show();
                    }
                });

        dialogLoad = builder.create();
        dialogLoad.show();
    }
}
