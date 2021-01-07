package com.agencytba.tba.dbhelpers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.agencytba.tba.LoginActivity;
import com.agencytba.tba.MainActivity;
import com.agencytba.tba.server.Constants;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class UploadDBAsync extends AsyncTask<String, Void, String> {

    private Context mContext;

    public UploadDBAsync (Context context){
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... params) {

        try {
            String sourceFileUri = "/data/data/com.agencytba.tba/databases/data.db";

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {

                try {
                    String upLoadServerUri = Constants.BASE_URL +  "api/upload.php?";

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("db", sourceFileUri);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    String unique_id;
                    unique_id = mContext.getSharedPreferences("Constants",0).getString(Constants.UNIQUE_ID, "");
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    String fileName = unique_id + "-" + timeStamp;
                    dos.writeBytes("Content-Disposition: form-data; name=\"dbfile\";filename=\""
                            + fileName + ".db" + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();

                    if (serverResponseCode == 200) {

                        // messageText.setText(msg);
                        Log.d("servRes", "File Upload Complete.");
                        Handler handler =  new Handler(mContext.getMainLooper());
                        handler.post( new Runnable(){
                            public void run(){
                                Toast.makeText(mContext, "Arxiv məlumatları uğurla yükləndi.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // recursiveDelete(mDirectory1);

                    } else {
                        Handler handler =  new Handler(mContext.getMainLooper());
                        handler.post( new Runnable(){
                            public void run(){
                                Toast.makeText(mContext, "Arxiv məlumatları yüklənmədi.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();

                }
                // dialog.dismiss();

            } else Log.d("servRes", "not file");


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
