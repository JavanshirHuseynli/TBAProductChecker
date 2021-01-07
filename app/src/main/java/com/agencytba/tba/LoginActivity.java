package com.agencytba.tba;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agencytba.tba.modals.User;
import com.agencytba.tba.server.Constants;
import com.agencytba.tba.server.RequestInterface;
import com.agencytba.tba.server.ServerRequest;
import com.agencytba.tba.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText unique_id;
    static Button login_btn;
    private static ProgressBar progress;
    private static SharedPreferences pref;
    private static SharedPreferences ref;
    private SharedPreferences firstLaunch;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_STORAGE_PERMISSION_CODE = 101;
    private static final int READ_PHONE_STATE_PERMISSION_CODE = 102;
    private static LoginActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        unique_id = findViewById(R.id.unique_id);
        login_btn = findViewById(R.id.login);
        progress = (ProgressBar) findViewById(R.id.progress);
        Objects.requireNonNull(getSupportActionBar()).hide();
        pref = getSharedPreferences("Constants", 0);
        ref = getSharedPreferences("images", 0);
        firstLaunch = getSharedPreferences("firstLaunch", 0);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection()) {
                    String imei_id = String.valueOf(getDeviceId(LoginActivity.this));
                    //String imei_id = "869719032016052";
                    loginProcess(unique_id.getText().toString(), imei_id);
                    progress.setVisibility(View.VISIBLE);
                    login_btn.setEnabled(false);
                } else
                    Toast.makeText(LoginActivity.this, "İnternet bağlantınız yoxdur.", Toast.LENGTH_SHORT).show();
            }
        });

        if (pref.getBoolean(Constants.IS_LOGGED_IN, false)) {
            if (!pref.getString(Constants.COMPANY, "").equals("")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, CompaniesActivity.class);
                startActivity(intent);
            }
        }
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE_PERMISSION_CODE);
    }

    private void loginProcess(final String unique_id, final String imei_id) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);
        builder.addInterceptor(interceptor);
        OkHttpClient httpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setUnique_id(unique_id);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                if (resp != null) {
                    if (resp.getResult().equals(Constants.SUCCESS)) {
                        if (imei_id.equals(resp.getUser().getImei_id())) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.UNIQUE_ID, resp.getUser().getUnique_id());
                            editor.putString(Constants.EMPLOYEE_NAME, resp.getUser().getName());
                            editor.putString(Constants.EMPLOYEE_SURNAME, resp.getUser().getSurname());
                            editor.putString(Constants.EMPLOYEE_CITY, resp.getUser().getCity());
                            editor.apply();
                            progress.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(LoginActivity.this, CompaniesActivity.class);
                            startActivity(intent);
                            login_btn.setEnabled(true);
                            pref.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply();
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            login_btn.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Zəhmət olmasa sizə verilən şirkət planşetindən istifadə edəsiniz.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                        login_btn.setEnabled(true);
                    }
                } else
                    Toast.makeText(LoginActivity.this, "Sistemdə tənzimləmələr aparılır. Zəhmət olmasa birazdan yenidən yoxlayın.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, "failed");
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                login_btn.setEnabled(true);

            }
        });
    }

    public static void downloadImages(@NotNull final Context context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progressbar_layout, null);
        final ProgressBar pgsBar = (ProgressBar) view.findViewById(R.id.pBar);
        final TextView txtView = (TextView) view.findViewById(R.id.tView);
        builder.setView(view);

        final android.app.AlertDialog dialogLoad = builder.create();

        final SharedPreferences preferences = context.getSharedPreferences("Constants", 0);

        String serverFilePath = "https://agencytba.com/images.zip";

        String path = FileUtils.getDataDir(context).getAbsolutePath();

        String fileName = "tba_images";
        File file = new File(path, fileName);

        String localPath = file.getAbsolutePath();
        String unzipPath = com.agencytba.tba.FileUtils.getDataDir(context, "tba_images").getAbsolutePath();

        FileDownloadService.DownloadRequest downloadRequest = new FileDownloadService.DownloadRequest(serverFilePath, localPath);
        downloadRequest.setRequiresUnzip(true);
        downloadRequest.setDeleteZipAfterExtract(false);
        downloadRequest.setUnzipAtFilePath(unzipPath);

        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {

            @Override
            public void onDownloadStarted() {
                Toast.makeText(context, "Şəkillər yüklənir...", Toast.LENGTH_SHORT).show();
                dialogLoad.show();
            }

            @Override
            public void onDownloadCompleted() {
                if (!preferences.getBoolean(Constants.IS_LOGGED_IN, false)) {
                    pref.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply();
                    ref.edit().putBoolean("imagesDownloaded", true).apply();
                    progress.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(context, CompaniesActivity.class);
                    context.startActivity(intent);
                    login_btn.setEnabled(true);
                }
                Toast.makeText(context, "Şəkillər yükləndi!", Toast.LENGTH_SHORT).show();
                dialogLoad.dismiss();
            }

            @Override
            public void onDownloadFailed() {
                if (!preferences.getBoolean(Constants.IS_LOGGED_IN, false))
                    login_btn.setEnabled(true);
                dialogLoad.dismiss();
            }

            @Override
            public void onDownloadProgress(int progress) {
                pgsBar.setProgress(progress);
                txtView.setText(progress+"/"+pgsBar.getMax());
            }
        };

        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
        downloader.download(context);

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{permission},
                    requestCode);
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == WRITE_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            } else {
                Toast.makeText(LoginActivity.this,
                        "İcazə verilmədi. Zəhmət olmasa, icazəni təsdiqləyin.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_PERMISSION_CODE);
            } else {
                Toast.makeText(LoginActivity.this,
                        "İcazə verilmədi. Zəhmət olmasa, icazəni təsdiqləyin.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == READ_PHONE_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(LoginActivity.this,
                        "İcazə verilmədi. Zəhmət olmasa, icazəni təsdiqləyin.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
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
        Log.i("update_status", "Network is available : FALSE ");
        return false;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        return telephonyManager.getDeviceId();
    }
}
