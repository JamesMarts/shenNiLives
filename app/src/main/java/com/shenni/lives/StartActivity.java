package com.shenni.lives;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.shenni.lives.activity.LoginActivity;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.PermissionRequest;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;

import java.io.File;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.Response;


public class StartActivity extends BaseActivity {

    public BDLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private String address;


    private static Dialog dialog;
    private static TextView vLoading_text;
    private PermissionRequest permissionRequest;

    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            address = location.getProvince() + location.getCity();
            if (!StringUtil.isNullOrEmpty(location.getCity()))
                SPUtils.put("city", location.getCity());
            Log.e("onReceiveLocation: ", address);
            {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.e("地位测试", "onReceiveLocation:" + sb.toString());
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.e("s", s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initOkGo();
        noActionBar();
        permissionRequest = new PermissionRequest(this, new PermissionRequest.PermissionCallback() {
            @Override
            public void onSuccessful() {
                checkUpdae();
            }

            @Override
            public void onFailure() {
                toast("必要权限申请失败");
            }
        });
//        checkUpdae();
//        PermissionGen.with(this)
//                .addRequestCode(100)
//                .permissions(//添加必须需要的权限
////                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_PHONE_STATE,
////                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.CAMERA
//                )
//                .request();

        //这里是第二种方法 消除启动前黑白屏
        setTranslucentStatus(true);

//        jumptoActivity();

    }

    private void initOkGo() {
        //配置参数
        OkGo.getInstance()
                .setConnectTimeout(3 * 1000)  //全局的连接超时时间
//                .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                .setReadTimeOut(10 * 1000)     //全局的读取超时时间
                .setWriteTimeOut(10 * 1000)    //全局的写入超时时间
                //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                .setRetryCount(0);
    }

    private void jumptoActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (StringUtil.isNullOrEmpty(SPUserUtils.sharedInstance().getUid())) {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }


    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @PermissionSuccess(requestCode = 100)
    public void doSuccess() {
//        doNext();
//        initLocation();
    }

    @PermissionFail(requestCode = 100)
    public void doFail() {
//        doNext();
//        initLocation();

    }

    /**
     * 定位
     */
    public void initLocation() {
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        InitLocation();
        mLocationClient.start();
    }

    private void InitLocation() {
        // 设置定位参数
        LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(15 * 60 * 1000); // 15分钟扫描1次
        option.setTimeOut(15 * 1000);
        // 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
        option.setAddrType("all");
        option.setIsNeedAddress(true);
        // 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
//        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setProdName("通过GPS定位我当前的位置");
        // 禁用启用缓存定位数据
        option.disableCache(true);


        mLocationClient.setLocOption(option);
    }


    /**
     * 检查更新
     */
    private void checkUpdae() {
        OkGo.get(Api.GET_USE_CORE)
                .tag(this)
                .params("channel", Constants.CHANNEL_APP_ID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("获取设置金额：", s);
                        CoreBean coreBean = GsonUtil.parseJsonWithGson(s, CoreBean.class);
                        if (null != coreBean && coreBean.getStatus() == 1) {
                            final CoreBean.ConBean conBean = coreBean.getCon();
                            if (null != conBean)
                                //测试版可大于服务器版本
                                if (!StringUtil.isNullOrEmpty(conBean.getAd_version()))
                                    if (MyApplication.getVersionCode() >= Integer.valueOf(conBean.getAd_version().trim())) {
//服务器卡顿时候  避免请求时间过长的问题
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {


//                if (AntiEmulator.isMoni(StartActivity.this)) {
//                if (MyApplication.isEmulator(StartActivity.this)) {
//                    ToastUtil.longToast(getApplication(), "禁止模拟器运行");
//                    finish();
//                } else
//                                            Log.e("sdfadfasdf", "run: " + SPUserUtils.sharedInstance().isBoolean());
                                                if (!StringUtil.isNullOrEmpty(conBean.getQq()))
                                                    SPUtils.put("User_qq", conBean.getQq());//400
                                                if (StringUtil.isNullOrEmpty(SPUserUtils.sharedInstance().getUid())) {
                                                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }, SPUserUtils.sharedInstance().isBoolean() ? 1000 : 1000);
                                    } else {
//                                    showMyDialog(StartActivity.this);
                                        successDialog(StartActivity.this, conBean.getAd_address());
                                    }
                                else
                                    jumptoActivity();
                            else
                                jumptoActivity();
                        } else jumptoActivity();

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        jumptoActivity();
                        super.onError(call, response, e);
                    }
                });
//        else {
//            conBean.setDfh((int) SPUtils.get("User_dfh", 0));
//            conBean.setGw((int) SPUtils.get("User_gw", 0));
//            conBean.setZs((int) SPUtils.get("User_zs", 0));
//        }
    }


    /**
     * 更新提示
     * <p>
     * title 提示标题
     */
    public void successDialog(final Context context, final String URL) {
        String tiele = "";
        tiele = "发现新版本，请立即升级";
        new AlertDialog.Builder(context)
                .setTitle(tiele)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("立即更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upApk(URL);
                    }
                })
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 点击“确认”后的操作
                                ((Activity) context).finish();
                            }
                        })
                .show();

    }

    private void upApk(String URL) {
        OkGo.get(URL)
                .tag(this)
                .execute(new FileCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        showMyDialog(StartActivity.this);
                    }

                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        installApk(file);
                        closeMyDialog();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        jumptoActivity();
                        super.onError(call, response, e);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        System.out.println("downloadProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);
                        if (null != vLoading_text) {

                            vLoading_text.setText("已下载" + (Math.round(progress * 10000) * 1.0f / 100) + "%");
                        }
//                        String downloadLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
//                        String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
//                        tvDownloadSize.setText(downloadLength + "/" + totalLength);
//                        String netSpeed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);
//                        tvNetSpeed.setText(netSpeed + "/S");
//                        tvProgress.setText((Math.round(progress * 10000) * 1.0f / 100) + "%");
//                        pbProgress.setMax(100);
//                        pbProgress.setProgress((int) (progress * 100));
                    }
                });


    }

    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file),
//                MyApplication.CONTENT_TYPE_APK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    /**
     * 自定义提示无标题加载框
     *
     * @param context
     */
    public static void showMyDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_loading, null);
        vLoading_text = (TextView) view.findViewById(R.id.loading_text);
        if (null == dialog) {
            dialog = new Dialog(context, R.style.MyLoadDialog);
            //空白是否退出
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            //回键是否退出
            dialog.setCancelable(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogs) {
                    dialog = null;
                }
            });
            dialog.show();
        }
    }

    /**
     * 关闭自定义加载框
     */
    public static void closeMyDialog() {
        try {
            if (null != dialog) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }
}
