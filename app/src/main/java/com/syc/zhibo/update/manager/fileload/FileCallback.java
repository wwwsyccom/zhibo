package com.syc.zhibo.update.manager.fileload;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class FileCallback implements Callback<ResponseBody>{
    /**
     * 订阅下载进度
     */
    private CompositeSubscription rxSubscriptions = new CompositeSubscription();
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();// 订阅下载进度
    }
    /**
     * 成功后回调
     */
    public abstract void onSuccess(File file);

    /**
     * 下载过程回调
     */
    public abstract void onLoading(long progress, long total);

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File saveFile(Response<ResponseBody> response) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048*10];
        int len;
        try {
            Log.d("ooooooo,destFileDir=", destFileDir);
            Log.d("ooooooo,destFileName=", destFileName);
            File dir = new File(destFileDir);
            if (!dir.exists()) {// 如果文件不存在新建一个
                Log.d("oooooooo", "文件不存在");
                dir.mkdirs();
            }
            in = response.body().byteStream();
            Log.d("oooooooo,in.available=", in.available()+"");
            File file = null;
            try{
                file = new File(dir,destFileName);
            }catch (Exception e){
                Log.d("oooooooo,e=", e.toString());
            }
            Log.d("ooooo,file.size=", file.length()+"");

            out = new FileOutputStream(file);
//            Log.d("oooooo,out.available=", out.);
            while ((len = in.read(buf)) != -1){
                out.write(buf,0,len);
            }
            // 回调成功的接口
            onSuccess(file);
            unSubscribe();// 取消订阅
            return file;
        }finally {
            in.close();
            out.close();
        }
    }
    /**
     * 订阅文件下载进度
     */
    private void subscribeLoadProgress() {
        rxSubscriptions.add(RxBus.getDefault()
                .toObservable(FileLoadingBean.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FileLoadingBean>() {
                    @Override
                    public void call(FileLoadingBean fileLoadEvent) {
                        onLoading(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
                    }
                }));
    }
    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
        if (!rxSubscriptions.isUnsubscribed()) {
            rxSubscriptions.unsubscribe();
        }
    }

}
