package android.lectcoding.ui.logcat.base;

import android.lectcoding.ui.logcat.Logcat;
import android.util.Log;

public class BaseLog {

    private static final int MAX_LENGTH = 4000;

    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int length = msg.length();
        int countOfSub = length / MAX_LENGTH;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + MAX_LENGTH);
                printSub(type, tag, sub);
                index += MAX_LENGTH;
            }
            printSub(type, tag, msg.substring(index, length));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case Logcat.V:
                Log.v(tag, sub);
                break;
            case Logcat.D:
                Log.d(tag, sub);
                break;
            case Logcat.I:
                Log.i(tag, sub);
                break;
            case Logcat.W:
                Log.w(tag, sub);
                break;
            case Logcat.E:
                Log.e(tag, sub);
                break;
            case Logcat.A:
                Log.wtf(tag, sub);
                break;
        }
    }

}
