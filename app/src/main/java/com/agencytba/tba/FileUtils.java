package com.agencytba.tba;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

public class FileUtils {

    public static File getDataDir(Context context) {

        String path = context.getFilesDir().getAbsolutePath() + "/Nanay";

        File file = new File(path);

        if(!file.exists()) {

            file.mkdirs();
        }

        return file;
    }

    public static File getDataDir(Context context, String folder) {

        String path = context.getFilesDir().getAbsolutePath() + "/" + folder;
        //Toast.makeText(context, path,Toast.LENGTH_SHORT).show();
        File file = new File(path);

        if(!file.exists()) {

            file.mkdirs();

        }

        return file;
    }
}