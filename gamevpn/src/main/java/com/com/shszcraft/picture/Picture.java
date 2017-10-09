package com.com.shszcraft.picture;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.yalantis.euclid.gamevpn.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 2017/9/17.
 */

public class Picture {

    private MainActivity MainActivity;
    private String PictrueUrl = getSDPath() + "/Gamevpn/sync/";// 文件Url
    private Bitmap bm;
    private int Timeout = 100*10;

    /**
     * 获取并缓存图片
     * @param HttpUrl 获取图片源地址
     * @param Picture_name 图片名字
     */
    public Picture(MainActivity MainActivity, ArrayList HttpUrl , ArrayList Picture_name, ArrayList Types) {
        this.MainActivity = MainActivity;
        //PictrueUrl = getSDPath() + "/Gamevpn/sync/";
        String Url = "http://api.shszcraft.com";
        String T = null;
        NewFile(PictrueUrl);
        // 如果文件夹不存在则创建
        for (int i = 0; Picture_name.size() > i; i ++){
            Log.v("Test", "Long  " + Picture_name.size());
            T = (String) Types.get(i);
            Log.v("Test", "T " + PictrueUrl + Picture_name.get(i) + "." + T);

            File Pictrue = new File(PictrueUrl + Picture_name.get(i) + "." + T);
            if(Pictrue.exists()) {
                Log.v("Test", "文件存在");
            }else {
                a(Pictrue, Url + HttpUrl.get(i).toString());
                Log.v("Test", "文件不存在, 开始缓存");
            }
        }
   }

   public Picture(){

   }

    public boolean NewFile(String fileUrl){
       File file = new File(fileUrl);
       // 如果文件夹不存在则创建
       if (!file.exists()) {
           Log.v("Test", "目录不存在，正在创建 " + file.getParentFile());
           file.mkdirs();
           return true;
       } else {
           Log.v("Test", "目录存在");
           return false;
       }
   }

    private void a(File PictrueUrl, String Http){

        URL url = null;
        try {
            //发送http请求
            url = new URL(Http);
            //获取连接对象，并没有建立连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接和读取超时
            conn.setConnectTimeout(Timeout);
            conn.setReadTimeout(Timeout);
            //设置请求方法，注意必须大写
            conn.setRequestMethod("GET");
            //建立连接，发送get请求
            //conn.connect();
            //建立连接，然后获取响应吗，200说明请求成功
            //Log.v("Test", String.valueOf(conn.getResponseCode()));
            if(conn.getResponseCode() == 200){
                //获取服务器返回的流，流里就是客户端请求的数据
                InputStream is = conn.getInputStream();

                //我们自己读取流里的数据，读取1k，就把1k写到本地文件缓存起来
                byte[] b = new byte[1024];
                int len;

                FileOutputStream fos = new FileOutputStream(PictrueUrl);
                while((len = is.read(b)) != -1){
                    fos.write(b, 0, len);
                }
                fos.close();

                //因为缓存时已经把流里数据读取完了，此时流里没有任何数据
                bm = BitmapFactory.decodeFile(PictrueUrl.getAbsolutePath());

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();//发送http请求
        } catch (ProtocolException e) {
            e.printStackTrace();//获取连接对象，并没有建立连接
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

}
