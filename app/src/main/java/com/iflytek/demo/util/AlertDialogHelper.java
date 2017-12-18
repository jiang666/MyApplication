package com.iflytek.demo.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.demo.R;


/**
 * Created by leo on 2015/9/1.
 * OnButtonClickListener:interface Click on the button listening
 */
public class AlertDialogHelper {
    private Activity activity;
    private View popupWindow_view;
    private RelativeLayout alert;
    private AnimationSet animationIn;
    private AnimationSet animationOut;
    private Boolean isAnimation = true;
    private AlertDialog mAlertDialog;
    private Boolean isClick = false;
    private int time;
    private OnButtonClickListener listener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    closeDialog();
                    break;
            }

        }
    };

    public static AlertDialogHelper toastBuilder(Activity activity, String title, int time) {
        AlertDialogHelper helper = new AlertDialogHelper(activity);
        helper.toast(title, true, time);
        return helper;
    }

    public static AlertDialogHelper toastBuilder(Activity activity, String title, String content, int time) {
        AlertDialogHelper helper = new AlertDialogHelper(activity);
        helper.messageToast(title, content, time);
        return helper;
    }

    public static AlertDialogHelper nextBuilder(Activity activity, String title, String description, String btText) {
        AlertDialogHelper helper = new AlertDialogHelper(activity);
        helper.nextDialog(title, description, btText);
        return helper;
    }

    public static AlertDialogHelper askBuilder(Activity activity, String title, String description) {
        AlertDialogHelper helper = new AlertDialogHelper(activity);
        helper.askDialog(title, description);
        return helper;
    }

    private AlertDialogHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * Toast like notify
     *
     * @param title   The content of the TextView
     * @param isClick the true said can click disappear,the false said didn't click disappear
     * @param time    timeout to disappear, 0 will never disappear
     */
    private void toast(String title, Boolean isClick, int time) {
        this.isClick = isClick;
        this.time = time;
        popupWindow_view = LayoutInflater.from(activity)
                .inflate(R.layout.popwindow_alerts, null);
        alert = (RelativeLayout) popupWindow_view.findViewById(R.id.alert);
        LinearLayout alert_ll = (LinearLayout) popupWindow_view.findViewById(R.id.alert_simple);
        TextView alert_description = (TextView) popupWindow_view.findViewById(R.id.tv_alert_simple);
        alert_ll.setVisibility(View.VISIBLE);
        alert_description.setText(title);
        initPop();
    }

    /**
     * Toast like notify with one more message
     *
     * @param title
     * @param content
     * @param time    the 0 said didn't support the timing,the else number said number milliseconds later disappear
     */
    private void messageToast(String title, String content, int time) {
        this.time = time;
        popupWindow_view = LayoutInflater.from(activity)
                .inflate(R.layout.popwindow_alerts, null);
        alert = (RelativeLayout) popupWindow_view.findViewById(R.id.alert);
        LinearLayout alert_nomal = (LinearLayout) popupWindow_view.findViewById(R.id.ll_alert_normal);
        alert_nomal.setVisibility(View.VISIBLE);
        TextView tv_description = (TextView) popupWindow_view.findViewById(R.id.tv_alert_normal);
        TextView tv_gray = (TextView) popupWindow_view.findViewById(R.id.tv_alert_gray);
        tv_description.setText(title);
        tv_gray.setText(content);
        tv_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
                if (listener != null) {
                    listener.onClickNext();
                }
            }
        });
        initPop();
    }

    /**
     * Small dialog like notify with next step
     *
     * @param title       the alert title
     * @param description the alert descriptioncribe
     * @param btText      Click on the content of the need to perform
     */
    private void nextDialog(String title, String description, String btText) {
        popupWindow_view = LayoutInflater.from(activity)
                .inflate(R.layout.popwindow_alerts, null);
        alert = (RelativeLayout) popupWindow_view.findViewById(R.id.alert);
        LinearLayout alert_mul = (LinearLayout) popupWindow_view.findViewById(R.id.alert_mul);
        alert_mul.setVisibility(View.VISIBLE);
        TextView alert_title = (TextView) popupWindow_view.findViewById(R.id.tv_alert_title);
        TextView alert_description = (TextView) popupWindow_view.findViewById(R.id.tv_alert_des);
        RadioGroup alert_button = (RadioGroup) popupWindow_view.findViewById(R.id.alert_buttons);
        TextView buttonNext = (TextView) popupWindow_view.findViewById(R.id.tv_button_next);
        alert_button.setVisibility(View.GONE);
        buttonNext.setVisibility(View.VISIBLE);
        buttonNext.setText(btText);
        alert_description.setText(description);
        alert_title.setText(title);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
                if (listener != null) {
                    listener.onClickNext();
                }
            }
        });
        initPop();
    }

    /**
     * have YES and NO button,option to ‘YES’ or ‘NO’.does not disappear, cannot be swiped away.
     *
     * @param title       the alert title
     * @param description the alert descriptioncribe
     */
    private void askDialog(String title, String description) {
        popupWindow_view = LayoutInflater.from(activity)
                .inflate(R.layout.popwindow_alerts, null);
        alert = (RelativeLayout) popupWindow_view.findViewById(R.id.alert);
        LinearLayout alert_mul = (LinearLayout) popupWindow_view.findViewById(R.id.alert_mul);
        alert_mul.setVisibility(View.VISIBLE);
        TextView alert_title = (TextView) popupWindow_view.findViewById(R.id.tv_alert_title);
        TextView alert_description = (TextView) popupWindow_view.findViewById(R.id.tv_alert_des);
        alert_description.setText(description);
        alert_title.setText(title);
        RadioGroup alert_button = (RadioGroup) popupWindow_view.findViewById(R.id.alert_buttons);
        alert_button.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.alert_button_no:
                        closeDialog();
                        if (listener != null) {
                            listener.onClickNo();
                        }
                        break;
                    case R.id.alert_button_yes:
                        closeDialog();
                        if (listener != null) {
                            listener.onClickYes();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        initPop();
    }

    private void initPop() {
        alertIn();
        alertOut();
        alert.startAnimation(animationIn);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogStyle);
        mAlertDialog = builder.create();
        show();
        mAlertDialog.getWindow().setContentView(popupWindow_view);
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        mAlertDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams lp = mAlertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        mAlertDialog.getWindow().setAttributes(lp);
        mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                    if (isAnimation) {
                        closeDialog();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void alertIn() {
        alert.measure(0, 0);
        int popheight = alert.getMeasuredHeight();
        TranslateAnimation poptransIn = new TranslateAnimation(0, 0, (float)0.2*popheight, 0);
        poptransIn.setDuration(300);
        ScaleAnimation scaleIn = new ScaleAnimation(
                1, 1,
                1, 1.005f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        scaleIn.setDuration(50);
        scaleIn.setStartOffset(300);
        animationIn = new AnimationSet(false);
        animationIn.addAnimation(poptransIn);
        animationIn.addAnimation(scaleIn);
    }

    private void alertOut() {
        TranslateAnimation scaleOut = new TranslateAnimation(0, 0, 0, 30);
        scaleOut.setDuration(300);
        scaleOut.setFillAfter(true);

        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0);
        alpha.setDuration(300);
        alpha.setFillAfter(true);
        animationOut = new AnimationSet(false);
        animationOut.addAnimation(scaleOut);
        animationOut.addAnimation(alpha);
    }

    public void show() {
        try {
            mAlertDialog.show();
        } catch (RuntimeException e) {
            return;
        }

        if (time != 0) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);

                }
            }.start();
        }
        if (isClick) {
            popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (isAnimation) {
                            closeDialog();
                        }
                    }
                    return false;
                }
            });
        }
    }

    public void closeDialog() {
        alert.startAnimation(animationOut);
        isAnimation = false;
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimation = true;
                mAlertDialog.dismiss();
                if (listener != null) {
                    listener.onAnimationOver();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnButtonClickListener {
        void onClickNo();

        void onClickYes();

        void onClickNext();

        void onAnimationOver();
    }
}