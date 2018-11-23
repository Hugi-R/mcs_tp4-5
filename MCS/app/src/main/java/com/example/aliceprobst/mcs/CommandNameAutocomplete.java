package com.example.aliceprobst.mcs;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

public class CommandNameAutocomplete extends AppCompatAutoCompleteTextView {


    public CommandNameAutocomplete(Context context) {
        super(context);
    }

    public CommandNameAutocomplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
/*
    @Override
    protected void performFiltering (CharSequence text, int keyCode) {

    }

    @Override
    protected void replaceText (CharSequence text) {

    }
    */

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
        }
    }


}
