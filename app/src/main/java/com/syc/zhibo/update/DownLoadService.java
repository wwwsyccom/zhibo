package com.syc.zhibo.update;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.syc.zhibo.R;
import com.syc.zhibo.update.manager.fileload.FileCallback;
import com.syc.zhibo.update.manager.fileload.FileResponseBody;
import com.syc.zhibo.update.util.ExternalPermissionUtils;
import com.syc.zhibo.util.CommonProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class DownLoadService  extends Service {
    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "M_DEFAULT_DIR";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "zhibo.apk";
    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Retrofit.Builder retrofit;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        loadFile();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 下载文件
     */
    private void loadFile() {
//        initNotification();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        //显示进度条
//        CommonProgressDialog pBar = new CommonProgressDialog(mContext);
//        pBar = new CommonProgressDialog(mContext);
//        pBar.setCanceledOnTouchOutside(false);
//        pBar.setTitle("正在下载");
//        pBar.setCustomTitle(LayoutInflater.from( MainActivity.this).inflate( R.layout.title_dialog, null));
//        pBar.setMessage("正在下载");
//        pBar.setIndeterminate(true);
//        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pBar.setCancelable(false);
//        pBar.setMax(100);
//        pBar.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        pBar.show();

        retrofit.baseUrl("http://liaot.xyz:3333/")
            .client(initOkHttpClient())
            .build()
            .create(IFileLoad.class)
            .loadFile()
            .enqueue(new FileCallback(destFileDir, destFileName) {

                @Override
                public void onSuccess(File file) {
                    Log.e("oooooooo", "请求成功");
                    // 安装软件
//                    cancelNotification();
                    installApk(file);
                }

                @Override
                public void onLoading(long progress, long total) {
                    Log.e("ooooooooo", progress + "----" + total);
                    //发送广播
                    Intent intent=new Intent();
                    intent.putExtra("percent", (int)(progress * 100 / total));
                    intent.setAction("com.syc.zhibo.update.DownLoadService");
                    sendBroadcast(intent);
//                    updateNotification(progress * 100 / total);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("oooooooooo", "请求失败");
//                    cancelNotification();
                }
            });
    }
    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri uri = null;
        //版本大于7.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Log.d("oooooooooo", "版本大于7.0...");
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile( mContext , "com.syc.zhibo.fileProvider" , file);
        }else{
            uri = Uri.fromFile(file);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        mContext.startActivity(install);
    }
    public interface IFileLoad {
//        @GET("download")
        @GET("zhibo.apk")
        Call<ResponseBody> loadFile();
    }
    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }



}
