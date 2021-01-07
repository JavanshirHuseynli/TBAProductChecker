package com.agencytba.tba;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agencytba.tba.adapters.CompaniesAdapter;
import com.agencytba.tba.modals.Companies;
import com.agencytba.tba.modals.User;
import com.agencytba.tba.server.Constants;
import com.agencytba.tba.server.RequestInterface;
import com.agencytba.tba.server.ServerRequest;
import com.agencytba.tba.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompaniesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SharedPreferences mPref;
    private List<Companies> companiesList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);
        mPref = getSharedPreferences("Constants", 0);
        companiesList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (haveNetworkConnection()) {
            loadUrlData(mPref.getString(Constants.UNIQUE_ID, ""));
        } else
            Toast.makeText(CompaniesActivity.this, "İnternet bağlantınız yoxdur.", Toast.LENGTH_SHORT).show();

    }

    private void loadUrlData(String unique_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setUnique_id(unique_id);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.COMPANIES_OPERATION);
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
                    if (jsonObject.has("companies")){
                        JSONArray array = jsonObject.getJSONArray("companies");
                        Log.d(Constants.TAG, String.valueOf(array));

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jo = array.getJSONObject(i);

                            Companies cmpns = new Companies(jo.getString("company_name"), jo.getString("image"), jo.getString("created_at"));
                            companiesList.add(cmpns);

                        }
                        adapterSet();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(CompaniesActivity.this, "No companies uploaded yet!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                //progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                //progress.setVisibility(View.INVISIBLE);
                Toast.makeText(CompaniesActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(Constants.TAG,"failed");
                Log.d("er",t.getLocalizedMessage());

            }
        });
    }

    public void adapterSet(){
        adapter = new CompaniesAdapter(companiesList, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
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

}
