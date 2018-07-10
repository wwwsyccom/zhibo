package com.syc.zhibo.update.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.syc.zhibo.update.DownLoadService;
import com.syc.zhibo.update.util.DeviceUtils;
import com.syc.zhibo.update.util.ExternalPermissionUtils;

public class UpdateManager {
    private Context mContext;
    public UpdateManager(Context context) {
        this.mContext = context;
    }
    /**
     * 检测软件更新
     */
    public void checkUpdate(final boolean isToast) {
        /**
         * 在这里请求后台接口，获取更新的内容和最新的版本号
         */
        Log.d("ooooooooo", "checkupdate........");
        // 版本的更新信息
        String version_info = "更新内容\n" + "    1. 车位分享异常处理\n" + "    2. 发布车位折扣格式统一\n" + "    ";
        int mVersion_code = DeviceUtils.getVersionCode(mContext);// 当前的版本号
        int nVersion_code = 2;
        if (mVersion_code < nVersion_code) {
            // 显示提示对话
            showNoticeDialog(version_info);
        } else {
            if (isToast) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 显示更新对话框
     *
     * @param version_info
     */
    private void showNoticeDialog(String version_info) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
        mDialog.setTitle("版本更新");
        mDialog.setMessage(version_info);
        mDialog.setCancelable(false);
        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (ExternalPermissionUtils.isGrantExternalRW((Activity) mContext, 1)) {
                    mContext.startService(new Intent(mContext, DownLoadService.class));
                }
//                CommonProgressDialog pBar = new CommonProgressDialog(MainActivity.this);
//                pBar.setCanceledOnTouchOutside(false);
//                pBar.setTitle("正在下载");
//                pBar.setCustomTitle(LayoutInflater.from(
//                        MainActivity.this).inflate(
//                        R.layout.title_dialog, null));
//                pBar.setMessage("正在下载");
//                pBar.setIndeterminate(true);
//                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pBar.setCancelable(false);
//                pBar.show();

            }
        }).create().show();
    }
}
