package com.totoro.freelancer.sdk.util;

import java.io.File;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownloadUtil {
    
    /**
     * 
     * @param directory
     *            exmaple "example/" , it actually downloaded to directory call
     *            "/sdcard/example/"
     * @param url
     *            http download site
     */
    public static boolean download(Context context, String directory, String url) {
	/* section for download module */
		File dir = new File(Environment.getExternalStorageDirectory().getPath()+directory);
		if(!dir.exists()||!dir.isDirectory()) {
		    dir.mkdirs();
		}
		String filename = url.substring(url.lastIndexOf("/") + 1);
		DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		//String trueUrl = WisdomCityRestClientParameterImpl.getUrl() + url;
		String trueUrl = url;
		Request request = new Request(Uri.parse(trueUrl));
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
				| DownloadManager.Request.NETWORK_MOBILE)
			.setAllowedOverRoaming(false).setTitle("Demo")
			.setDescription("Something useful. No, really.")
			.setDestinationInExternalPublicDir(directory, filename);
	
		long enqueue = dm.enqueue(request);
	/*
	 * show the view screen about DownloadManager.ACTION_VIEW_DOWNLOADS�?it
	 * will dismiss when the download is complete
	 */
	/*
	 * Intent i = new Intent();
	 * i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS); 
	 * context.startActivity(i);
	 */
		return true;
    }
    
//  //接受下载完成后的intent  
//    class DownloadCompleteReceiver extends BroadcastReceiver {  
//        @Override  
//        public void onReceive(Context context, Intent intent) {  
//            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){  
//                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);  
////                Log.v(TAG," download complete! id : "+downId);  
//                Toast.makeText(context, intent.getAction()+"id : "+downId, Toast.LENGTH_SHORT).show();  
//            }  
//        }  
//    }  
}
