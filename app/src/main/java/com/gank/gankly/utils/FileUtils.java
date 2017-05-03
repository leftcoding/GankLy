package com.gank.gankly.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by LingYan on 2016-05-31
 */
public class FileUtils {

    public static void writeFile(InputStream in, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        } else {
            file.delete();
        }

        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024 * 128];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();
    }

    public static String getGlideDefaultPath(Context context) {
        if (context == null) {
            throw new NullPointerException("context can't be null");
        }
        String path = context.getCacheDir().getAbsolutePath();
        if (isSDCard()) {
            String directory = Environment.getExternalStorageDirectory() + "/GankLy/cache/img";
            File file = new File(directory);
            if (!file.exists() && file.mkdirs()) {
                return directory;
            }
        }
        return path;
    }

    public static boolean isSDCard() {
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
