package com.totoro.freelancer.sdk.util;

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
	try {
	    InputStream input = null;
	    OutputStream output = null;
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

		// this will be useful to display download percentage
		// might be -1: server did not report the length
		int fileLength = connection.getContentLength();

		// download the file
		input = connection.getInputStream();
		output = new FileOutputStream(Environment.getExternalStorageDirectory() + filename);
		byte data[] = new byte[4096];
		long total = 0;
		int count;
		while ((count = input.read(data)) != -1) {
		    // allow canceling with back button
		    if (isCancelled())
			return null;
		    total += count;
		    // publishing the progress....
		    if (fileLength > 0) // only if total length is known
			publishProgress((int) (total * 100 / fileLength));
		    output.write(data, 0, count);
		}
		flag = true;
	    } catch (Exception e) {
		return e.toString();
	    } finally {
		try {
		    if (output != null)
			output.close();
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
        if(flag) {
            Toast.makeText(context, "下载 "+filename+" 成功!    路径地址" + Environment.getExternalStorageDirectory() + "/" + filename, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "下载 "+filename+" 失败", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(result);
    }
}