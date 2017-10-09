package com.shszcraft.gamevpn.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 2017/9/17.
 */

public class ArrayJson {

    private String result;
    private ArrayList Picture_type = new ArrayList();
    private ArrayList Picture_url = new ArrayList();
    private ArrayList Picture_name = new ArrayList();
    private ArrayList lorem_long = new ArrayList();
    private ArrayList lorem_short = new ArrayList();
    private ArrayList array_name = new ArrayList();
    private JSONObject Array;

    public ArrayJson(String HttpUrl){
        //创建一个JSON对象
        JSONObject result  = null;
        try {
            result  = new JSONObject(getHttpJson(HttpUrl));//.getJSONObject("array")
            JSONArray ArrayList = result.getJSONArray("array");
            for(int i = 0; i < ArrayList.length(); i++) {//遍历JSONArray
                Array = ArrayList.getJSONObject(i);

                array_name.add(Array.getString("array_names"));
                lorem_short.add(Array.getString("lorem_short"));
                lorem_long.add(Array.getString("lorem_long"));

                //Log.v("Test", String.valueOf(Array));
                //Log.v("Test", String.valueOf(array_name));
                //Log.v("Test", String.valueOf(lorem_short));
                //Log.v("Test", String.valueOf(lorem_long));

                JSONArray picture = Array.getJSONArray("picture");
                for (int ix = 0;ix<picture.length(); ix++){
                    JSONObject pictureArray = picture.getJSONObject(i);

                    Picture_name.add(pictureArray.getString("name"));
                    Picture_url.add(pictureArray.getString("url"));
                    Picture_type.add(pictureArray.getString("type"));

                    //Log.v("Test", String.valueOf(Picture_name));
                    //Log.v("Test", String.valueOf(Picture_url));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHttpJson(String HttpUrl) {
        int code;
        StringBuffer buffer = new StringBuffer();
        try {
            String path;
            URL url = new URL(HttpUrl);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");//使用GET方法获取
            conn.setConnectTimeout(5000);
            code = conn.getResponseCode();
            if (code == 200) {
                /**
                 * 如果获取的code为200，则证明数据获取是正确的。
                 */
                InputStream is = conn.getInputStream();

                //将返回的输入流转换成字符
                InputStreamReader inputStreamReader = new InputStreamReader(is, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                // 释放资源
                is.close();
                is = null;
                conn.disconnect();
                result = buffer.toString();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList getArray_name(){
        return array_name;
    }
    public ArrayList getLorem_short(){
        return lorem_short;
    }
    public ArrayList getLorem_long(){
        return lorem_long;
    }
    public ArrayList getPicture_name(){
        return Picture_name;
    }
    public ArrayList getPicture_url(){
        return Picture_url;
    }
    public ArrayList getPicture_type(){
        return Picture_type;
    }


}
