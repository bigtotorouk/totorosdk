package com.totoro.freelancer.sdk.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * tool class wrapped EditText. it has add a TextWatcher to it. you could see
 * the toast message after you 'length' value. a TextView is used for show left
 * length of the EditText.
 * 
 * @author bing
 * 
 */
public class EditTextWatcher {
    private Context context;
    private EditText editTxt;
    private List<InputFilter> filters;
    private TextView textView;

    private int MAX_COUNT = 10;

    /* toast it when length of the content beyond the limit */
    private String msg = "字数不能超过 " + MAX_COUNT + "个汉字";

    public EditTextWatcher(Context context, EditText editTxt, TextView textView) {
	this.context = context;
	this.editTxt = editTxt;
	this.textView = textView;
	this.editTxt.addTextChangedListener(textWatcher);
	filters = new ArrayList<InputFilter>();
    }

    public void setLimit(int maxCount, String msg) {
	this.MAX_COUNT = maxCount;
	if(msg!=null) {
	    this.msg = msg;
	}else {
	    this.msg = "字数不能超过 " + this.MAX_COUNT + "个汉字";
	}
    }
    
    /**
     * edittext 数字+字母
     */
    public void setLetterOrDigit() {
	filters.add(filterLetterOrDigit);

	InputFilter[] inputFilters = new InputFilter[filters.size()];
	filters.toArray(inputFilters);
	editTxt.setFilters(inputFilters); 
    }
    
    /**
     * edittext 中文+数字+字母
     */
    public void setChineseLetterDigit() {
	filters.add(filterChinese);
	InputFilter[] inputFilters = new InputFilter[filters.size()];
	filters.toArray(inputFilters);
	editTxt.setFilters(inputFilters); 
    }
    
    /**
     * 中文过滤器+数字+字母
     */
    InputFilter filterChinese = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, 
                        Spanned dest, int dstart, int dend) { 
                for (int i = start; i < end; i++) { 
                    char c = source.charAt(i);
                    if (!isChineseOnly(c)&&!Character.isLetterOrDigit(c)) { 
                            //Toast.makeText(context, "非法字符，禁止输入", Toast.LENGTH_SHORT).show();
                	return ""; 
                    } 
                } 
                return null; 
        }
        /**
         * 判断是否问中文字符（包括标点）
         * @param c
         * @return
         */
        private boolean isChinese(char c) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                return true;
            }
            return false;
        }
        /**
         * 判断是否问中文字符（不包括标点）
         * @param c
         * @return
         */
	private boolean isChineseOnly(char c) {
	    boolean isGB2312 = false;
	    byte[] bytes = ("" + c).getBytes();
	    if (bytes.length == 2) {
		int[] ints = new int[2];
		ints[0] = bytes[0] & 0xff;
		ints[1] = bytes[1] & 0xff;
		if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
			&& ints[1] <= 0xFE) {
		    isGB2312 = true;
		}
	    }
	    return isGB2312;
	}
    };
    /**
     * 字幕和数字
     */
    InputFilter filterLetterOrDigit = new InputFilter() { 
        @Override
        public CharSequence filter(CharSequence source, int start, int end, 
                        Spanned dest, int dstart, int dend) { 
                for (int i = start; i < end; i++) { 
                        if (!Character.isLetterOrDigit(source.charAt(i))) { 
                            Toast.makeText(context, "非法字符，禁止输入", Toast.LENGTH_SHORT).show();
                                return ""; 
                        } 
                } 
                return null; 
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

	private int editStart;

	private int editEnd;

	public void afterTextChanged(Editable s) {
	    editStart = editTxt.getSelectionStart();
	    editEnd = editTxt.getSelectionEnd();

	    // 先去掉监听器，否则会出现栈溢出
	    editTxt.removeTextChangedListener(textWatcher);

	    // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
	    // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
	    while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
		s.delete(editStart - 1, editEnd);
		editStart--;
		editEnd--;
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	    }
	    // mEditText.setText(s);将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了，多谢@ainiyidiandian的提醒
	    editTxt.setSelection(editStart);

	    // 恢复监听器
	    editTxt.addTextChangedListener(textWatcher);

	    setLeftCount();
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
		int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before,
		int count) {

	}
    };

    /**
     * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
     */
    private void setLeftCount() {
	if (textView != null) {
	    textView.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}

    }

    /**
     * 获取用户输入的分享内容字数
     * 
     * @return
     */
    private long getInputCount() {
	return calculateLength(editTxt.getText().toString());
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     * 
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c) {
	double len = 0;
	for (int i = 0; i < c.length(); i++) {
	    int tmp = (int) c.charAt(i);
	    if (tmp > 0 && tmp < 127) {
		len += 0.5;
	    } else {
		len++;
	    }
	}
	return Math.round(len);
    }
}
