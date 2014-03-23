package com.totoro.freelancer.sdk.util;

public class VerifyUtil {

	/**
	 * 验证密码格式 —— 密码长度限制6-18 只能为A-Za-z0-9
	 * 
	 * @param str
	 * @return
	 */
	public static String verifyPasswrod(String str) {
		String psd = str.trim();
		if (psd.length() < 6 || psd.length() > 14) {
			return "密码输入长度不合规范，请输入6-14位长度的字符";
		} else {
			// 判断是否含有中文字符
			char[] c = psd.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if ((c[i] >= 0x4e00) && (c[i] <= 0x9fbb)) {
					return "密码格式不正确，请输入字母或数字";
				}
			}
		}
		if (!psd.matches("[A-Za-z0-9]{6,14}")) {
			return "密码格式不正确";
		}
		return "";
	}

}
