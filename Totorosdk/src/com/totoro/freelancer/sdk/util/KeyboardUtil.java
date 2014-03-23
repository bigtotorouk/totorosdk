package com.totoro.freelancer.sdk.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
	/**
	 * 强制隐藏软键盘
	 * @param view
	 * @param context
	 */
	public static void hideKeyBoard(View view, Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);// 强制隐藏键盘
	}
}
