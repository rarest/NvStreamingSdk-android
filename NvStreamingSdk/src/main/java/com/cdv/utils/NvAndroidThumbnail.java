//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2014. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Apr 7. 2017
//   Author:        NewAuto video team
//================================================================================
package com.cdv.utils;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.meicam.sdk.NvsVideoStreamInfo;

import java.io.File;
import java.io.IOException;

public class NvAndroidThumbnail
{
    private static final String TAG = "NvAndroidThumbnail";

    public static Bitmap createThumbnail(Context context, String filePath, boolean isVideo, int expectedWidth, int expectedHeight) {
        if (context == null || filePath == null || filePath.isEmpty())
            return null;

        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Query android media store id from the file path
        // NOTE: We may fail to query media store id from the file path due to
        // the image or video file was not registered to the system.
        Cursor cursor;
        if (isVideo) {
            cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                            new String[]{BaseColumns._ID},
                                            MediaStore.MediaColumns.DATA + "=?",
                                            new String[]{filePath},
                                            null);
        } else {
            cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            new String[]{BaseColumns._ID},
                                            MediaStore.MediaColumns.DATA + "=?",
                                            new String[]{filePath},
                                            null);
        }

        long mediaStoreId = 0;
        if (cursor != null) {
            if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(BaseColumns._ID);
                if (columnIndex >= 0) {
                    mediaStoreId = cursor.getLong(columnIndex);
                    cursor.close();

                    // Query thumbnail image file path according to the media store id
                    // NOTE: It seems that some stupid android devices(such as Xiaomi MI 4LTE) will always
                    // generate a new thumbnail image file once we called android.provider.MediaStore.Video.Thumbnails.getThumbnail() or
                    // android.provider.MediaStore.Images.Thumbnails.getThumbnail() which is very slow operation.
                    // To avoid this, we should first check whether the thumbnail image already exists and use it when possible
                    if (isVideo) {
                        cursor = contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                                new String[]{MediaStore.Video.Thumbnails.DATA},
                                MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                                new String[]{String.valueOf(mediaStoreId)},
                                null);
                    } else {
                        cursor = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                                new String[]{MediaStore.Images.Thumbnails.DATA},
                                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                                new String[]{String.valueOf(mediaStoreId)},
                                null);
                    }

                    if (cursor != null) {
                        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                            if (isVideo)
                                columnIndex = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
                            else
                                columnIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                            if (columnIndex >= 0) {
                                String thumbnailFilePath = cursor.getString(columnIndex);
                                cursor.close();

                                if (!thumbnailFilePath.isEmpty()) {
                                    File file = new File(thumbnailFilePath);
                                    if (file.exists()) {
                                        int videoRotation = isVideo ? NvsVideoStreamInfo.VIDEO_ROTATION_0 : detectImageFileRotation(filePath);
                                        return createThumbnailFromThumbnailImageFile(thumbnailFilePath, expectedWidth, expectedHeight, videoRotation);
                                    }
                                }
                            } else
                                cursor.close();
                        } else
                            cursor.close();
                    }
                    // Get thumbnail bitmap from system cache(It will be blocked until the thumbnails are generated)
                    Bitmap bitmap;
                    if (isVideo)
                        bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, mediaStoreId, MediaStore.Video.Thumbnails.MINI_KIND, null);
                    else
                        bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, mediaStoreId, MediaStore.Images.Thumbnails.MINI_KIND, null);
                    if (bitmap == null) {
                        Log.e(TAG, String.format("Fail to get thumbnail file for media '%d'!", mediaStoreId));
                        return createThumbnailFromFile(filePath, isVideo, expectedWidth, expectedHeight);
                    }
                    int videoRotation = isVideo ? NvsVideoStreamInfo.VIDEO_ROTATION_0 : detectImageFileRotation(filePath);
                    return rotateBitmap(bitmap, videoRotation);
                } else
                    cursor.close();
            } else
                cursor.close();
        }
        Log.e(TAG, String.format("Failed to query media store id for '%s'!", filePath));
        return createThumbnailFromFile(filePath, isVideo, expectedWidth, expectedHeight);
    }

    private static int detectImageFileRotation(String filePath) {
        // Read EXIF tags to detect image rotation
        int orientation = ExifInterface.ORIENTATION_NORMAL;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            orientation = exifInterface.getAttributeInt("Orientation", ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (orientation) {
            default:
            case ExifInterface.ORIENTATION_NORMAL:
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
            case ExifInterface.ORIENTATION_UNDEFINED:
                return NvsVideoStreamInfo.VIDEO_ROTATION_0;

            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_TRANSPOSE:
                return NvsVideoStreamInfo.VIDEO_ROTATION_90;

            case ExifInterface.ORIENTATION_ROTATE_180:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return NvsVideoStreamInfo.VIDEO_ROTATION_180;

            case ExifInterface.ORIENTATION_ROTATE_270:
            case ExifInterface.ORIENTATION_TRANSVERSE:
                return NvsVideoStreamInfo.VIDEO_ROTATION_270;
        }
    }

    private static Bitmap createThumbnailFromThumbnailImageFile(String thumbnailFilePath, int expectedWidth, int expectedHeight, int videoRotation) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(thumbnailFilePath, options);

        options.inSampleSize = 1;
        if (options.outWidth > expectedWidth && options.outHeight > expectedHeight) {
            int widthRadio = Math.round(options.outWidth * 1.0f / expectedWidth);
            int heightRadio = Math.round(options.outHeight * 1.0f / expectedHeight);
            options.inSampleSize = Math.max(widthRadio, heightRadio);
        }
        options.inJustDecodeBounds = false;
        return rotateBitmap(BitmapFactory.decodeFile(thumbnailFilePath, options), videoRotation);
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int videoRotation) {
        if (videoRotation == NvsVideoStreamInfo.VIDEO_ROTATION_0)
            return bitmap;

        Matrix matrix = new Matrix();
        if (videoRotation == NvsVideoStreamInfo.VIDEO_ROTATION_90)
            matrix.postRotate(90);
        else if (videoRotation == NvsVideoStreamInfo.VIDEO_ROTATION_180)
            matrix.postRotate(180);
        else if (videoRotation == NvsVideoStreamInfo.VIDEO_ROTATION_270)
            matrix.postRotate(270);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap createThumbnailFromFile(String filePath, boolean isVideo, int expectedWidth, int expectedHeight) {
        if (!isVideo) {
            // Read image file to extract thumbnail image
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            options.inSampleSize = 1;
            if (options.outWidth > expectedWidth && options.outHeight > expectedHeight) {
                int widthRadio = Math.round(options.outWidth * 1.0f / expectedWidth);
                int heightRadio = Math.round(options.outHeight * 1.0f / expectedHeight);
                options.inSampleSize = Math.max(widthRadio, heightRadio);
            }
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }

        //
        // Generate thumbnail bitmap for video using ThumbnailUtils
        //
        Bitmap bitmap = android.media.ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        if (bitmap == null)
            Log.e(TAG, String.format("Failed to create video thumbnail bitmap for '%s'!", filePath));
        return bitmap;
    }
}
