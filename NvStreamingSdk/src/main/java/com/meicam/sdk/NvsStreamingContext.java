//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Feb 28. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsFxDescription;
import com.meicam.sdk.NvsAVFileInfo;
import org.qtproject.qt5.android.QtNative;

import android.Manifest;
import android.os.Build;
import android.os.IBinder;
import android.content.Context;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.Surface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.Math;
import java.lang.reflect.Method;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/*!
    \brief 流媒体上下文

    流媒体上下文类可视作整个SDK框架的入口。开发过程中，NvsStreamingContext提供了静态sharedInstance接口创建流媒体上下文的唯一实例。
    通过这个实例对象，我们可以开启采集设备录制视频，添加采集视频特效，设置拍摄时的各项参数，包括自动聚焦，自动曝光调节，
    开关换补光灯等。同时还能够创建时间线，并将时间线与实时预览窗口连接起来，
    实时预览播放已经拍摄完成的视频。整个视频制作完成后，要销毁流媒体上下文的对象实例。
*/

public class NvsStreamingContext
{
/*! \anchor STREAMING_ENGINE_STATE */
/*! @name 流媒体引擎状态 */
//!@{
    public static final int STREAMING_ENGINE_STATE_STOPPED = 0;          //!< \if ENGLISH \else 引擎停止 \endif
    public static final int STREAMING_ENGINE_STATE_CAPTUREPREVIEW = 1;   //!< \if ENGLISH \else 采集预览 \endif
    public static final int STREAMING_ENGINE_STATE_CAPTURERECORDING = 2; //!< \if ENGLISH \else 拍摄录制 \endif
    public static final int STREAMING_ENGINE_STATE_PLAYBACK = 3;         //!< \if ENGLISH \else 播放 \endif
    public static final int STREAMING_ENGINE_STATE_SEEKING = 4;          //!< \if ENGLISH \else 定位 \endif
    public static final int STREAMING_ENGINE_STATE_COMPILE = 5;          //!< \if ENGLISH \else 生成文件 \endif

//!@}

/*! \anchor VIDEO_CAPTURE_RESOLUTION_GRADE */
/*! @name 视频采集分辨率级别 */
//!@{
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_LOW = 0;     //!< \if ENGLISH \else 视频采集低分辨率 \endif
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_MEDIUM = 1;  //!< \if ENGLISH \else 视频采集中等分辨率 \endif
    public static final int VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH = 2;    //!< \if ENGLISH \else 视频采集高分辨率 \endif

//!@}

/*! \anchor COMPILE_VIDEO_RESOLUTION_GRADE */
/*! @name 生成视频的分辨率高度级别 */
//!@{
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_360 = 0;     //!< \if ENGLISH \else 生成视频高度-360p(宽度由视频高度以及创建时间线(createTimeline)时指定的视频宽高以及像素比算出)\endif
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_480 = 1;     //!< \if ENGLISH \else 生成视频高度-480p \endif
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_720 = 2;     //!< \if ENGLISH \else 生成视频高度-720p \endif
    public static final int COMPILE_VIDEO_RESOLUTION_GRADE_1080 = 3;    //!< \if ENGLISH \else 生成视频高度-1080p \endif

//!@}

/*! \anchor COMPILE_BITRATE_GRADE */
/*! @name 生成视频的码率级别 */
//!@{
    public static final int COMPILE_BITRATE_GRADE_LOW = 0;              //!< \if ENGLISH \else 生成视频低码率 \endif
    public static final int COMPILE_BITRATE_GRADE_MEDIUM = 1;           //!< \if ENGLISH \else 生成视频中等码率 \endif
    public static final int COMPILE_BITRATE_GRADE_HIGH = 2;             //!< \if ENGLISH \else 生成视频高码率 \endif

//!@}

/*! \anchor CAPTURE_DEVICE_ERROR */
/*! @name 采集设备错误标识 */
//!@{
    public static final int CAPTURE_DEVICE_ERROR_UNKNOWN = 1;           //!< \if ENGLISH \else 采集设备错误未知 \endif
    public static final int CAPTURE_DEVICE_ERROR_SERVER_DIED = 2;       //!< \if ENGLISH \else 采集设备错误服务器崩溃 \endif

//!@}


/*! \anchor VIDEO_PREVIEW_SIZEMODE */
/*! @name 视频大小预览模式 */
//!@{
    public static final int VIDEO_PREVIEW_SIZEMODE_FULLSIZE = 0;        //!< \if ENGLISH \else 视频全屏预览模式 \endif
    public static final int VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE = 1; //!< \if ENGLISH \else 视频livewindow大小预览模式 \endif

//!@}

/*! \anchor STREAMING_ENGINE_CAPTURE_FLAG */
/*! @name 采集标志 */
//!@{
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_GRAB_CAPTURED_VIDEO_FRAME = 1;    //!< \if ENGLISH \else 获取采集视频的帧内容（打开这个标志会降低性能，只有在必要的时候开启这个标志） \endif
    public static final int STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER = 4;     //!< \if ENGLISH \else 不使用系统自带的recorder进行录制 \endif

//!@}

/*! \anchor STREAMING_ENGINE_SEEK_FLAG */
/*! @name 引擎定位标识 */
//!@{
    public static final int STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER = 2;         //!< \if ENGLISH \else 整体展示字幕效果 \endif
    public static final int STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER = 4;//!< \if ENGLISH \else 整体展示动画贴纸效果 \endif

//!@}

/*! \anchor STREAMING_ENGINE_COMPILE_FLAG */
/*! @name 引擎生成文件标志 */
//!@{
    public static final int STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER = 1; //!< \if ENGLISH \else 禁用硬件编码器 \endif

//!@}

    private static NvsStreamingContext m_instance = null;
    private static Activity m_activity = null;
    private static boolean m_initializedOnce = false;
    private static final String TAG = "Meicam";

    // The qt plugins we need to deploy to sandbox
    private static String[] m_qtPluginsInfo = {
        "libqtforandroid.so", "platforms/android/libqtforandroid.so",
        "libqjpeg.so", "imageformats/libqjpeg.so",
        "libqtaudio_opensles.so", "audio/libqtaudio_opensles.so"};
    // The qt plugins we need to load explicitely
    private static String[] m_qtPluginsToLoad = {"platforms/android/libqtforandroid.so"};
    private static String m_nativeLibraryDirPath;
    private static String m_qtReservedFilesDirPath;
    private static String m_qtPluginsDirPath;
    private static String m_qtEnvVars = "QT_BLOCK_EVENT_LOOPS_WHEN_SUSPENDED=0";

