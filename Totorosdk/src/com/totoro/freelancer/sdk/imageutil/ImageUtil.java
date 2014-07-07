package com.totoro.freelancer.sdk.imageutil;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import static android.graphics.BitmapFactory.Options;

/**
 * Created by bing on 2014/7/4.
 */
public class ImageUtil {
    private static String TAG = "ImageUtil";

    public void bindImageViewWith(ImageView imageView, String path) {

    }

    /**
     * requests the decoder to subsample the original image, returning a smaller image to save memory.
     * it could receive too large image. and set default value of width,height with 100,100
     *
     * @param filePath
     * @return
     */
    public static Bitmap getImageLocal(String filePath) {
        return getImageLocal(filePath, BitmapUtil.REQUEST_WIDTH, BitmapUtil.REQUEST_HEIGHT);
    }

    public static Bitmap getImageLocal(String filePath, int reqWidth, int reqHeight) {
        if (reqWidth == -1 || reqHeight == -1) { // no subsample and no
            return BitmapFactory.decodeFile(filePath);
        } else {
            // First decode with inJustDecodeBounds=true to check dimensions
            final Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
            options.inSampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight);
            Log.d(TAG, "options inSampleSize " + options.inSampleSize);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }
    }

    /**
     * Purpose: calculate option for a bitmap, now define default value of width,height with 100,100
     * Author: Slider Xiao
     * Created Time: Mar 27, 2013 2:57:36 PM
     * Update By: Slider Xiao, Mar 27, 2013 2:57:36 PM
     */
    static class BitmapUtil {
        public static final int REQUEST_WIDTH = 80;
        public static final int REQUEST_HEIGHT = 80;

        public static int calculateInSampleSize(Options options) {
            return calculateInSampleSize(options, REQUEST_WIDTH, REQUEST_HEIGHT);
        }

        public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {

            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                // Calculate ratios of height and width to requested height and
                // width
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);

                // Choose the smallest ratio as inSampleSize value, this will
                // guarantee
                // a final image with both dimensions larger than or equal to
                // the
                // requested height and width.
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            return inSampleSize;
        }
    }

    public String getAbsoluteImagePath(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = cr.query(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            // 如果游标为空说明获取的已经是绝对路径了
            return uri.getPath();
        }
    }
}
