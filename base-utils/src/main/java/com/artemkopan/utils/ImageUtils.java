package com.artemkopan.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ImageUtils {

    @WorkerThread
    public static void saveBitmapToJPEG(Bitmap bmp, String filePath,
                                        @IntRange(from = 0, to = 100) int quality) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, out); // bmp is your Bitmap instance
        out.close();
    }

    @Nullable
    @WorkerThread
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static String convertBitmapToBase64(final Bitmap bitmap,
                                               final Bitmap.CompressFormat format,
                                               @IntRange(from = 0, to = 100) final int quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String image = null;

        try {
            bitmap.compress(format, quality, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            image = "data:image/jpeg;base64," + Base64.encodeToString(b, Base64.DEFAULT);
        } finally {
            baos.close();
            if (!bitmap.isRecycled()) bitmap.recycle();
        }
        return image;
    }

    @WorkerThread
    public static Bitmap openBitmapFromFile(final String imagePath, final Bitmap.Config config) throws IOException {
        if (TextUtils.isEmpty(imagePath)) {
            throw new IllegalArgumentException("ImagePath is null or empty : " + imagePath);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        if (bitmap == null) {
            throw new IOException("Can't load this bitmap");
        }
        return bitmap;
    }

    @WorkerThread
    public static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            return recycleSource(image, Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true));
        } else {
            return image;
        }
    }

    @WorkerThread
    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        int orientation = getExifOrientation(src);

        if (orientation == 1) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        switch (orientation) {
            case 2:
                matrix.setScale(-1, 1);
                break;
            case 3:
                matrix.setRotate(180);
                break;
            case 4:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case 5:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case 6:
                matrix.setRotate(90);
                break;
            case 7:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case 8:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return recycleSource(bitmap, oriented);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private static int getExifOrientation(String src) {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(String.class);
                Object exifInstance = exifConstructor.newInstance(src);
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", String.class,
                                                             int.class);
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, tagOrientation, 1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return orientation;
    }

    private static Bitmap recycleSource(Bitmap src, Bitmap dest) {
        if (src != dest && !src.isRecycled()) {
            src.recycle();
        }
        return dest;
    }

}
