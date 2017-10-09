package com.yalantis.euclid.gamevpn;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.com.shszcraft.picture.Picture;
import com.shszcraft.gamevpn.json.ArrayJson;
import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.VpnStatus;


/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class MainActivity extends EuclidActivity {


    private static Picture P = null;
    private MainActivity MainActivity = this;
    private ArrayJson results;
    private ArrayList Array_name;
    private ArrayList Lorem_short;
    private ArrayList Lorem_long;
    private ArrayList Picture_name;
    private ArrayList Picture_url;
    private ArrayList Picture_type;
    private Handler handler;
    private List<Map<String, Object>> profilesList = new ArrayList<>();
    private Message message = new Message();
    protected Drawable[] avatar;

    boolean Bart = false;
    boolean isFirst;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "正在初始化 ", Toast.LENGTH_SHORT).show();
                connectVPN();
            }
        });

        new RequestTask().execute();
        Log.v("Test", "Bart True " +Bart);
    }

    @Override
    protected BaseAdapter getAdapter() {

        Log.v("Test", "UI开始更新");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                Map<String, Object> profileMap = null;
                if(msg.what == 1){ // UI更新后 成功
                    for (int i = 0; i < Array_name.size(); i++) {
                        profileMap = new HashMap<>();
                        profileMap.put(EuclidListAdapter.KEY_AVATAR, "/Gamevpn/sync/"+Picture_name.get(i)+"."+Picture_type.get(i));
                        profileMap.put(EuclidListAdapter.KEY_NAME, Array_name.get(i));
                        profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, Lorem_short.get(i));
                        profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, Lorem_long.get(i));
                        profilesList.add(profileMap);
                        initList();
                        Log.v("Test", "UI更新后 ");
                    }
                }
                if(msg.what == 2){ // UI更新后 数据获取失败[断网？？]
                    profileMap = new HashMap<>();
                    profileMap.put(EuclidListAdapter.KEY_AVATAR, R.drawable.anastasia);
                    profileMap.put(EuclidListAdapter.KEY_NAME, "宝宝睡着了");
                    profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, " ");
                    profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, "呼呼呼呼~~");
                    profilesList.add(profileMap);
                    initList();
                    Log.v("Test", "UI更新后 数据获取失败");
                }
            }

        };
        Log.v("Test", "UI更新完成");
        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }



    private class RequestTask extends AsyncTask<Void, Void, ArrayJson> {
        @Override
        protected ArrayJson doInBackground(Void... params) {
            ArrayJson Arr = new ArrayJson("http://api.shszcraft.com/array.html");
            if (Arr != null) {
                Array_name = Arr.getArray_name();
                Lorem_short = Arr.getLorem_short();
                Lorem_long = Arr.getLorem_long();
                Picture_name = Arr.getPicture_name();
                Picture_url = Arr.getPicture_url();
                Picture_type = Arr.getPicture_type();
                new Picture(MainActivity, Picture_url, Picture_name, Picture_type);
                Log.v("Test", "G缓存完成！");
            }
            return Arr;
        }

        @Override
        protected void onPostExecute(ArrayJson result) {
            super.onPostExecute(result);
            if (result != null) {
                if (Per.getPermission()){
                    message.what = 1;
                    handler.sendMessage(message);
                }else {
                    boolean Ax = true;
                    while (Ax){
                        if (Per.getPermission()){
                            Ax = false;
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }

            } else if (result == null) {
                Toast.makeText(MainActivity.this, "请求数据失败", Toast.LENGTH_LONG).show();
                message.what = 2;
                handler.sendMessage(message);
            }
        }
    }


}
