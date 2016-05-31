package com.gank.gankly.utils;

import android.os.Environment;

import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by LingYan on 2016-05-31
 */
public class FileUtils {

    public static void writeFile(InputStream in, String fileName) throws IOException {
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()  + "/GankLy/" + fileName);
        KLog.d(file.getAbsolutePath());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        } else {
            file.delete();
        }

        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024 * 128];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();
    }
}
