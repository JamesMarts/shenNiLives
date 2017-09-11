package com.shenni.lives.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;


/**
 * Created by lijiafu on 16/6/1.
 */
public abstract class PPWUtil {
    private static PopupWindow popView;
    private static View view;

    public static void dismissPop() {
        try {
            popView.dismiss();
//            popView = null;
        } catch (Exception e) {
        }
    }

    /**
     * 以id组件为参考
     * 显示layout
     */
    public static View showPop(Context context, int id, int layout) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout, null);

            // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
            popView = new PopupWindow(view,
                    context.getResources().getDisplayMetrics().widthPixels,
                    context.getResources().getDisplayMetrics().heightPixels, true);

            popView.setFocusable(true);
            popView.setOutsideTouchable(true);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        popView.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            // window.setAnimationStyle(R.style.mypopwindow_anim_style);
            // 在底部显示
            popView.showAtLocation(((Activity) context).findViewById(id), Gravity.BOTTOM, 0, 0);
//        window.showAsDropDown(((Activity) context).findViewById(id));
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popView.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {

                }

            });
            // popWindow消失监听方法
//        window.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                window.dismiss();
//                return true;
//            }
//        });
            return view;
        } catch (Exception w) {
            return null;
        }
    }


}
