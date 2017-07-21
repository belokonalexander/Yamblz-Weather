package com.yamblz.voltek.weather.presentation.ui.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Alexander on 21.07.2017.
 */

public class AutoCompletableCustom extends AppCompatAutoCompleteTextView {


    public AutoCompletableCustom(Context context) {
        super(context);

    }

    public AutoCompletableCustom(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AutoCompletableCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            if (onKeyActionListener != null)
                onKeyActionListener.onAction();
            clearFocus();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if (onKeyActionListener != null)
                onKeyCancelListener.onCanel();
            clearFocus();
            return false;
        }

        return super.dispatchKeyEvent(event);
    }


    OnKeyActionListener onKeyActionListener;
    OnKeyCancelListener onKeyCancelListener;

    public void setOnKeyActionListener(OnKeyActionListener onKeyActionListener) {
        this.onKeyActionListener = onKeyActionListener;
    }

    public void setOnKeyCancelListener(OnKeyCancelListener onKeyCancelListener) {
        this.onKeyCancelListener = onKeyCancelListener;
    }

    public interface OnKeyActionListener {
        void onAction();
    }

    public interface OnKeyCancelListener {
        void onCanel();
    }
}
