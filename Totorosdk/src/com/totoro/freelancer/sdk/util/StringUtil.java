package com.totoro.freelancer.sdk.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.EditText;

public class StringUtil {
	
	
	/**
	 * 判断字符串是否为空或null
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmptyOrNull(String string) {
		if (string == null) {
			return true;
		}
		if (string.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmptyOrNull(EditText editText){
		return isEmptyOrNull(editText.getText().toString());
	}
	
	
	/**
	 * 验证手机号码 add by slider
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();

	}

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		if (email.contains(";")) {
			String[] emails = email.split(";");
			if (emails != null && emails.length > 0) {
				for (String e : emails) {
					if (!checkSingleEmail(e)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return checkSingleEmail(email);
		}
	}

	public static boolean checkSingleEmail(String email) {
		boolean flag = false;
		// <xiaowangzaixian@gmail.com> match
		if (email.contains("<") && email.contains(">")
				&& (email.lastIndexOf("<") < email.lastIndexOf(">"))) {
			String str = email.substring(email.lastIndexOf("<"),
					email.lastIndexOf(">"));
			email = str.substring(1, str.length());
		}
		try {
			String check = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";//
			// String check =
			// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	/**
	 * 将map转换成url
	 * 
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		return sb.toString();
	}
}
