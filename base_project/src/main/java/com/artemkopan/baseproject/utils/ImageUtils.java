package com.artemkopan.baseproject.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.Base64;

import com.artemkopan.baseproject.rx.BaseRx;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;


public class ImageUtils {


    public static void saveBitmapToJPEG(Bitmap bmp, String filePath,
            @IntRange(from = 0, to = 100) int quality) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, out); // bmp is your Bitmap instance
        out.close();
    }

    public static Observable<String> convertBitmapToBase64(final String path,
            Bitmap.Config config, final Bitmap.CompressFormat format,
            @IntRange(from = 0, to = 100) final int quality, final int width, final int height) {

        return openBitmapFromFile(path, config)
                .flatMap(new Function<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Bitmap bitmap) throws Exception {
                        ExtraUtils.checkBackgroundThread();
                        return convertBitmapToBase64(resizeBitmap(rotateBitmap(path, bitmap), width, height),
                                format, quality);
                    }
                });
    }

    public static Observable<String> convertBitmapToBase64(final Bitmap bitmap,
            final Bitmap.CompressFormat format,
            @IntRange(from = 0, to = 100) final int quality) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(format, quality, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                baos.close();
                bitmap.recycle();
                e.onNext("data:image/jpeg;base64," + Base64.encodeToString(b, Base64.DEFAULT));
                e.onComplete();
            }
        });
    }


    public static Observable<Bitmap> openBitmapFromFile(final String imagePath,
            final Bitmap.Config config) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                if (TextUtils.isEmpty(imagePath)) {
                    e.onError(
                            new IllegalArgumentException(
                                    "ImagePath is null or empty : " + imagePath));
                    return;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = config;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                if (bitmap == null) {
                    e.onError(new IOException("Can't load this bitmap"));
                    return;
                }

                e.onNext(bitmap);
                e.onComplete();
            }
        });
    }

    public static Observable<Bitmap> resizeBitmapObservable(final Bitmap image,
            final int maxWidth,
            final int maxHeight) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> subscriber) throws Exception {
                subscriber.onNext(resizeBitmap(image, maxWidth, maxHeight));
                subscriber.onComplete();
            }
        }).compose(BaseRx.<Bitmap>applySchedulers());
    }


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
            Bitmap mImage = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            image.recycle();
            return mImage;
        } else {
            return image;
        }
    }

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
            Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return oriented;
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

}
