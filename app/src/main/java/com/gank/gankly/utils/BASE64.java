package com.gank.gankly.utils;

import android.content.Context;

import com.gank.gankly.BuildConfig;
import com.socks.library.KLog;

public class BASE64 {

    static class EncipherConst {
        public static final String m_strKey1 = "zxcvbnm,./asdfg";
        public static final String m_strKey2 = "hjkl;'qwertyuiop";
        public static final String m_strKey3 = "[]\\1234567890-";
        public static final String m_strKey4 = "=` ZXCVBNM<>?:LKJ";
        public static final String m_strKey5 = "HGFDSAQWERTYUI";
        public static final String m_strKey6 = "OP{}|+_)(*&^%$#@!~";
        public static final String m_strKeya = "cjk;";
        public static final String m_strKeyb = "cai2";
        public static final String m_strKeyc = "%^@#";
        public static final String m_strKeyd = "*(N";
        public static final String m_strKeye = "%^HJ";

        EncipherConst() {
        }
    }

    public static String decryptBASE64(String str, Context con) {
        if (!(StringUtils.hasChinese(str) || StringUtils.getString(con, "psw3", BuildConfig.pwd).equals(BuildConfig.pwd))) {
            str = DecodePasswd(str);
        }
//        KLog.d("zkzk", "de:  " + str);
        return str;
    }

    public static String encryptBASE64(String str, Context con) {
        StringUtils.saveString(con, "psw3", "v3");
        if (!StringUtils.hasChinese(str)) {
            str = EncodePasswd(str);
        }
//        KLog.d("zkzk", "en:  " + str);
        return str;
    }

    public static String EncodePasswd(String strPasswd) {
        String strEncodePasswd = new String(BuildConfig.pwd);
        String des = new String();
        String strKey = new String();
        if (strPasswd == null || strPasswd.length() == 0) {
            return BuildConfig.pwd;
        }
        strKey = "zxcvbnm,./asdfghjkl;'qwertyuiop[]\\1234567890-=` ZXCVBNM<>?:LKJHGFDSAQWERTYUIOP{}|+_)(*&^%$#@!~";
        while (strPasswd.length() < 8) {
            strPasswd = strPasswd + '\u0001';
        }
        des = BuildConfig.pwd;
        int n = 0;
        while (n <= strPasswd.length() - 1) {
            char code;
            int i;
            int i2;
            do {
                char mid;
                code = (char) ((int) Math.rint(Math.random() * 100.0d));
                while (code > '\u0000' && ((strPasswd.charAt(n) ^ code) < 0 || (strPasswd.charAt(n) ^ code) > 90)) {
                    code = (char) (code - 1);
                }
                int flag = code ^ strPasswd.charAt(n);
                if (flag > 93) {
                    mid = '\u0000';
                } else {
                    mid = strKey.charAt(flag);
                }
                if (code > '#') {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                if (code < 'z') {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 &= i;
                if (code != '|') {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 &= i;
                if (code != '\'') {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 &= i;
                if (code != ',') {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 &= i;
                if (mid != '|') {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 &= i;
                if (mid != '\'') {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 &= i;
                if (mid != ',') {
                    i = 1;
                } else {
                    i = 0;
                }
            } while ((i & i2) == 0);
            KLog.d(des);
            des = des + code + strKey.charAt(strPasswd.charAt(n) ^ code);
            KLog.d(des);
            n++;
        }
        return des;
    }

    public static String DecodePasswd(String varCode) {
        String des = new String();
        String strKey = new String();
        if (varCode == null || varCode.length() == 0) {
            return BuildConfig.pwd;
        }
        int n;
        strKey = "zxcvbnm,./asdfghjkl;'qwertyuiop[]\\1234567890-=` ZXCVBNM<>?:LKJHGFDSAQWERTYUIOP{}|+_)(*&^%$#@!~";
        if (varCode.length() % 2 != 0) {
            varCode = varCode + "?";
        }
        des = BuildConfig.pwd;
        for (n = 0; n <= (varCode.length() / 2) - 1; n++) {
            des = des + ((char) (varCode.charAt(n * 2) ^ strKey.indexOf(varCode.charAt((n * 2) + 1))));
        }
        n = des.indexOf(1);
        return n > 0 ? des.substring(0, n) : des;
    }
}
