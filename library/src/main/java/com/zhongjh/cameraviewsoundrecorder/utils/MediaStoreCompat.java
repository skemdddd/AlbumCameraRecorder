package com.zhongjh.cameraviewsoundrecorder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;

import com.zhongjh.cameraviewsoundrecorder.settings.CaptureStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 有关多媒体的文件操作
 * Created by zhongjh on 2018/8/23.
 */
public class MediaStoreCompat {

    private final WeakReference<Context> mContext;

    private CaptureStrategy mCaptureStrategy;           // 设置目录

    public MediaStoreCompat(Context context) {
        mContext = new WeakReference<>(context);
    }

    /**
     * 检查设备是否具有相机特性。
     *
     * @param context 检查相机特征的上下文。
     * @return 如果设备具有相机特性，则为真。否则为假。
     */
    public static boolean hasCameraFeature(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 设置目录
     */
    public void setCaptureStrategy(CaptureStrategy strategy) {
        mCaptureStrategy = strategy;
    }

    /**
     * 创建文件
     *
     * @param type 0是图片 1是视频 2是音频
     * @return 临时文件
     * @throws IOException
     */
    private File createFile(int type) throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = null;
        switch (type) {
            case 0:
                fileName = String.format("JPEG_%s.jpg", timeStamp);
                break;
            case 1:
                fileName = String.format("VIDEO_%s.mp4", timeStamp);
                break;
        }
        File storageDir;
        if (mCaptureStrategy.isPublic) {
            storageDir = Environment.getExternalStoragePublicDirectory(
                    mCaptureStrategy.directory);
            if (!storageDir.exists()) storageDir.mkdirs();
        } else {
            storageDir = mContext.get().getExternalFilesDir(mCaptureStrategy.directory);
        }

        // Avoid joining path components manually
        File tempFile = new File(storageDir, fileName);

        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }

        return tempFile;

    }

    /**
     * 保存bitmap到file
     *
     * @return 返回file的路径
     */
    public String saveFileByBitmap(Bitmap bitmap) {
        File file = null;
        try {
            file = createFile(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert file != null;
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }


}
