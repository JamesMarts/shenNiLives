/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shenni.lives.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

/**
 * <p>这就是随便封装了下，我希望起到抛砖引玉的作用，发挥你的强大脑力吧。</p>
 * Created by Yan Zhenjie on 2017/5/1.
 */
public class PermissionRequest {

    private Context mContext;
    private PermissionCallback mCallback;

    public PermissionRequest(Context context, PermissionCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        request();
    }

    /**
     * 申请权限
     */
    public void request() {
        AndPermission.with(mContext)
                .requestCode(110)
                .permission(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .callback(this)
                .start();
    }

    @PermissionYes(110)
    public void yes(List<String> permissions) {
        this.mCallback.onSuccessful();
    }

    @PermissionNo(110)
    public void no(List<String> permissions) {
        successDialog(mContext, 2);
        this.mCallback.onFailure();
    }

    public interface PermissionCallback {
        void onSuccessful();

        void onFailure();
    }


    /**
     * <p>
     * title 提示标题
     */
    public static void successDialog(final Context context, final int type) {
        String tiele = "";
        tiele = "请允许\"" + context.getString(com.dou361.ijkplayer.R.string.app_name) + "\"拥有必要的权限";
        new AlertDialog.Builder(context)
                .setTitle(tiele)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("设置", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                        if (type == 1) {
//                            位置开关
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        } else {
                            //设置页面
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            context.startActivity(intent);
                        }

                    }
                })
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 点击“确认”后的操作
                            }
                        }).show();

    }


}
