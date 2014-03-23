package com.totoro.freelancer.sdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.view.View.OnClickListener;

/**
 * custom AutoCompleteTextView. default, system widget has one limit that ,the
 * mini value of property called 'android:completionThreshold' is 1. so goal to
 * create this view makes you could browser the tip-list when it get focus, even
 * you hav't input one word.
 * 
 * @author bing
 * 
 */
public class MyAutoCompleteTextView extends AutoCompleteTextView implements OnClickListener {

    public MyAutoCompleteTextView(Context context) {
		super(context);
		setOnClickListener(this);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	setOnClickListener(this);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs,
	    int defStyle) {
	super(context, attrs, defStyle);
	setOnClickListener(this);
    }

    @Override
    public boolean enoughToFilter() {
    	return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
	    Rect previouslyFocusedRect) {
	super.onFocusChanged(focused, direction, previouslyFocusedRect);
	if (focused && getFilter() != null) {
	    performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
	}

    }

    @Override
    public void onClick(View v) {
	showDropDown();
    }

}
