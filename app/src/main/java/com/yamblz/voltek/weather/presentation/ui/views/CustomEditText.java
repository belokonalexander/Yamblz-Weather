package com.yamblz.voltek.weather.presentation.ui.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.yamblz.voltek.weather.utils.LogUtils;


/**
 * edit text, который реагирует на события клавиатуры: ON_DONE и BACK_PRESSED
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {


    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            if (onKeyActionListener != null)
                onKeyActionListener.onAction(getText().toString());
            clearFocus();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        LogUtils.log("Focus: " + focused);
        if (focused) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(this, 0);

        } else {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }


    }


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {

            clearFocus();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    OnKeyActionListener onKeyActionListener;

    public void setOnKeyActionListener(OnKeyActionListener onKeyActionListener) {
        this.onKeyActionListener = onKeyActionListener;
    }

    public interface OnKeyActionListener {
        void onAction(String text);
    }

}
