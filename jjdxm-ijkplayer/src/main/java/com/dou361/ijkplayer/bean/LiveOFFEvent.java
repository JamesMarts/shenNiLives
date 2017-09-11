package com.dou361.ijkplayer.bean;

/**
 * Created by xpf on 2016/12/25 :)
 * GitHub:xinpengfei520
 * Function:这个类很简单，构造时传进去一个字符串，然后可以通过getmMsg()获取出来
 */

public class LiveOFFEvent {


    private boolean position;

    public LiveOFFEvent(boolean position) {
        this.position = position;
    }

    public boolean getPosition() {
        return position;
    }

}
