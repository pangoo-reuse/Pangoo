package org.os.netcore.net.convert;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;

/**
 * Created by admin on 2018/1/2.
 */

public class InternalJsonConvert {
    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private static boolean isBlank(String text) {
        try {
            Object json = new JSONTokener(text).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) json;
                return jsonObject.length() <= 0;

            } else if (json instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) json;
                return jsonArray.length() <= 0;
            }else if (json instanceof String){
                String s = (String) json;
                return TextUtils.isEmpty(s);
            }else if (json instanceof  Boolean
                    || json instanceof  Integer
                    || json instanceof  Long
                    || json instanceof  Double
                    || json instanceof Float
                    || json instanceof  Byte
                    || json instanceof  Short
            ){
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    public static  <D> D  parseJsonObject(String result, Type type, ParserConfig config, int featureValues,
                                 Feature... features) throws JSONException {
        if (!TextUtils.isEmpty(result)) {
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                int code = jsonObject.getInt("code");
//                String message = jsonObject.getString("msg");
//                String data = jsonObject.getString("data");
//
//
//                if (!isBlank(data)) {
//                    return JSON.parseObject(jsonObject.toString(), type);
//                }
//                return JSON.parseObject(jsonObject.toString(), type);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            return JSON.parseObject(result, type);
        }
        return null;

    }

}
