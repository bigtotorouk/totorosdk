package com.totoro.freelancer.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

/**
 * 
 * @author bing
 * 
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
	private boolean flag = true;
	private String filename;
	private Context context;

	final String dir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/nvxiuwang/";

	public DownloadTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... sUrl) {
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		// PowerManager pm = (PowerManager)
		// context.getSystemService(Context.POWER_SERVICE);
		// PowerManager.WakeLock wl =
		// pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		// wl.acquire();
		String path = sUrl[0];
		filename = path.substring(path.lastIndexOf("/") + 1);
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		try {
			InputStream input = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
					return "Server returned HTTP "
							+ connection.getResponseCode() + " "
							+ connection.getResponseMessage();

				input = connection.getInputStream();

				// --------------------------------------
				byte[] data = readInputStream(input);
				// new一个文件对象用来保存图片，默认保存当前工程根目录
				// File imageFile = new
				// File(Environment.getExternalStorageDirectory() + filename);
				File imageFile = new File(dir + filename);
				// 创建输出流
				FileOutputStream outStream = new FileOutputStream(imageFile);
				// 写入数据
				outStream.write(data);
				// 关闭输出流
				outStream.close();

				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			} finally {
				try {
					if (input != null)
						input.close();
				} catch (IOException ignored) {

				}

				if (connection != null)
					connection.disconnect();
			}
		} finally {
			// wl.release();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (flag) {
			Toast.makeText(context,
					"下载 " + filename + " 成功!    路径：" + dir + filename,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "下载 " + filename + " 失败",
					Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}

	public byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
}