    private NvsAssetPackageManager m_assetPackageManager = null;
    private CaptureDeviceCallback m_catpureDeviceCallback;
    private PlaybackCallback m_playbackCallback;
    private CompileCallback m_compileCallback;
    private StreamingEngineCallback m_streamingEngineCallback;
    private TimelineTimestampCallback m_timelineTimestampCallback;
/*!
    \brief 对流媒体上下文的单例实例进行初始化
    \param mainActivity 主工作流
    \param sdkLicenseFilePath 指定SDK license文件的路径
    \return 返回流媒体上下文的单例实例对象
    \sa getInstance
    \sa close
*/
    public static NvsStreamingContext init(Activity mainActivity, String sdkLicenseFilePath)
    {
        if (m_instance != null)
            return m_instance;

        ApplicationInfo ai = mainActivity.getApplicationInfo();
        m_nativeLibraryDirPath = ai.nativeLibraryDir + "/";
        m_qtReservedFilesDirPath = ai.dataDir + "/qt-reserved-files/";
        m_qtPluginsDirPath = m_qtReservedFilesDirPath + "plugins/";

        File mainLibFile = new File(m_nativeLibraryDirPath + "libNvStreamingSdkCore.so");
        if (!mainLibFile.exists()) {
            Log.e(TAG, "libNvStreamingSdkCore.so is missing!");
            return null;
        }

        // Construct environment variables string used by Qt Android
        m_qtEnvVars += "\tQT_PLUGIN_PATH=" + m_qtReservedFilesDirPath + "plugins";
        m_qtEnvVars += "\tQT_ANDROID_FONTS=Roboto;Droid Sans;Droid Sans Fallback";
        m_qtEnvVars += "\tQT_ANDROID_FONTS_MONOSPACE=Droid Sans Mono;Droid Sans;Droid Sans Fallback";
        m_qtEnvVars += "\tQT_ANDROID_FONTS_SERIF=Droid Serif";
        m_qtEnvVars += "\tHOME=" + mainActivity.getFilesDir().getAbsolutePath();
        m_qtEnvVars += "\tTMPDIR=" + mainActivity.getFilesDir().getAbsolutePath();

        String qtAppArgs = null;
        Intent intent = mainActivity.getIntent();
        if (intent != null)
            qtAppArgs = intent.getStringExtra("applicationArguments");
        if (qtAppArgs != null) {
            qtAppArgs = qtAppArgs.replace(' ', '\t').trim();
            if (qtAppArgs.length() > 0 && !qtAppArgs.startsWith("\t"))
                qtAppArgs = "\t" + qtAppArgs;
        } else {
            qtAppArgs = "";
        }
        qtAppArgs = mainLibFile.getAbsolutePath() + qtAppArgs;

        try {
            extractQtPlugins(mainActivity);

            // Set QtNative.m_classLoader and QtNative.m_activity, these fields are needed in:
            // QtAndroidPrivate::initJNI(), src\corelib\kernel\qjnihelpers.cpp
            // Probably JNI_OnLoad(), src\plugins\platforms\android\androidjnimain.cpp
            // ...
            QtNative.setClassLoader(mainActivity.getClassLoader());
            QtNative.setActivity(mainActivity, null);

            if (!m_initializedOnce) {
                // Load qt libraries
                // NOTE: It seems that if we don't load qt library explicitly, its JNI_OnLoad() will not be called!
                System.loadLibrary("gnustl_shared");
                System.loadLibrary("Qt5Core");
                System.loadLibrary("Qt5Gui");
                System.loadLibrary("Qt5AndroidExtras");
                System.loadLibrary("Qt5Network");
                System.loadLibrary("Qt5Multimedia");

                // Load qt plugins (if needed)
                for (String pluginName : m_qtPluginsToLoad) {
                    String pluginFilePath = m_qtPluginsDirPath + pluginName;
                    System.load(pluginFilePath);
                }
            } else {
                // NOTE: This app's activity has been recreated, in this case we must call
                // QtNative.setNativeActivity() to udpate the Activity object stored in qjnihelpers.cpp,
                // Otherwise, QtAndroid::androidActivity() will return a wrong Activity object
                // NOTE: androidjnimain.cpp will also hold a reference to the Activity object
                // once its JNI_OnLoad() get called, for now I can't find a way to update this
                // reference, but fortunately it is a local variable and not used anywhere
                // FIXME: Find a way to update g_jClassLoader stored in qjnihelpers.cpp
                try {
                    Method m = QtNative.class.getDeclaredMethod("setNativeActivity", Activity.class);
                    m.setAccessible(true);
                    m.invoke(null, mainActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "" + e.getMessage());
                }
            }

            // We must set QtNative.m_activity to null to prevent further function call such as QtNative.initializeAccessibility()
            // and QtNative.registerClipboardManager(). The latter will freeze android main thread!
            QtNative.setActivity(null, null);

            // Load main sdk native library
            if (!m_initializedOnce)
                System.loadLibrary("NvStreamingSdkCore");

            if (!QtNative.startQtAndroidPlugin())
                return null;

            //
            // Set display metrics for Qt
            //
            Display display = mainActivity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            Point screenRealSize = new Point(metrics.widthPixels, metrics.heightPixels);
            if (Build.VERSION.SDK_INT >= 17)
                display.getRealSize(screenRealSize);

            // Fix buggy dpi report
            final float xdpi = Math.max(metrics.xdpi, android.util.DisplayMetrics.DENSITY_LOW);
            final float ydpi = Math.max(metrics.ydpi, android.util.DisplayMetrics.DENSITY_LOW);
            QtNative.setDisplayMetrics(
                    metrics.widthPixels, metrics.heightPixels,
                    screenRealSize.x, screenRealSize.y,
                    xdpi, ydpi, metrics.scaledDensity, metrics.density);

            // We must set QtNative.m_service to a non-null value to prevent QtNative.registerClipboardManager() being called
            // That will freeze android main thread!
            QtNative.setService(new Service() {
                @Override
                public IBinder onBind(Intent intent)
                {
                    return null;
                }
            }, null);

            if (!m_initializedOnce)
                nativeVerifySdkLicenseFile(mainActivity, sdkLicenseFilePath);

            if (!nativeInit(qtAppArgs, m_qtEnvVars))
                return null;

            QtNative.setService(null, null);

            m_instance = new NvsStreamingContext(mainActivity);
            m_activity = mainActivity;
            m_initializedOnce = true;
            return m_instance;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
/*!
 *  \brief 销毁流媒体上下文实例。注意: 销毁之后可以再次创建及获取
    \sa init
    \sa getInstance

 */
    public static void close()
    {
        if (m_instance == null)
            return;

        NvsAssetPackageManager assetPackageManager = m_instance.getAssetPackageManager();
        if (assetPackageManager != null)
            assetPackageManager.setCallbackInterface(null);

        m_instance.setCaptureDeviceCallback(null);
        m_instance.setPlaybackCallback(null);
        m_instance.setCompileCallback(null);
        m_instance.setStreamingEngineCallback(null);
        m_instance.setTimelineTimestampCallback(null);
        m_instance = null;
        m_activity = null;

        nativeClose();

        QtNative.quitQtAndroidPlugin();
        QtNative.setActivity(null, null);
        QtNative.setClassLoader(null);
    }
/*!
    \brief 获取流媒体上下文的单例实例(必须进行初始化后才能使用)
    \return 返回流媒体上下文的单例实例对象
    \sa init
    \sa close
*/
    public static NvsStreamingContext getInstance()
    {
        return m_instance;
    }

    private NvsStreamingContext(Activity mainActivity)
    {
        m_assetPackageManager = new NvsAssetPackageManager();
        m_assetPackageManager.setInternalObject(nativeGetAssetPackageManager());
        nativeDetectPackageName(mainActivity);
    }
/*!
    \brief 获取资源包管理器
    \return 返回获取的资源包管理器对象
*/
    public NvsAssetPackageManager getAssetPackageManager()
    {
        return m_assetPackageManager;
    }
/*!
 *  \brief 采集设备能力描述

     定义采集设备的相关属性，包含自动聚焦，自动曝光，缩放等
 */
    public static class CaptureDeviceCapability
    {
        public boolean supportAutoFocus; //!< \if ENGLISH brief member variable description \else 是否支持自动聚焦 \endif

        public boolean supportZoom;  //!< \if ENGLISH brief member variable description \else 是否支持缩放 \endif
        public int maxZoom; //!< \if ENGLISH brief member variable description \else 最大缩放值 \endif
        public float[] zoomRatios; //!< \if ENGLISH brief member variable description \else 缩放比例 \endif

        public boolean supportFlash; //!< \if ENGLISH brief member variable description \else 是否支持换补灯光 \endif

        public boolean supportExposureCompensation; //!< \if ENGLISH brief member variable description \else 是否支持曝光补偿 \endif
        public int minExposureCompensation; //!< \if ENGLISH brief member variable description \else 最小曝光补偿系数 \endif
        public int maxExposureCompensation; //!< \if ENGLISH brief member variable description \else 最大曝光补偿系数 \endif
        public float exposureCompensationStep; //!< \if ENGLISH brief member variable description \else 曝光补偿步径值 \endif
    }
    /*!
        \brief 采集设备回调接口
    */
    public interface CaptureDeviceCallback
    {
        /*!
         *  \brief 采集设备预览准备完成
         *  \param captureDeviceIndex 设备索引
         */
        void onCaptureDeviceCapsReady(int captureDeviceIndex);

        /*!
         *  \brief 采集设备预览解析度准备完成
         *  \param captureDeviceIndex 设备索引
         */
        void onCaptureDevicePreviewResolutionReady(int captureDeviceIndex);

        /*!
         *  \brief 采集设备预览开始
         *  \param captureDeviceIndex 设备索引
         */
        void onCaptureDevicePreviewStarted(int captureDeviceIndex);

        /*!
         *  \brief 采集设备错误
         *  \param captureDeviceIndex 设备索引
         *  \param errorCode 错误码。请参见[采集设备错误标识] (@ref CAPTURE_DEVICE_ERROR)
         */
        void onCaptureDeviceError(int captureDeviceIndex, int errorCode);

        /*!
         *  \brief 采集设备停止
         *  \param captureDeviceIndex 设备索引
         */
        void onCaptureDeviceStopped(int captureDeviceIndex);

        /*!
         *  \brief 采集设备自动对焦完成
         *  \param captureDeviceIndex 设备索引
         *  \param succeeded 对焦是否完成
         */
        void onCaptureDeviceAutoFocusComplete(int captureDeviceIndex, boolean succeeded);

        /*!
         *  \brief 采集录制完成
         *  \param captureDeviceIndex 设备索引
         */
        void onCaptureRecordingFinished(int captureDeviceIndex);

        /*!
         *  \brief 采集录制失败
         *  \param captureDeviceIndex 设备索引
         */
        void onCaptureRecordingError(int captureDeviceIndex);
    }
    /*!
        \brief 时间线播放回调接口
    */
    public interface PlaybackCallback
    {
        /*!
         *  \brief 时间线播放预先加载完成
         *  \param timeline 时间线
         */
        void onPlaybackPreloadingCompletion(NvsTimeline timeline);

        /*!
         *  \brief 时间线播放停止
         *  \param timeline 时间线
         */
        void onPlaybackStopped(NvsTimeline timeline);

        /*!
         *  \brief 时间线播放到结尾
         *  \param timeline 时间线
         */
        void onPlaybackEOF(NvsTimeline timeline);
    }
    /*!
        \brief 时间线生成文件回调接口
    */
    public interface CompileCallback
    {

        /*!
         *  \brief 时间线生成文件进度
         *  \param timeline 时间线
         *  \param progress 进度值
         */
        void onCompileProgress(NvsTimeline timeline, int progress);

        /*!
         *  \brief 时间线生成文件完成
         *  \param timeline 时间线
         */
        void onCompileFinished(NvsTimeline timeline);

        /*!
         *  \brief 时间线生成文件失败
         *  \param timeline 时间线
         */
        void onCompileFailed(NvsTimeline timeline);
    }
    /*!
        \brief 流媒体引擎回调接口
    */
    public interface StreamingEngineCallback
    {
        /*!
         *  \brief 流媒体引擎状态改变
         *  \param state [流媒体引擎状态] (@ref STREAMING_ENGINE_STATE)
         */
        void onStreamingEngineStateChanged(int state);

        /*!
         *  \brief 第一视频帧呈现
         *  \param timeline 时间线
         */
        void onFirstVideoFramePresented(NvsTimeline timeline);
    }
    /*!
        \brief 时间线时间戳回调接口（注：非必要情况请勿使用此回调接口）
    */
    public interface TimelineTimestampCallback
    {
        /*!
         *  \brief 时间戳越界
         *  \param timeline 时间线
         *  \since 1.2.0
         */
        void onTimestampOutOfRange(NvsTimeline timeline);
    }

/*!
    \brief 设置采集设备回调接口
    \param cb 采集设备回调接口
*/
    public void setCaptureDeviceCallback(CaptureDeviceCallback cb)
    {
        m_catpureDeviceCallback = cb;
        nativeSetCaptureDeviceCallback(cb);
    }

/*!
    \brief 设置时间线播放回调接口
    \param cb 时间线播放回调接口
*/
    public void setPlaybackCallback(PlaybackCallback cb)
    {
        m_playbackCallback = cb;
        nativeSetPlaybackCallback(cb);
    }
/*!
    \brief 设置时间线生成文件回调接口
    \param cb 时间线生成文件回调接口
*/
    public void setCompileCallback(CompileCallback cb)
    {
        m_compileCallback = cb;
        nativeSetCompileCallback(cb);
    }
/*!
    \brief 设置流媒体引擎回调接口
    \param cb 流媒体引擎回调接口
*/
    public void setStreamingEngineCallback(StreamingEngineCallback cb)
    {
        m_streamingEngineCallback = cb;
        nativeSetStreamingEngineCallback(cb);
    }
/*!
    \brief 设置时间线时间戳回调接口
    \param cb 时间线时间戳回调接口
*/
    public void setTimelineTimestampCallback(TimelineTimestampCallback cb)
    {
        m_timelineTimestampCallback = cb;
    }
/*!
 *  \brief 获取音视频文件的详细信息
 *  \param avFilePath 音视频文件的路径
 *  \return 返回获取的音视频信息对象
 */
    public NvsAVFileInfo getAVFileInfo(String avFilePath)
    {
        return nativeGetAVFileInfo(avFilePath);
    }
/*!
 *  \brief 设置默认主题末尾的Logo
 *  \param logoImageFilePath Logo的路径
 *  \return 判断是否设置Logo成功。true则设置成功，false则失败。
    \sa getDefaultThemeEndingLogoImageFilePath
 */
    public boolean setDefaultThemeEndingLogoImageFilePath(String logoImageFilePath)
    {
        return nativeSetDefaultThemeEndingLogoImageFilePath(logoImageFilePath);
    }

/*!
 *  \brief 获取主题末尾默认的Logo图片路径
 *  \return 返回获得的Logo图片路径
    \sa setDefaultThemeEndingLogoImageFilePath
 */
    public String getDefaultThemeEndingLogoImageFilePath()
    {
        return nativeGetDefaultThemeEndingLogoImageFilePath();
    }

/*!
 *  \brief 创建时间线
 *  \param videoEditRes 视频解析度(指定图像宽高及像素比)。对于视频编辑解析度，在传入对应参数值时，目前要求传入的图像宽度值是4的倍数，高度值是2的倍数，并且视频编辑解析度里的imageWidth * imageHeight不能高于1920 * 1080像素。
 *  \param videoFps 视频帧率
 *  \param audioEditRes 音频解析度(指定采样率、采样格式及声道数)。对于音频编辑解析度，传入的采样率值支持两种：44100与48000。
 *  \return 返回创建的时间线对象
    \sa removeTimeline
 */
    public NvsTimeline createTimeline(NvsVideoResolution videoEditRes, NvsRational videoFps, NvsAudioResolution audioEditRes)
    {
        return nativeCreateTimeline(videoEditRes, videoFps, audioEditRes);
    }

/*!
 *  \brief 移除时间线
 *  \param timeline 要移除的时间线
 *  \return 返回布尔值。值为true则移除成功，false则失败。
 *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa createTimeline
 */
    public boolean removeTimeline(NvsTimeline timeline)
    {
        return nativeRemoveTimeline(timeline);
    }

/*!
 *  \brief 获取流媒体引擎状态
 *  \return 返回流媒体引擎状态。请参见[流媒体引擎状态标志] (@ref STREAMING_ENGINE_STATE)
 */
    public int getStreamingEngineState()
    {
        return nativeGetStreamingEngineState();
    }

/*!
 *  \brief 取得时间线当前的时间位置(单位为微秒)
 *  \param timeline 时间线对象
 *  \return 返回时间线的当前位置值
    \sa seekTimeline
 */
    public long getTimelineCurrentPosition(NvsTimeline timeline)
    {
        return nativeGetTimelineCurrentPosition(timeline);
    }

/*!
 *  \brief 将时间线生成一个文件
 *  \param timeline 时间线对象
 *  \param startTime 开始时间。startTime取值范围在[0,timeline.duration - 1],传入其他值无效。
 *  \param endTime 结束时间。endTime取值范围在(startTime,timeline.duration],同样传入其他值无效。
 *  \param outputFilePath 生成输出的文件路径。
 *  \param videoResolutionGrade 生成文件输出的视频分辨率。请参见[生成视频的分辨率高度级别] (@ref COMPILE_VIDEO_RESOLUTION_GRADE)
 *  \param videoBitrateGrade 生成文件输出的视频码率。请参见[生成视频的码率级别] (@ref COMPILE_BITRATE_GRADE)
 *  \param flags 生成文件输出的特殊标志，如果没有特殊需求，请填写0。请参见[生成文件标志] (@ref STREAMING_ENGINE_COMPILE_FLAG)
 *  \return 返回布尔值。注意：时间线生成文件是异步操作。返回true，则启动打包成功；false，则打包启动失败
 *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 *  \sa seekTimeline
 *  \sa playbackTimeline
 */
    public boolean compileTimeline(NvsTimeline timeline, long startTime, long endTime, String outputFilePath,
                                   int videoResolutionGrade, int videoBitrateGrade, int flags)
    {
        return nativeCompileTimeline(timeline, startTime, endTime, outputFilePath,
                videoResolutionGrade, videoBitrateGrade, flags);
    }

/*!
 *  \brief 连接时间线和实时预览图像窗口
 *  \param timeline 时间线对象
 *  \param liveWindow 实时预览图像窗口对象
 *  \return 返回布尔值。值为true则连接成功，false则失败。
 */
    public boolean connectTimelineWithLiveWindow(NvsTimeline timeline, NvsLiveWindow liveWindow)
    {
        return nativeConnectTimelineWithLiveWindow(timeline, liveWindow);
    }

/*! \cond */
    public boolean connectTimelineWithSurface(NvsTimeline timeline, Surface outputSurface)
    {
        return nativeConnectTimelineWithSurface(timeline, outputSurface);
    }
/*! \endcond */

/*!
 *  \brief 定位某一时间戳的图像
 *  \param timeline 时间线
 *  \param timestamp 时间戳。timestamp取值范围在[0,timeline.duration - 1]。传入其他值无效，seekTimeline会返回false,导致无法开启定位。
 *  \param videoSizeMode 图像预览模式。请参见[视频大小预览模式] (@ref VIDEO_PREVIEW_SIZEMODE)
 *  \param flags 引擎定位的特殊标志。具体参见[引擎定位标识](@ref STREAMING_ENGINE_SEEK_FLAG)
 *  \return 返回布尔值。注意：定位某时间戳图像是异步操作。返回true则成功开启定位，false则无法开启定位。
 *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 *  \sa playbackTimeline
 *  \sa compileTimeline
 */
    public boolean seekTimeline(NvsTimeline timeline, long timestamp, int videoSizeMode, int flags)
    {
        if (timestamp < 0 || timestamp >= timeline.getDuration()) {
            if (m_timelineTimestampCallback != null)
                m_timelineTimestampCallback.onTimestampOutOfRange(timeline);
            return false;
        }
        return nativeSeekTimeline(timeline, timestamp, videoSizeMode, flags);
    }
/*!
 *  \brief 定位某一时间戳的图像
 *  \param timeline 时间线
 *  \param timestamp 时间戳。timestamp取值范围在[0,timeline.duration - 1]。传入其他值无效，seekTimeline会返回false,导致无法开启定位。
 *  \param proxyScale 代理缩放比例
 *  \param flags 引擎定位的特殊标志。具体参见[引擎定位标识](@ref STREAMING_ENGINE_SEEK_FLAG)
 *  \return 返回布尔值。注意：定位某时间戳图像是异步操作。返回true则成功开启定位，false则无法开启定位。
 *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 *  \sa playbackTimeline
 *  \sa compileTimeline
 */
    public boolean seekTimeline(NvsTimeline timeline, long timestamp, NvsRational proxyScale, int flags)
    {
        if (timestamp < 0 || timestamp >= timeline.getDuration()) {
            if (m_timelineTimestampCallback != null)
                m_timelineTimestampCallback.onTimestampOutOfRange(timeline);
            return false;
        }
        return nativeSeekTimelineWithProxyScale(timeline, timestamp, proxyScale, flags);
    }

    /*!
     *  \brief 获取时间线某一时间戳的图像。详细情况参见[视频帧图像提取专题] (@ref videoFrameRetriever.md)
     *  \param timeline 欲获取图像的时间线对象
     *  \param timestamp 欲获取图像的时间戳。timestamp取值范围在[0,timeline.duration - 1]。传入其他值无效，grabImageFromTimeline会返回nil。
     *  \param proxyScale 代理缩放比例
     *  \return 返回该时间戳图像的Bitmap对象，如果获取图像失败返回null
     *  \since 1.1.2
     */
    public Bitmap grabImageFromTimeline(NvsTimeline timeline, long timestamp, NvsRational proxyScale)
    {
        return nativeGrabImageFromTimeline(timeline, timestamp, proxyScale);
    }

/*!
 *  \brief 播放时间线
 *  \param timeline 时间线
 *  \param startTime 开始时间(单位是微秒)。startTime取值范围在[0,timeline.duration - 1]。传入其他值无效，playbackTimeline会返回false导致无法开启播放。
 *  \param endTime 结束时间(单位是微秒)。如果endTime值传入是负值，则默认播放到视频末尾。
 *  \param videoSizeMode 视频预览模式。请参见[视频大小预览模式] (@ref VIDEO_PREVIEW_SIZEMODE)
 *  \param preload 是否预先加载
 *  \param flags 预览的特殊标志(暂时只设为0)
 *  \return 返回布尔值。注意：播放时间线是异步操作。返回true则可以开启播放时间线，false则无法开启播放。
 *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 *  \sa seekTimeline
 *  \sa compileTimeline
 */
    public boolean playbackTimeline(NvsTimeline timeline, long startTime, long endTime,
                                    int videoSizeMode, boolean preload, int flags)
    {
        if (startTime < 0 || startTime >= timeline.getDuration() || (endTime >= 0 && startTime >= endTime)) {
            if (m_timelineTimestampCallback != null)
                m_timelineTimestampCallback.onTimestampOutOfRange(timeline);
            return false;
        }
        return nativePlaybackTimeline(timeline, startTime, endTime, videoSizeMode, preload, flags);
    }

/*!
*  \brief 播放时间线
*  \param timeline 时间线
*  \param startTime 开始时间(单位是微秒)。startTime取值范围在[0,timeline.duration - 1]。传入其他值无效，playbackTimeline会返回false导致无法开启播放。
*  \param endTime 结束时间(单位是微秒)。如果endTime值传入是负值，则默认播放到视频末尾。
*  \param proxyScale 代理缩放比例
*  \param preload 是否预先加载
*  \param flags 预览的特殊标志(暂时只设为0)
*  \return 返回布尔值。注意：播放时间线是异步操作。返回true则可以开启播放时间线，false则无法开启播放。
*  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
*  \sa seekTimeline
*  \sa compileTimeline
*/
    public boolean playbackTimeline(NvsTimeline timeline, long startTime, long endTime,
                                    NvsRational proxyScale, boolean preload, int flags)
    {
        if (startTime < 0 || startTime >= timeline.getDuration() || (endTime >= 0 && startTime >= endTime)) {
            if (m_timelineTimestampCallback != null)
                m_timelineTimestampCallback.onTimestampOutOfRange(timeline);
            return false;
        }
        return nativePlaybackTimelineWithProxyScale(timeline, startTime, endTime, proxyScale, preload, flags);
    }

/*!
 *  \brief 停止引擎
 */
    public void stop()
    {
        nativeStop();
    }

/*!
 *  \brief 清除缓存资源
 *  \param asynchronous 设置是否为异步模式
 */
    public void clearCachedResources(boolean asynchronous)
    {
        nativeClearCachedResources(asynchronous);
    }

/*!
 *  \brief 获取采集设备的数量
 *  \return 返回采集设备数
    \sa isCaptureDeviceBackFacing
 */
    public int getCaptureDeviceCount()
    {
        return nativeGetCaptureDeviceCount();
    }

/*!
 *  \brief 判断是否为后置采集设备
 *  \param captureDeviceIndex 采集设备索引
 *  \return 返回布尔值。值为true则是后置采集设备，false则不是。
 */
    public boolean isCaptureDeviceBackFacing(int captureDeviceIndex)
    {
        return nativeIsCaptureDeviceBackFacing(captureDeviceIndex);
    }

/*!
 *  \brief 获取采集设备的能力描述对象
 *  \param captureDeviceIndex 采集设备索引
 *  \return 返回采集设备的能力描述对象
 */
    public CaptureDeviceCapability getCaptureDeviceCapability(int captureDeviceIndex)
    {
        return nativeGetCaptureDeviceCapability(captureDeviceIndex);
    }

/*!
 *  \brief 连接采集预览和实时预览图像窗口
 *  \param liveWindow 实时预览图像窗口对象
 *  \return 返回布尔值。值为true则连接成功，false则失败。
 *  \sa connectTimelineWithLiveWindow
 */
    public boolean connectCapturePreviewWithLiveWindow(NvsLiveWindow liveWindow)
    {
        return nativeConnectCapturePreviewWithLiveWindow(liveWindow);
    }

/*!
 *  \brief 启动采集预览
 *  \param captureDeviceIndex 采集设备索引
 *  \param videoResGrade 视频采集分辨率级别。请参见[视频采集分辨率级别](@ref VIDEO_CAPTURE_RESOLUTION_GRADE)
 *  \param flags 标志字段，如果无特殊需求请填写0。请参见[采集标志](@ref STREAMING_ENGINE_CAPTURE_FLAG)
 *  \param aspectRatio 预览视频横纵比，传入null则由系统采集设备来决定横纵比
 *  \return 返回布尔值。值为true则启动预览成功，false则启动失败。
 */
    public boolean startCapturePreview(int captureDeviceIndex, int videoResGrade, int flags, NvsRational aspectRatio)
    {
        if (!checkCameraPermission())
            return false;

        return nativeStartCapturePreview(captureDeviceIndex, videoResGrade, flags, aspectRatio);
    }

/*!
 *  \brief 为直播启动采集设备预览
 *  \param captureDeviceIndex 采集设备索引
 *  \param videoResGrade 视频采集分辨率级别。请参见[视频采集分辨率级别](@ref VIDEO_CAPTURE_RESOLUTION_GRADE)
 *  \param flags 标志字段，如果无特殊需求请填写0。请参见[采集标志](@ref STREAMING_ENGINE_CAPTURE_FLAG)
 *  \param aspectRatio 预览视频横纵比，传入null则由系统采集设备来决定横纵比
 *  \param liveStreamingEndPoint 直播推流的目的地址(rtmp://xxx)
 *  \return 返回布尔值。值为true则启动预览成功，false则启动预览失败。
 *  \since 1.3.0
 */
    public boolean startCapturePreviewForLiveStreaming(int captureDeviceIndex, int videoResGrade, int flags, NvsRational aspectRatio, String liveStreamingEndPoint)
    {
        if (!checkCameraPermission())
            return false;

        checkInternetPermission();

        return nativeStartCapturePreviewForLiveStreaming(captureDeviceIndex, videoResGrade, flags, aspectRatio, liveStreamingEndPoint);
    }

/*!
 *  \brief 对采集设备的视频帧内容进行采样
 *  \param sampleRect 采样的矩形区域，坐标为实时预览窗口的自身坐标系
 *  \return 返回值为采样的颜色值，该颜色值是采样的矩形区域内所有像素颜色的平均值
 *  \since 1.2.0
 */
    public NvsColor sampleColorFromCapturedVideoFrame(RectF sampleRect)
    {
        return nativeSampleColorFromCapturedVideoFrame(sampleRect);
    }

/*!
 *  \brief 启动自动聚焦
 *  \param focusRect 自动聚焦的检测区域，坐标为实时预览窗口的自身坐标系
    \sa cancelAutoFocus
 */
    public void startAutoFocus(RectF focusRect)
    {
        nativeStartAutoFocus(focusRect);
    }
/*!
 *  \brief 取消正在进行中的自动聚焦
    \sa startAutoFocus
 */
    public void cancelAutoFocus()
    {
        nativeCancelAutoFocus();
    }
/*!
 *  \brief 设置缩放因子
 *  \param zoom 缩放比例
    \sa getZoom
 */
    public void setZoom(int zoom)
    {
        nativeSetZoom(zoom);
    }
/*!
 *  \brief 获取缩放因子
 *  \return 返回缩放因子值
    \sa setZoom
 */
    public int getZoom()
    {
        return nativeGetZoom();
    }
/*!
 *  \brief 设置开/关换补光灯状态
 *  \param flashOn 开/关换补光灯状态。
    \sa isFlashOn
 */
    public void toggleFlash(boolean flashOn)
    {
        nativeToggleFlash(flashOn);
    }
/*!
 *  \brief 判断换补光灯状态
 *  \return 返回布尔值。值为true则换补光灯是打开状态，false则是关闭状态。
    \sa toggleFlash
 */
    public boolean isFlashOn()
    {
        return nativeIsFlashOn();
    }
/*!
 *  \brief 设置曝光补偿
 *  \param exposureCompensation 曝光补偿值
    \sa getExposureCompensation
 */
    public void setExposureCompensation(int exposureCompensation)
    {
        nativeSetExposureCompensation(exposureCompensation);
    }
/*!
 *  \brief 获取曝光补偿值
 *  \return 返回曝光补偿值
    \sa setExposureCompensation
 */
    public int getExposureCompensation()
    {
        return nativeGetExposureCompensation();
    }
/*!
 *  \brief 给采集的视频追加一个内建视频特效
 *  \param videoFxName 视频特效名。获取视频特效名称，请参见[getAllBuiltinCaptureVideoFxNames()] (@ref getAllBuiltinCaptureVideoFxNames)或[内建特效名称列表] (\ref FxNameList.md)。
 *  \return 返回追加的采集视频特效对象
    \sa insertBuiltinCaptureVideoFx
 */
    public NvsCaptureVideoFx appendBuiltinCaptureVideoFx(String videoFxName)
    {
        return insertBuiltinCaptureVideoFx(videoFxName, getCaptureVideoFxCount());
    }
/*!
 *  \brief 给采集的视频插入一个内建视频特效
 *  \param videoFxName 视频特效名。获取视频特效名称，请参见[getAllBuiltinCaptureVideoFxNames()] (@ref getAllBuiltinCaptureVideoFxNames)或[内建特效名称列表] (\ref FxNameList.md)。
 *  \param insertPosition 插入位置
 *  \return 返回插入的采集视频特效对象
    \sa appendBuiltinCaptureVideoFx
 */
    public NvsCaptureVideoFx insertBuiltinCaptureVideoFx(String videoFxName, int insertPosition)
    {
        return nativeInsertBuiltinCaptureVideoFx(videoFxName, insertPosition);
    }
/*!
 *  \brief 给采集的视频追加一个资源包视频特效
 *  \param videoFxPackageId 视频特效资源包ID
 *  \return 返回追加的采集视频特效对象
    \sa insertPackagedCaptureVideoFx
 */
    public NvsCaptureVideoFx appendPackagedCaptureVideoFx(String videoFxPackageId)
    {
        return insertPackagedCaptureVideoFx(videoFxPackageId, getCaptureVideoFxCount());
    }

/*!
 *  \brief 给采集的视频插入一个资源包视频特效
 *  \param videoFxPackageId 视频特效资源包ID
 *  \param insertPosition 插入位置
 *  \return 返回插入的采集视频特效对象
    \sa appendPackagedCaptureVideoFx
 */
    public NvsCaptureVideoFx insertPackagedCaptureVideoFx(String videoFxPackageId, int insertPosition)
    {
        return nativeInsertPackagedCaptureVideoFx(videoFxPackageId, insertPosition);
    }
/*!
 *  \brief 给采集的视频追加一个美颜视频特效
 *  \return 返回追加的采集视频特效对象
    \sa insertBeautyCaptureVideoFx
 */
    public NvsCaptureVideoFx appendBeautyCaptureVideoFx()
    {
        return insertBeautyCaptureVideoFx(getCaptureVideoFxCount());
    }
/*!
 *  \brief 给采集的视频插入一个美颜视频特效
 *  \param insertPosition 插入位置
 *  \return 返回插入的采集视频特效对象
    \sa appendBeautyCaptureVideoFx
 */
    public NvsCaptureVideoFx insertBeautyCaptureVideoFx(int insertPosition)
    {
        return insertBuiltinCaptureVideoFx(getBeautyVideoFxName(), insertPosition);
    }
/*!
 *  \brief 对采集的视频移除特定索引值的采集视频特效
 *  \param captureVideoFxIndex 采集视频特效索引
 *  \return 返回布尔值。返回true则采集视频特效移除成功，false则失败。
 *  \sa appendBuiltinCaptureVideoFx
 *  \sa appendPackagedCaptureVideoFx
 *  \sa appendBeautyCaptureVideoFx
 */
    public boolean removeCaptureVideoFx(int captureVideoFxIndex)
    {
        return nativeRemoveCaptureVideoFx(captureVideoFxIndex);
    }
/*!
 *  \brief 移除所有的采集视频特效
    \sa removeCaptureVideoFx
    \sa getCaptureVideoFxCount
 */
    public void removeAllCaptureVideoFx()
    {
        nativeRemoveAllCaptureVideoFx();
    }
/*!
 *  \brief 获得采集视频特效的数量
 *  \return 返回采集视频特效数量
 */
    public int getCaptureVideoFxCount()
    {
        return nativeGetCaptureVideoFxCount();
    }
/*!
 *  \brief 根据视频特效索引获得采集视频特效的对象
 *  \param captureVideoFxIndex 采集视频特效索引
 *  \return 返回获得的采集视频特效对象
    \sa removeCaptureVideoFx
 */
    public NvsCaptureVideoFx getCaptureVideoFxByIndex(int captureVideoFxIndex)
    {
        return nativeGetCaptureVideoFxByIndex(captureVideoFxIndex);
    }
/*!
 *  \brief 对采集设备应用拍摄场景资源包
 *  \param captureSceneId 拍摄场景资源包的ID
 *  \return 返回BOOL值。值为YES则应用成功，NO则失败。
 *  \since 1.2.0
 *  \sa getCurrentCaptureSceneId
 *  \sa removeCurrentCaptureScene
 */
    public boolean applyCaptureScene(String captureSceneId)
    {
        return nativeApplyCaptureScene(captureSceneId);
    }

/*!
 *  \brief 取得当前拍摄场景资源包的ID
 *  \return 当前拍摄场景资源包的ID字符串，返回nil表示当前没有拍摄场景资源包
 *  \since 1.2.0
 *  \sa applyCaptureScene:
 *  \sa removeCurrentCaptureScene
 */
    public String getCurrentCaptureSceneId()
    {
        return nativeGetCurrentCaptureSceneId();
    }

/*!
 *  \brief 移除当前拍摄场景
 *  \since 1.2.0
 *  \sa applyCaptureScene:
 *  \sa getCurrentCaptureSceneId
 */
    public void removeCurrentCaptureScene()
    {
        nativeRemoveCurrentCaptureScene();
    }

/*!
 *  \brief 启动录制采集信号。请参见[视频录制方式] (\ref videoRecorderMode.md)
 *  \param outputFilePath 录制文件的路径
 *  \return 返回布尔值。值为true则启动录制成功，false则启动失败。
    \sa stopRecording
 */
    public boolean startRecording(String outputFilePath)
    {
        if (!checkRecordAudioPermission())
            return false;

        return nativeStartRecording(outputFilePath);
    }

/*!
 *  \brief 结束录制采集信号
    \sa startRecording
 */
    public void stopRecording()
    {
        nativeStopRecording();
    }

/*!
 *  \brief 获取全部内嵌视频特效名称
 *  \return 返回包含所有内嵌的视频特效名称的List集合
    \sa getAllBuiltinAudioFxNames
 */
    public List<String> getAllBuiltinVideoFxNames()
    {
        return nativeGetAllBuiltinVideoFxNames();
    }

/*!
 *  \brief 获取全部内嵌音频特效名称
 *  \return 返回包含所有内嵌的音频特效名称的List集合
    \sa getAllBuiltinVideoFxNames
 */
    public List<String> getAllBuiltinAudioFxNames()
    {
        return nativeGetAllBuiltinAudioFxNames();
    }

/*!
 *  \brief 获取全部内嵌视频转场名称
 *  \return 返回包含所有内嵌的视频转场名称的List集合
    \sa getAllBuiltinCaptureVideoFxNames
    \sa getBeautyVideoFxName
    \sa getDefaultVideoTransitionName
 */
    public List<String> getAllBuiltinVideoTransitionNames()
    {
        return nativeGetAllBuiltinVideoTransitionNames();
    }

/*!
 *  \brief 获取全部内嵌采集视频特效名称
 *  \return 返回包含所有内嵌的采集视频特效名称的List集合
    \sa getBeautyVideoFxName
    \sa getAllBuiltinVideoFxNames

 */
    public List<String> getAllBuiltinCaptureVideoFxNames()
    {
        return nativeGetAllBuiltinCaptureVideoFxNames();
    }

/*!
 *  \brief 获得美颜的视频特效名称
 *  \return 返回美颜视频特效名称
    \sa getAllBuiltinVideoFxNames
    \sa getAllBuiltinCaptureVideoFxNames
 */
    public String getBeautyVideoFxName()
    {
        return nativeGetBeautyVideoFxName();
    }

/*!
 *  \brief 获得默认视频转场名称
 *  \return 返回表示默认转场名称的字符串
    \sa getAllBuiltinVideoTransitionNames
 */
    public String getDefaultVideoTransitionName()
    {
        return nativeGetDefaultVideoTransitionName();
    }

/*!
 *  \brief 获得视频特效描述
    \param fxName 视频特效名称
 *  \return 返回获得的视频特效描述
    \sa getAudioFxDescription
 */
    public NvsFxDescription getVideoFxDescription(String fxName)
    {
        return nativeGetVideoFxDescription(fxName);
    }

/*!
 *  \brief 获得音频特效描述
    \param fxName 音频特效名称
 *  \return 返回获得的音频特效描述
    \sa getVideoFxDescription
 */
    public NvsFxDescription getAudioFxDescription(String fxName)
    {
        return nativeGetAudioFxDescription(fxName);
    }

/*!
 *  \brief 创建视频帧提取对象
 *  \param videoFilePath 原始视频文件路径
 *  \return 返回NvsVideoFrameRetriever对象，表示视频帧提取对象
 *  \since 1.2.0
 */
    public NvsVideoFrameRetriever createVideoFrameRetriever(String videoFilePath)
    {
        NvsVideoFrameRetriever retriever = new NvsVideoFrameRetriever();
        retriever.m_videoFilePath = videoFilePath;
        return retriever;
    }

    //
    // Private methods goes below
    //
    private boolean checkCameraPermission()
    {
        if (Build.VERSION.SDK_INT < 23)
            return true;

        if (m_activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "CAMERA permission has not been granted!");
            return false;
        }

        return true;
    }

    private boolean checkRecordAudioPermission()
    {
        if (Build.VERSION.SDK_INT < 23)
            return true;

        if (m_activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "RECORD_AUDIO permission has not been granted!");
            return false;
        }

        return true;
    }

