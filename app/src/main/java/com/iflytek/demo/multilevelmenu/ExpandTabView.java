package com.iflytek.demo.multilevelmenu;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.iflytek.demo.R;

import java.util.ArrayList;

/**
 * Created by jianglei on 2017/3/20.
 */

public class ExpandTabView extends LinearLayout implements OnDismissListener {
    private static final String TAG = "ExpandTabView";
    private ToggleButton selectedButton;
    private ArrayList<String> mTextArray = new ArrayList<String>();
    private ArrayList<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
    private ArrayList<ToggleButton> mToggleButton = new ArrayList<ToggleButton>();
    private Context mContext;
    private final int SMALL = 0;
    private int displayWidth;
    private int displayHeight;
    private PopupWindow popupWindow;
    private int selectPosition;

    public ExpandTabView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 根据选择的位置设置tabitem显示的值
     */
    public void setTitle(String valueText, int position) {
        if (position < mToggleButton.size()) {
            mToggleButton.get(position).setText(valueText);
        }
    }

    public void setTitle(String title){

    }
    /**
     * 根据选择的位置获取tabitem显示的值
     */
    public String getTitle(int position) {
        if (position < mToggleButton.size() && mToggleButton.get(position).getText() != null) {
            return mToggleButton.get(position).getText().toString();
        }
        return "";
    }

    /**
     * 设置tabitem的个数和初始值
     * @param textArray 标题数组
     * @param viewArray 控件数组
     */
    public void setValue(ArrayList<String> textArray, ArrayList<View> viewArray) {
        if (mContext == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG,"setValue");
        mTextArray = textArray;
        for (int i = 0; i < viewArray.size(); i++) {
            final RelativeLayout r = new RelativeLayout(mContext);
            int maxHeight = (int) (displayHeight * 0.7);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
            rl.leftMargin = 10;
            rl.rightMargin = 10;
            r.addView(viewArray.get(i), rl);
            mViewArray.add(r);
            r.setTag(SMALL);
            ToggleButton tButton = (ToggleButton) inflater.inflate(R.layout.toggle_button, this, false);
            addView(tButton);
            View line = new TextView(mContext);
            line.setBackgroundResource(R.drawable.choosebar_line);
            if (i < viewArray.size() - 1) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
                addView(line, lp);
            }
            mToggleButton.add(tButton);
            tButton.setTag(i);
            tButton.setText(mTextArray.get(i));

            r.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RelativeLayout","view:"+v);
                    onPressBack();
                }
            });

            r.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));
            tButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("tButton","setOnClickListener(l)");
                    // initPopupWindow();
                    ToggleButton tButton = (ToggleButton) view;

                    if (selectedButton != null && selectedButton != tButton) {
                        selectedButton.setChecked(false);
                    }
                    selectedButton = tButton;
                    selectPosition = (Integer) selectedButton.getTag();
                    startAnimation();
                    if (mOnButtonClickListener != null && tButton.isChecked()) {
                        mOnButtonClickListener.onClick(selectPosition);
                    }
                }
            });
        }// for..
    }

    private void startAnimation() {
        Log.d(TAG,"startAnimation");
        if (popupWindow == null) {
            Log.d(TAG,"startAnimation(),new popupWindow now");
            popupWindow = new PopupWindow(mViewArray.get(selectPosition), displayWidth, displayHeight);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
        }
        Log.d(TAG,"startAnimation(),selectedButton:"+selectedButton+",isChecked:"+selectedButton.isChecked()+
                ",popupWindow.isShowing:"+popupWindow.isShowing());
        if (selectedButton.isChecked()) {

            if (!popupWindow.isShowing()) {
                showPopup(selectPosition);
            } else {
                popupWindow.setOnDismissListener(this);
                popupWindow.dismiss();
                hideView();
            }
        } else {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                hideView();
            }
        }
    }

    private void showPopup(int position) {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.show();
        }
        if (popupWindow.getContentView() != mViewArray.get(position)) {
            popupWindow.setContentView(mViewArray.get(position));
        }
        popupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        Log.d(TAG,"onPressBack");
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
            if (selectedButton != null) {
                selectedButton.setChecked(false);
            }
            return true;
        } else {
            return false;
        }

    }

    private void hideView() {
        Log.d(TAG, "hide()");
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
        }
    }

    private void init(Context context) {
        mContext = context;
        displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void onDismiss() {
        Log.d(TAG,"onDismiss,selectPosition:"+selectPosition);
        showPopup(selectPosition);
        popupWindow.setOnDismissListener(null);
    }

    private OnButtonClickListener mOnButtonClickListener;

    /**
     * 设置tabitem的点击监听事件
     */
    public void setOnButtonClickListener(OnButtonClickListener l) {
        mOnButtonClickListener = l;
    }

    /**
     * 自定义tabitem点击回调接口
     */
    public interface OnButtonClickListener {
        public void onClick(int selectPosition);
    }

}
