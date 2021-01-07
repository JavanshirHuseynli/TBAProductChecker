package com.agencytba.tba.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.agencytba.tba.MainActivity;
import com.agencytba.tba.R;
import com.agencytba.tba.modals.Companies;
import com.agencytba.tba.modals.User;
import com.agencytba.tba.server.Constants;
import com.agencytba.tba.server.RequestInterface;
import com.agencytba.tba.server.RetrofitResponseListener;
import com.agencytba.tba.server.ServerRequest;
import com.agencytba.tba.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

    private static SharedPreferences mPref;
    private Dialog dialogLoad;
    // we define a list from the DevelopersList java class

    private List<Companies> companiesList;
    private static Context context;

    public CompaniesAdapter(List<Companies> companyList, Context context) {

        // generate constructors to initialise the List and Context objects

        this.companiesList = companyList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.companies, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Companies cList = companiesList.get(position);
        mPref = context.getSharedPreferences("Constants", 0);
        holder.companyN.setText(cList.getCompany_name());
        File mFileTemp = new File(context.getFilesDir() + File.separator
                + "tba_images" + File.separator
                +"company_images",cList.getImage());
        Picasso.get().load(mFileTemp).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).fit()
                .into(holder.companyI);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection()) {
                    showDialogLoading(cList.getCompany_name(), cList.getImage());
                    retrieveTasks(context, mPref.getString(Constants.UNIQUE_ID, ""), cList.getCompany_name(), new RetrofitResponseListener() {
                        @Override
                        public void onSuccess() {
                            loadUrlData(context, mPref.getString(Constants.UNIQUE_ID, ""), cList.getCompany_name(), new RetrofitResponseListener() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                } else
                    Toast.makeText(context, "İnternet bağlantınız yoxdur.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override

    public int getItemCount() {
        return companiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public TextView companyN;
        public ImageView companyI;
        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects
            companyN = (TextView) itemView.findViewById(R.id.company_name);
            companyI = (ImageView) itemView.findViewById(R.id.company_logo);
        }

    }

    public static void retrieveTasks(final Context context, String unique_id, String company, final RetrofitResponseListener retrofitResponseListener){
        final SharedPreferences pref = context.getSharedPreferences("Constants", 0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setUnique_id(unique_id);
        user.setCompany(company);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.RETRIEVE_TASKS);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(resp));
                    Log.d("whut", String.valueOf(jsonObject));
                    if (jsonObject.has("tasks")){
                        Log.d(Constants.TAG, String.valueOf(jsonObject));
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("tasks", String.valueOf(jsonObject));
                        editor.apply();
                        retrofitResponseListener.onSuccess();
                    } else {
                        Toast.makeText(context, "No tasks uploaded yet!", Toast.LENGTH_LONG).show();
                        retrofitResponseListener.onFailure();
                    }
                } catch (JSONException e) {
                    retrofitResponseListener.onFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                retrofitResponseListener.onFailure();
                Log.d(Constants.TAG,"failed");
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public static void loadUrlData(final Context context, String unique_id, String company, final RetrofitResponseListener retrofitResponseListener) {
        final SharedPreferences pref = context.getSharedPreferences("Constants", 0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setUnique_id(unique_id);
        user.setCompany(company);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CATEGORIES_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                try {

                    JSONObject jsonObject = new JSONObject(gson.toJson(resp));
                    Log.d("whut", String.valueOf(jsonObject));
                    if (jsonObject.has("category")){
                        Log.d(Constants.TAG, String.valueOf(jsonObject));
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("categories", String.valueOf(jsonObject));
                        editor.apply();
                        retrofitResponseListener.onSuccess();
                    } else {
                        retrofitResponseListener.onFailure();
                        Toast.makeText(context, "No categories uploaded yet!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    retrofitResponseListener.onFailure();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                retrofitResponseListener.onFailure();
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(Constants.TAG,"failed");
                Log.d("er",t.getLocalizedMessage());

            }
        });
    }

    private void showDialogLoading(final String company, final String cImage){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_data_dialog, null);
        builder.setView(view);
        dialogLoad = builder.create();
        dialogLoad.show();
        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialogLoad.isShowing()) {
                    dialogLoad.dismiss();
                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putString(Constants.COMPANY, company);
                    editor.putString(Constants.COMPANY_LOGO, cImage);
                    editor.apply();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
            }
        };

        dialogLoad.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 5000);
    }

    private boolean haveNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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