    private boolean checkInternetPermission()
    {
        if (Build.VERSION.SDK_INT < 23)
            return true;

        if (m_activity.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "INTERNET permission has not been granted!");
            return false;
        }

        return true;
    }

    private static void extractQtPlugins(Context ctx) throws IOException
    {
        String libsDir = ctx.getApplicationInfo().nativeLibraryDir + "/";

        long packageVersion = -1;
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            packageVersion = packageInfo.lastUpdateTime;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }

        if (!cleanQtCacheIfNecessary(packageVersion))
            return;

        try {
            File versionFile = new File(m_qtReservedFilesDirPath + "cache.version");

            File parentDirectory = versionFile.getParentFile();
            if (!parentDirectory.exists())
                parentDirectory.mkdirs();

            versionFile.createNewFile();

            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(versionFile));
            outputStream.writeLong(packageVersion);
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }

        final int pluginCount = m_qtPluginsInfo.length / 2;
        for (int i = 0; i < pluginCount; ++i) {
            String srcFilePath = m_nativeLibraryDirPath + m_qtPluginsInfo[i * 2];
            String dstFilePath = m_qtPluginsDirPath + m_qtPluginsInfo[i * 2 + 1];
            createQtBundledBinary(srcFilePath, dstFilePath);
        }
    }

    private static boolean cleanQtCacheIfNecessary(long packageVersion)
    {
        File versionFile = new File(m_qtReservedFilesDirPath + "cache.version");

        long cacheVersion = 0;
        if (versionFile.exists() && versionFile.canRead()) {
            try {
                DataInputStream inputStream = new DataInputStream(new FileInputStream(versionFile));
                cacheVersion = inputStream.readLong();
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }

        if (cacheVersion != packageVersion) {
            deleteRecursively(new File(m_qtReservedFilesDirPath));
            return true;
        } else {
            return false;
        }
    }

    private static void deleteRecursively(File directory)
    {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    deleteRecursively(file);
                else
                    file.delete();
            }

            directory.delete();
        }
    }

    private static void createQtBundledBinary(String source, String destination) throws IOException
    {
        // Already exists, we don't have to do anything
        File destinationFile = new File(destination);
        if (destinationFile.exists())
            return;

        File parentDirectory = destinationFile.getParentFile();
        if (!parentDirectory.exists())
            parentDirectory.mkdirs();

        destinationFile.createNewFile();

        InputStream inputStream = new FileInputStream(source);
        OutputStream outputStream = new FileOutputStream(destinationFile);
        copyFile(inputStream, outputStream);

        inputStream.close();
        outputStream.close();
    }

    static private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        byte[] buffer = new byte[32 * 1024];

        int count;
        while ((count = inputStream.read(buffer)) > 0)
            outputStream.write(buffer, 0, count);
    }

    private static native boolean nativeVerifySdkLicenseFile(Context ctx, String licenseFilePath);

    private static native boolean nativeInit(String appArgs, String envVars);
    private static native void nativeClose();

    private native long nativeGetAssetPackageManager();
    private native void nativeDetectPackageName(Context ctx);

    private static native void nativeSetCaptureDeviceCallback(CaptureDeviceCallback cb);
    private static native void nativeSetPlaybackCallback(PlaybackCallback cb);
    private static native void nativeSetCompileCallback(CompileCallback cb);
    private static native void nativeSetStreamingEngineCallback(StreamingEngineCallback cb);

    private native NvsAVFileInfo nativeGetAVFileInfo(String avFilePath);
    private native boolean nativeSetDefaultThemeEndingLogoImageFilePath(String logoImageFilePath);
    private native String nativeGetDefaultThemeEndingLogoImageFilePath();
    private native NvsTimeline nativeCreateTimeline(NvsVideoResolution videoEditRes, NvsRational videoFps, NvsAudioResolution audioEditRes);
    private native boolean nativeRemoveTimeline(NvsTimeline timeline);
    private native int nativeGetStreamingEngineState();
    private native long nativeGetTimelineCurrentPosition(NvsTimeline timeline);
    private native boolean nativeCompileTimeline(NvsTimeline timeline, long startTime, long endTime, String outputFilePath,
                                                 int videoResolutionGrade, int videoBitrateGrade, int flags);
    private native boolean nativeConnectTimelineWithLiveWindow(NvsTimeline timeline, NvsLiveWindow liveWindow);
    private native boolean nativeConnectTimelineWithSurface(NvsTimeline timeline, Surface outputSurface);
    private native boolean nativeSeekTimeline(NvsTimeline timeline, long timestamp, int videoSizeMode, int flags);
    private native boolean nativeSeekTimelineWithProxyScale(NvsTimeline timeline, long timestamp, NvsRational proxyScale, int flags);
    private native Bitmap nativeGrabImageFromTimeline(NvsTimeline timeline, long timestamp, NvsRational proxyScale);
    private native boolean nativePlaybackTimeline(NvsTimeline timeline, long startTime, long endTime,
                                                  int videoSizeMode, boolean preload, int flags);
    private native boolean nativePlaybackTimelineWithProxyScale(NvsTimeline timeline, long startTime, long endTime,
                                                                NvsRational proxyScale, boolean preload, int flags);
    private native void nativeStop();

    private native void nativeClearCachedResources(boolean asynchronous);

    private native int nativeGetCaptureDeviceCount();
    private native boolean nativeIsCaptureDeviceBackFacing(int captureDeviceIndex);
    private native CaptureDeviceCapability nativeGetCaptureDeviceCapability(int captureDeviceIndex);
    private native boolean nativeConnectCapturePreviewWithLiveWindow(NvsLiveWindow liveWindow);
    private native boolean nativeStartCapturePreview(int captureDeviceIndex, int videoResGrade, int flags, NvsRational aspectRatio);
    private native boolean nativeStartCapturePreviewForLiveStreaming(int captureDeviceIndex, int videoResGrade, int flags, NvsRational aspectRatio, String liveStreamingEndPoint);
    private native NvsColor nativeSampleColorFromCapturedVideoFrame(RectF sampleRect);
    private native void nativeStartAutoFocus(RectF focusRect);
    private native void nativeCancelAutoFocus();
    private native void nativeSetZoom(int zoom);
    private native int nativeGetZoom();
    private native void nativeToggleFlash(boolean flashOn);
    private native boolean nativeIsFlashOn();
    private native void nativeSetExposureCompensation(int exposureCompensation);
    private native int nativeGetExposureCompensation();
    private native NvsCaptureVideoFx nativeInsertBuiltinCaptureVideoFx(String videoFxName, int insertPosition);
    private native NvsCaptureVideoFx nativeInsertPackagedCaptureVideoFx(String videoFxPackageId, int insertPosition);
    private native boolean nativeRemoveCaptureVideoFx(int captureVideoFxIndex);
    private native void nativeRemoveAllCaptureVideoFx();
    private native int nativeGetCaptureVideoFxCount();
    private native NvsCaptureVideoFx nativeGetCaptureVideoFxByIndex(int captureVideoFxIndex);
    private native boolean nativeApplyCaptureScene(String captureSceneId);
    private native String nativeGetCurrentCaptureSceneId();
    private native void nativeRemoveCurrentCaptureScene();
    private native boolean nativeStartRecording(String outputFilePath);
    private native void nativeStopRecording();

    private native List<String> nativeGetAllBuiltinVideoFxNames();
    private native List<String> nativeGetAllBuiltinAudioFxNames();
    private native List<String> nativeGetAllBuiltinVideoTransitionNames();
    private native List<String> nativeGetAllBuiltinCaptureVideoFxNames();
    private native String nativeGetBeautyVideoFxName();
    private native String nativeGetDefaultVideoTransitionName();
    private native NvsFxDescription nativeGetVideoFxDescription(String fxName);
    private native NvsFxDescription nativeGetAudioFxDescription(String fxName);
}
