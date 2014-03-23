package com.totoro.freelancer.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class DateUtil {
	
	static String LOCK_FILE = "lock-file";
	static String LOCK_DATE = "lock-date";
	/**
	 * ���ò��жϸ��������Ȩ����
	 * @param context
	 * @param dateSize
	 * @return
	 */
	public static boolean isExpired(Context context, int dateSize){
		SharedPreferences setting = context.getSharedPreferences(LOCK_FILE, 0);
		long _date = setting.getLong(LOCK_DATE, -1L);
		if(_date < 0){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.add(Calendar.DAY_OF_YEAR, dateSize);
			setting.edit().putLong(LOCK_DATE, cal.getTimeInMillis()).commit();
			return false;
		}else{
			if((_date > System.currentTimeMillis())){
				return false;
			}else{
				return true;
			}
		}
	}
	
	/**
	 * 
	 * @param dt
	 * @return ���ڼ�
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (index < 0) {
			index = 0;
		}
		return weekDays[index];
	}

	/** ��ȡ��ǰʱ�� */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrenDateTime() {
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sm.format(new Date());
		String[] splitDate = dateStr.split(" ");
		String string = splitDate[1];
		return string;
	}

}
