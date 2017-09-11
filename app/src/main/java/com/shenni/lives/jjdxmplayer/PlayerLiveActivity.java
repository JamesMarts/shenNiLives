package com.shenni.lives.jjdxmplayer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.bean.LiveOFFEvent;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.shenni.lives.R;
import com.shenni.lives.activity.PlayerEndActivity;
import com.shenni.lives.fragment.ChatFragment;
import com.shenni.lives.jjdxmplayer.bean.LiveBean;
import com.shenni.lives.jjdxmplayer.module.ApiServiceUtils;
import com.shenni.lives.jjdxmplayer.utlis.MediaUtils;
import com.shenni.lives.utils.GoldPay;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.shenni.lives.utils.SPUtils.get;


/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网科技控股有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015/11/18 9:40
 * <p/>
 * 描 述：直播全屏竖屏场景
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class PlayerLiveActivity extends AutoLayoutActivity {

    private FrameLayout mFrameLayout;
    private PlayerView player;
    private Context mContext;
    private View rootView;
    private List<LiveBean> list;
    public String mAid;
    public int freetime;
    private int monResume = 0;
    //    private String url = "http://hdl.9158.com/live/744961b29380de63b4ff129ca6b95849.flv";
//    http://hls.yy.com/80362119_80362119_100571200.flv
//    private String url = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";
//    String url = "http://www.snsbao.com/zhibo.mp4";
    String url = "";
    String bigpic = "";
    //    1 视频 2直播源
    private int videotype = 1;
    private String title = "标题";
    private PowerManager.WakeLock wakeLock;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (list.size() > 1) {
                url = list.get(1).getLiveStream();
                title = list.get(1).getNickname();
            }
            player.setPlaySource(url)
                    .startPlay();
        }
    };

    protected SPUserUtils config;
    //level:观看权限[0:屌丝视频,1:帅哥视频,2:土豪视频,3:走私视频]
    private int level = -200;
    int a = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveOFFEvent event) {
        /* Do something */

        if (videotype == 2)
            if (event.getPosition()) {
                if (a == 0) {
                    a++;
                    Log.e("", "onMessageEvent: zhibodaoqi");
//            EventBus.getDefault().post(new RefreshMainEvent(true));
                    Intent intent = new Intent(PlayerLiveActivity.this, PlayerEndActivity.class);
                    //int
                    intent.putExtra("online", 2);
                    startActivity(intent);
                    finish();
                }
            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);

        // 在要接收消息的页面的OnCreate()中注册EventBus
        EventBus.getDefault().register(this);
        //状态 判断
        if (savedInstanceState != null) {
            Log.d("HELLO", "HELLO：应用进程被回收后，状态恢复");
            mAid = savedInstanceState.getString("mAid");
            url = savedInstanceState.getString("source");
            bigpic = savedInstanceState.getString("bigpic");
            freetime = savedInstanceState.getInt("freetime");
            level = savedInstanceState.getInt("liveLevel");
            videotype = savedInstanceState.getInt("videotype", 1);
        }
        if (null != getIntent()) {
            try {
                mAid = getIntent().getStringExtra("liveAid");
                url = getIntent().getStringExtra("source");
//                url = "http://hls.yy.com/80362119_80362119_100571200.flv";
//                url = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
                StringBuilder sb = new StringBuilder(url);
                if (url.toLowerCase().startsWith("https")) {
                    url = sb.replace(0, url.indexOf(":"), "http").toString();
                }
                bigpic = getIntent().getStringExtra("bigpic");
                level = getIntent().getIntExtra("liveLevel", -100);
                freetime = getIntent().getIntExtra("freetime", 60);
                videotype = getIntent().getIntExtra("videotype", 1);
                if (!StringUtil.isNullOrEmpty(mAid)) {
                    config = SPUserUtils.sharedInstance();
                }
            } catch (Exception e) {
                Log.e("PlayerLiveActivity have Exception:", e.toString());
            }
        }
        mFrameLayout = (FrameLayout) rootView.findViewById(R.id.chat_fragment);

        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        int liveend = (int) SPUtils.get(mAid + "liveend", -11);
        if (liveend != 1)
            player = new PlayerView(this, rootView)
                    .setLive(videotype == 2 ? true : false)
                    .setTitle(title)
                    .setScaleType(PlayStateParams.fitparent)
                    .hideMenu(true)
                    .hideSteam(true)
                    .setForbidDoulbeUp(true)
                    .hideCenterPlayer(true)
                    .hideControlPanl(true)
                    .setAutoReConnect(true, 10 * 1000)
//                    .setChargeTie( true, 6)
//                    .setChargeTie(config.getLevel().equalsIgnoreCase("0") ? true : false, 60)
                    .setChargeTie(config.getLevel().equalsIgnoreCase("0") ? true : false, freetime)
                    .showThumbnail(new OnShowThumbnailListener() {
                        @Override
                        public void onShowThumbnail(ImageView ivThumbnail) {
                            Glide.with(mContext)
                                    .load(StringUtil.isNullOrEmpty(bigpic) ? R.drawable.icon_live_star : bigpic)
                                    .placeholder(R.color.cl_default)
                                    .error(R.color.cl_error)
                                    .into(ivThumbnail);
                        }
                    }).setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                            //准备完成
                            if (i == PlayStateParams.STATE_PREPARED) {
                                chatView();
                                if (videotype == 1)
                                    seetk();
                                setResult(RESULT_OK, new Intent());
                                SPUtils.put(mAid + "endduration", player.getProgresEnd(player.getDuration()));
                            }

                            //播放结束
                            if (i == PlayStateParams.STATE_COMPLETED) {
                                setResult(RESULT_OK, new Intent());
                                SPUtils.put(mAid + "liveend", 1);
                                Intent intent = new Intent(PlayerLiveActivity.this, PlayerEndActivity.class);
                                intent.putExtra("liveAid", mAid);
                                //int 视频等级
                                intent.putExtra("liveLevel", level);
                                startActivity(intent);
                                finish();
                            }
                            return false;
                        }
                    });
        else {
            finish();
            return;
        }
        new Thread() {
            @Override
            public void run() {
                //这里多有得罪啦，网上找的直播地址，如有不妥之处，可联系删除
                list = ApiServiceUtils.getLiveList();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("liveAid", mAid);
        outState.putString("source", url);
        outState.putString("bigpic", bigpic);
        outState.putInt("freetime", freetime);
        outState.putInt("liveLevel", level);
        outState.putInt("videotype", videotype);
    }

    private void seetk() {
        Log.e("6465465464654", "seetk: ");
        if (videotype == 2) {
            Log.e("测试", "直播 ");
            return;
        }
//        if ((url.trim().endsWith(".flv"))
//                || (url.trim().endsWith(".m3u8"))
//                || (url.trim().endsWith(".mov"))
//                || (url.trim().endsWith(".sdp"))
//                || (url.trim().endsWith(".hks"))
//                )
//            return;

        if ((long) get(mAid, -1L) > 0) {
            //退出时的现实时间
//            long curtime = (long) get("Data" + mAid, -1L);
            //再进来时的 时间差
//            long gtpTime = DateUtil.getGrpTime(curtime);
            //视频进度=退出时的播放进度+时间差
            long seek = (long) get(mAid, -1L) + Long.valueOf(15 * 1000);

            if (seek < player.getDuration())
                player.seekTo((int) seek);
            else
                player.seekTo((int) (long) get(mAid, -1L));
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("6465465464654", "onStop: ");
        Log.d("当前播放位置：", "" + player.getCurrentPosition());
//        if ((url.trim().endsWith(".flv"))
//                || (url.trim().endsWith(".m3u8"))
//                || (url.trim().endsWith(".mov"))
//                || (url.trim().endsWith(".sdp"))
//                || (url.trim().endsWith(".hks"))
//                )
//            return;
        if (videotype == 2) {
            Log.e("测试", "直播 ");
            return;
        }

        if (player.getCurrentPositionLong() != 0) {
            if ((long) get(mAid, -1L) < player.getCurrentPositionLong())
                SPUtils.put(mAid, player.getCurrentPositionLong());
//            SPUtils.put("Data" + mAid, DateUtil.getCurTime());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        monResume = 0;
        Log.e("6465465464654", "onPause: ");
        if (videotype == 1) {
            if (player.getCurrentPositionLong() != 0) {
                SPUtils.put(mAid, player.getCurrentPositionLong());
//            SPUtils.put("Data" + mAid, DateUtil.getCurTime());
            }
        }
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("6465465464654", "onResume: ");
        if (monResume >= 1) return;
        try {
            monResume++;
//            if ((url.trim().endsWith(".flv"))
//                    || (url.trim().endsWith(".m3u8"))
//                    || (url.trim().endsWith(".mov"))
//                    || (url.trim().endsWith(".sdp"))
//                    || (url.trim().endsWith(".hks"))
//                    )
//                return;

            if (player != null) {
                player.onResume();
            }
            MediaUtils.muteAudioFocus(mContext, false);
            if (wakeLock != null) {
                wakeLock.acquire();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        if (wakeLock != null) {
            try {
                wakeLock.release();
            } catch (Exception e) {
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        monResume = 0;
        GoldPay.paySuccessAfter(PlayerLiveActivity.this, SPUserUtils.sharedInstance().getUid());
    }

    /**
     * 上层添加聊天界面 不影响其他页面的播放添加可见判断
     */
    private void chatView() {
        if (View.GONE == mFrameLayout.getVisibility()) {
            mFrameLayout.setVisibility(View.VISIBLE);
//            noActionBar();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            ChatFragment mFragment = new ChatFragment(player);
            Bundle bundle = new Bundle();
            bundle.putString("liveAid", mAid);
            bundle.putInt("videotype", videotype);
            mFragment.setArguments(bundle);

            transaction.replace(R.id.chat_fragment, mFragment).commit();
        }
    }

}
