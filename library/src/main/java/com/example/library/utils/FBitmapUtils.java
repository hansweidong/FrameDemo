package com.example.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bjhl on 16/6/27.
 *
 * 图片
 */
public class FBitmapUtils {

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        if (baos != null) {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 旋转bitmap, 生成一个新的bitmap
     * @param source	将要被旋转的bitmap
     * @param rotateDegree 旋转角度
     * @param config	Bitmap.Config
     * @return a new Bitmap
     */
    public static Bitmap rotateBitmap(Bitmap source, float rotateDegree, Bitmap.Config config) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        Matrix m = new Matrix();
        m.postRotate(rotateDegree, sourceWidth >> 1, sourceHeight >> 1);

        Bitmap targetBitmap = Bitmap.createBitmap(source, 0, 0, sourceWidth, sourceHeight, m, true);
        return targetBitmap;
    }

    public static int computeFineSampleSize(int rawWidth, int rawHeight, int targetWidth, int targetHeight) {
        int sampleSize = 1;

        int sampleWidth = 1;
        int sampleHeight = 1;
        if (rawWidth > targetWidth) {
            sampleWidth = (int)Math.ceil((double)rawWidth/targetWidth);
        }

        if (rawHeight > targetHeight) {
            sampleHeight = (int)Math.ceil((double)rawHeight/targetHeight);
        }

        sampleSize = Math.max(sampleHeight, sampleWidth);
//		sampleSize = Math.min(sampleWidth, sampleHeight);
//		Log.v("bitmap", sampleHeight + " " + sampleWidth + " " + sampleSize);
        return (int)Math.pow(2d, Math.floor(Math.log(sampleSize)/Math.log(2d)));
    }

    /**
     * @see {@link #decodeFile(String, int, int, boolean)}
     * @param filePath
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeFile(String filePath, int targetWidth, int targetHeight) {
        return decodeFile(filePath, targetWidth, targetHeight, true);
    }

    /**
     * 以合适的大小decode图片文件
     * @param filePath     	图片路径
     * @param targetWidth	最大宽度
     * @param targetHeight	最大高度
     * @param shouldCorrectRotation 是否需要纠正exif中的rotate信息
     * @return
     */
    public static Bitmap decodeFile(String filePath, int targetWidth, int targetHeight, boolean shouldCorrectRotation) {
        if (targetWidth <=0 || targetHeight <= 0 || TextUtils.isEmpty(filePath)) {
            return null;
        }

        Bitmap bm = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        int rawWidth = opts.outWidth;
        int rawHeight = opts.outHeight;

        if (rawHeight <=0 || rawWidth <= 0) {
            Log.e("FBitmapUtils", "decoded out bounds is (0, 0)");
            return null;
        }

        opts.inSampleSize = computeFineSampleSize(rawWidth, rawHeight, targetWidth, targetHeight);
//		Log.v("bitmap", "in decodefile rawWidth = " + rawWidth + ", rawHeight = " + rawHeight + ", insamplesize = " + opts.inSampleSize);
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        bm = BitmapFactory.decodeFile(filePath, opts);
        if (shouldCorrectRotation && bm != null) {
            try {
                ExifInterface exif = new ExifInterface(filePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Bitmap rotatedBitmap = null;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = FBitmapUtils.rotateBitmap(bm, 90, Bitmap.Config.RGB_565);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = FBitmapUtils.rotateBitmap(bm, 180, Bitmap.Config.RGB_565);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = FBitmapUtils.rotateBitmap(bm, 270, Bitmap.Config.RGB_565);
                        break;
                    default:
                        break;
                }

                if (rotatedBitmap != null) {
                    bm.recycle();
                    bm = rotatedBitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// end rotate

        return bm;
    }

    /**
     * 经测试在某些情况下decode图片可能返回null
     * @param is
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeStream(final InputStream is, int targetWidth, int targetHeight) {
        if (targetWidth <=0 || targetHeight <= 0 || is == null) {
            return null;
        }

        final BufferedInputStream bis = new BufferedInputStream(is);

        try {
            bis.mark(bis.available());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bis, null, opts);

        int rawWidth = opts.outWidth;
        int rawHeight = opts.outHeight;

        if (rawHeight <=0 || rawWidth <= 0) {
            return null;
        }

        opts.inSampleSize = computeFineSampleSize(rawWidth, rawHeight, targetWidth, targetHeight);
//		opts.inSampleSize = sampleSize;

        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        try {
            bis.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = BitmapFactory.decodeStream(bis, null, opts);

        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, int targetWidth, int targetHeight) {
        if (targetWidth <=0 || targetHeight <= 0 || data == null) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, opts);

        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;
        if (outHeight <= 0 || outWidth <= 0) {
            return null;
        }
        opts.inSampleSize = FBitmapUtils.computeFineSampleSize(outWidth, outHeight, targetWidth, targetHeight);
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length, opts);

        return bitmap;
    }


}