package com.emergency.app.util.imagehelper;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.emergency.app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {
    public static File getResizedFile(Activity activity,String path, String fileName) {
        File dir = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)), activity.getResources().getString(R.string.app_name));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File objFile = new File(dir, fileName + ".jpg");
        try {
            int inWidth = 0;
            int inHeight = 0;
            InputStream objInputStream = new FileInputStream(path);
            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options objOptions = new BitmapFactory.Options();
            objOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(objInputStream, null, objOptions);
            objInputStream.close();
            objInputStream = null;
            // save width and height
            inWidth = objOptions.outWidth;
            inHeight = objOptions.outHeight;

            // decode full image pre-resized
            objInputStream = new FileInputStream(path);
            objOptions = new BitmapFactory.Options();
            int dstWidth = 500;
            int dstHeight = 500;
            // calc rought re-size (this is no exact resize)
            objOptions.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(objInputStream, null, objOptions);
            // calc exact destination size
            Matrix objMatrix = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            objMatrix.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            objMatrix.getValues(values);
            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            // save image
            try {
                FileOutputStream objFileOutputStream = new FileOutputStream(objFile);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, objFileOutputStream);
                objFileOutputStream.flush();
                objFileOutputStream.close();
                resizedBitmap.recycle();
            } catch (Exception e) {
                Log.e("Image", e.getMessage(), e);
            }
        } catch (IOException e) {
            Log.e("Image", e.getMessage(), e);
        }
        return objFile;
    }
    public static String getRealPathFromURI(Activity activity,Uri uri) {
        Cursor objCursor = activity.getContentResolver().query(uri, null, null, null, null);
        objCursor.moveToFirst();
        String document_id = objCursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        objCursor.close();
        objCursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        objCursor.moveToFirst();
        String path = objCursor.getString(objCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        objCursor.close();
        return path;
    }
}
