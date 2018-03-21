package android.lectcoding.ui.logcat.base;

import android.lectcoding.ui.logcat.Logcat;
import android.lectcoding.ui.logcat.LogcatUtil;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(Logcat.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(Logcat.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        LogcatUtil.printLine(tag, true);
        message = headString + Logcat.LINE_SEPARATOR + message;
        String[] lines = message.split(Logcat.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        LogcatUtil.printLine(tag, false);
    }
}
