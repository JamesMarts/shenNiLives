package com.shenni.lives.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.MainActivity;
import com.shenni.lives.MyApplication;
import com.shenni.lives.R;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.LoginBean;
import com.shenni.lives.utils.GoldPay;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.NetworkUtil;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.shenni.lives.widget.CustomVideoView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    String appUid;


    public BDLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    @InjectView(R.id.id_video)
    CustomVideoView videoView;
    private String address;

    private int add = 0;

    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            address = location.getProvince() + location.getCity();
            if (!StringUtil.isNullOrEmpty(location.getCity())) {
                SPUtils.put("city", location.getCity());
                add++;
            } else {
//                successDialog(LoginActivity.this, 2);
                return;
            }
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
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        GoldPay.getCore(this);
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(//添加必须需要的权限
//                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA
                )
                .request();
       InitView();
        initVideoView(false);
//        getPersimmions();

//        initLocation();


    }


    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    public void InitView() {
        noActionBar();
    }

    /**
     * 初始化
     */
    private void initVideoView() {
        //设置播放加载路径
//        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.loginmovie));
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome_media2));
        //播放
        videoView.start();
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

    }


    /**
     * 初始化
     *
     * @param voice 是否有声音
     */
    private void initVideoView(final boolean voice) {
        //设置播放加载路径
//        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.loginmovie));
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome_media2));

        //播放
        if (voice)
            videoView.start();
        else
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f, 0f);
                    mp.start();
                    videoView.start();
                }
            });
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (!voice)
                    mediaPlayer.setVolume(0.0F, 0.0F);
                videoView.start();
            }
        });

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


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @RequiresApi(23)
    @OnClick(R.id.btn_login)
    void mLoginNow() {
        appUid = MyApplication.getAndroidID();
        Log.e("测试UID", appUid);
        if (NetworkUtil.isNetworkAvailable(this)) {
            SPUtils.put("appuid", appUid);
            login();

        } else toast("请链接网络");
//                startActivity(new Intent(this, JjdxmMainActivity.class));
    }

    @RequiresApi(23)
    private void logins() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 定位精确位置
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            successDialog(LoginActivity.this, 2);
            return;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            successDialog(LoginActivity.this, 2);
            return;
        }


        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            successDialog(LoginActivity.this, 1);
            return;
        } else {
            login();
        }
    }

    private void login() {

//        if (StringUtil.isNullOrEmpty((String) SPUtils.get("city", ""))) {
//            successDialog(LoginActivity.this, 2);
//            return;
//        }
//        if (add == 0) {
//            successDialog(LoginActivity.this, 2);
//            return;
//        }
        String uids = SPUserUtils.sharedInstance().getUid();
        OkGo.get(Api.LOGIN_URL)
                .tag(LoginActivity.this)
                .params("uid", StringUtil.isNullOrEmpty(uids) ? "" : uids)
//                .params("uid", StringUtil.isNullOrEmpty(uids) ? appUid : uids)
                .params("openid", appUid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("login", s);

                        LoginBean bean = GsonUtil.parseJsonWithGson(s, LoginBean.class);
                        if (null != bean && 1 == bean.getStatus()) {
                            LoginBean.UserBean ubean = bean.getUser();
                            config.setUid(ubean.getUid());
                            config.setNickname(ubean.getNickname());
                            config.setHeadpic(ubean.getHeadpic());
                            config.setSex(ubean.getSex());
                            config.setLevel(ubean.getLevel());
                            config.setWallet(ubean.getWallet());
                            config.setStatus(ubean.getStatus());
                            config.setFocus(ubean.getFocus());

                            config.savePreferences();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            finish();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("loginonError", e.toString());
                    }
                });
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
     * 成功提示
     * <p>
     * title 提示标题
     */
    public static void successDialog(final Context context, final int type) {
        String tiele = "";
        tiele = "请允许\"猫咪直播\"获取您的位置信息";
        new AlertDialog.Builder(context)
                .setTitle(tiele)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("设置", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                        if (type == 1) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
        if (videoView != null) {
            //释放掉占用的内存
            videoView.suspend();
        }
    }
}
