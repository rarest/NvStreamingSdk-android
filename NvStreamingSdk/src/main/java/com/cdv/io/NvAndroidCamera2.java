//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2015. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Oct 06. 2014
//   Author:        NewAuto video team
//================================================================================
package com.cdv.io;

import com.cdv.io.NvSyncEvent;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.SensorManager;
import android.os.Handler;
import android.media.MediaRecorder;
import android.media.CamcorderProfile;
import android.graphics.SurfaceTexture;
import android.graphics.Rect;
import android.view.Surface;
import android.view.OrientationEventListener;
import android.util.Size;
import android.util.Range;
import android.util.Rational;
import android.util.Log;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;


public class NvAndroidCamera2
{
    private static final String TAG = "CDV Camera2";
    private static final boolean m_verbose = true;

    private int m_cameraIndex;
    private String m_cameraId;
    private Handler m_handler;
    private Size m_preivewSize;

    public CameraInfo m_cameraInfo;

    private CameraDevice m_cameraDevice;
    private CameraCharacteristics m_cameraCharacteristics;
    private boolean m_openingCameraDevice = false;
    private boolean m_openCameraDeviceSucceeded = false;

    private CameraCaptureSession m_captureSession;
    private boolean m_isWaitingForCaptureSessionClose = false;
    private CaptureRequest.Builder m_requestBuilder;

    private MediaRecorder m_mediaRecorder;
    private boolean m_isRecording = false;

    private boolean m_syncingCaptureRequest = false;
    private CaptureRequest m_captureRequestToSync;

    private SurfaceTexture m_texture;
    // Texture id used in SurfaceTexture object
    private int m_texId;

    private boolean m_isFocusing = false;

    private OrientationEventListener m_orientationEventListener;
    private int m_currentOrientationAngle = OrientationEventListener.ORIENTATION_UNKNOWN;
    private int m_lastValidOrientationAngle = OrientationEventListener.ORIENTATION_UNKNOWN;

    // Synchronization related
    private NvSyncEvent m_replyEvent = new NvSyncEvent(false);
    private static final long m_replyTimeout = 10000;

    public static boolean isLegacyCamera(Context ctx)
    {
        try {
            CameraManager manager = (CameraManager)ctx.getSystemService(Context.CAMERA_SERVICE);
            final String[] cameraIdList = manager.getCameraIdList();
            final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIdList[0]);
            return characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return true;
        }
    }

    public static NvAndroidCamera2 open(Context ctx,
                                        int cameraIndex,
                                        String cameraId,
                                        Handler handler,
                                        int texId)
    {
        try {
            CameraManager manager = (CameraManager)ctx.getSystemService(Context.CAMERA_SERVICE);
            NvAndroidCamera2 cam = new NvAndroidCamera2(ctx, cameraIndex, cameraId, handler, texId);
            if (!cam.openCamera(manager)) {
                cam.close();
                return null;
            }

            return cam;
        } catch(Exception e) {
            Log.e(TAG, "Failed to open camera '" + cameraId + "'!");
            Log.e(TAG, "" + e.getMessage());
            return null;
        }
    }

    public void close()
    {
        stopCapture();

        try {
            if (m_cameraDevice != null) {
                m_cameraDevice.close();
                m_cameraDevice = null;
            }
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public boolean startCapture(Size preivewSize)
    {
        if (m_cameraDevice == null)
            return false;

        m_preivewSize = preivewSize;

        try {
            // Create SurfaceTexture object which contian the real texture we use
            m_texture = new SurfaceTexture(m_texId);

            // Create MediaRecorder object
            m_mediaRecorder = new MediaRecorder();
            m_mediaRecorder.setOnErrorListener(m_mediaRecorderErrorListener);
            m_mediaRecorder.setOnInfoListener(m_mediaRecorderInfoListener);

            if (!startPreview()) {
                stopCapture();
                return false;
            }

            if (m_orientationEventListener != null && m_orientationEventListener.canDetectOrientation())
                m_orientationEventListener.enable();

            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            stopCapture();
            return false;
        }
    }

    public void stopCapture()
    {
        if (m_orientationEventListener != null && m_orientationEventListener.canDetectOrientation())
            m_orientationEventListener.disable();

        try {
            stopRecord();

            if (m_captureSession != null) {
                m_captureSession.abortCaptures();
                m_isWaitingForCaptureSessionClose = true;
                m_captureSession.close();
                // NOTE: we must wait util capture session has been closed,
                // otherwise the preview surface may still being used by it
                m_replyEvent.waitEvent(m_replyTimeout);
                m_isWaitingForCaptureSessionClose = false;
                m_captureSession = null;
            }

            // NOTE: we must release MediaRecorder after we close capture session
            // since the surface of MediaRecorder object may still being used by it
            if (m_mediaRecorder != null) {
                m_mediaRecorder.release();
                m_mediaRecorder = null;
            }

            m_requestBuilder = null;

            if (m_texture != null) {
                m_texture.release();
                m_texture = null;
            }
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            m_isWaitingForCaptureSessionClose = false;
        }
    }

    public boolean startRecord(String recordingFilePath, Size recordingSize)
    {
        if (m_isRecording || m_captureSession == null)
            return false;

        if (!setupMediaRecorder(recordingFilePath, recordingSize))
            return false;

        try {
            // We configure the size of default buffer to be the size of camera preview we want.
            m_texture.setDefaultBufferSize(m_preivewSize.getWidth(), m_preivewSize.getHeight());
            // Set frame available listener
            m_texture.setOnFrameAvailableListener(m_frameAvailableListener, m_handler);

            // Create capture request builder and setup(the old one will be released)
            m_requestBuilder = m_cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            initCaptureRequestBuilder(m_requestBuilder);

            // This is the output Surface we need to start preview.
            Surface previewSurface = new Surface(m_texture);
            m_requestBuilder.addTarget(previewSurface);

            // Get Surface object in MediaRecorder
            final Surface mediaRecorderSurface = m_mediaRecorder.getSurface();
            m_requestBuilder.addTarget(mediaRecorderSurface);

            List<Surface> surfaces = new ArrayList<Surface>();
            surfaces.add(previewSurface);
            surfaces.add(mediaRecorderSurface);

            // (Re)Start capture session
            // NOTE: According to android developer documentation: Using createCaptureSession(List, CameraCaptureSession.StateCallback, Handler)
            // directly without closing the previous session is the recommended approach for quickly switching to a new session,
            // since unchanged target outputs can be reused more efficiently
            m_cameraDevice.createCaptureSession(surfaces, m_cameraCaptureSessionStateCallback, m_handler);
            m_replyEvent.waitEvent(m_replyTimeout);
            if (m_captureSession == null) {
                Log.e(TAG, "Failed to create capture session!");
                m_mediaRecorder.reset();
                return false;
            }

            final CaptureRequest recordingCaptureRequest = m_requestBuilder.build();
            m_captureSession.setRepeatingRequest(recordingCaptureRequest, m_cameraCaptureSessionCaptureCallback, m_handler);

            // Wait until capture really started
            m_syncingCaptureRequest = true;
            synchronized(this) {
                m_captureRequestToSync = recordingCaptureRequest;
            }
            m_replyEvent.waitEvent(m_replyTimeout);
            m_syncingCaptureRequest = false;
            synchronized (this) {
                m_captureRequestToSync = null;
            }

            m_mediaRecorder.start();
            m_isRecording = true;
            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            m_mediaRecorder.reset();
            return false;
        }
    }

    public boolean stopRecord()
    {
        if (!m_isRecording)
            return false;

        try {
            // NOTE: There is an issue about crashing on stop MediaRecorder,
            // Please refer to https://github.com/googlesamples/android-Camera2Video/issues/2 for more detail
            // After some testing, I think the best way to avoid this crash is to
            // close the current capture session and wait until the close callback is called.
            m_captureSession.abortCaptures();
            m_isWaitingForCaptureSessionClose = true;
            m_captureSession.close();
            m_replyEvent.waitEvent(m_replyTimeout);
            m_isWaitingForCaptureSessionClose = false;
            m_captureSession = null;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            m_isWaitingForCaptureSessionClose = false;
        }

        boolean recordingSucceeded = true;
        try {
            m_mediaRecorder.stop();
            m_mediaRecorder.reset();
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            recordingSucceeded = false;
        }

        m_isRecording = false;

        // Restart preivew capture session
        startPreview();

        return recordingSucceeded;
    }

    public void updateSurfaceTextureImage()
    {
        try {
            if (m_texture != null)
                m_texture.updateTexImage();
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public long getSurfaceTextureTimestamp()
    {
        if (m_texture != null)
            return m_texture.getTimestamp();
        else
            return 0;
    }

    public void getSurfaceTextureTransformMatrix(float[] mtx)
    {
        if (m_texture != null)
            m_texture.getTransformMatrix(mtx);
    }

    public boolean startFocus()
    {
        if (m_captureSession == null || !m_cameraInfo.m_supportAutoFocus)
            return false;

        m_requestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, null);
        return doStartFocus();
    }

    public boolean startFocus(float left, float right, float bottom, float top)
    {
        if (m_captureSession == null || !m_cameraInfo.m_supportAutoFocus || !m_cameraInfo.m_supportFocusArea)
            return false;

        try {
            final Rect focusRect = calcFocusRect(left, right, bottom, top);
            final MeteringRectangle meteringRect = new MeteringRectangle(focusRect, 1);
            final MeteringRectangle[] controlAFRegions = new MeteringRectangle[] {meteringRect};
            m_requestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, controlAFRegions);
            return doStartFocus();
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        }
    }

    public void cancelFocus()
    {
        if (m_captureSession == null || !m_cameraInfo.m_supportAutoFocus)
            return;

        if (!m_isFocusing)
            return;

        try {
            m_requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_CANCEL);
            m_captureSession.capture(m_requestBuilder.build(), m_cameraCaptureSessionCaptureCallback, m_handler);
            m_isFocusing = false;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        } finally {
            m_requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
        }
    }

    public void toggleFlash(boolean on)
    {
        if (m_captureSession == null || !m_cameraInfo.m_supportFlash)
            return;

        if (on == m_cameraInfo.m_isFlashOn)
            return;

        m_cameraInfo.m_isFlashOn = on;

        try {
            if (on)
                m_requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            else
                m_requestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);

            m_captureSession.setRepeatingRequest(m_requestBuilder.build(), m_cameraCaptureSessionCaptureCallback, m_handler);
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public boolean setDigitalZoom(float digitalZoom)
    {
        if (m_captureSession == null || digitalZoom < 1 || digitalZoom > m_cameraInfo.m_maxDigitalZoom)
            return false;
        if (Math.abs(digitalZoom - m_cameraInfo.m_digitalZoom) < 0.001)
            return true;

        m_cameraInfo.m_digitalZoom = digitalZoom;
        setupCropRegion(m_requestBuilder);

        try {
            m_captureSession.setRepeatingRequest(m_requestBuilder.build(), m_cameraCaptureSessionCaptureCallback, m_handler);
            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        }
    }

    public boolean setAECompensation(int compensation)
    {
        if (m_captureSession == null || compensation < m_cameraInfo.m_minAECompensation || compensation > m_cameraInfo.m_maxAECompensation)
            return false;

        final Integer curCompensation = m_requestBuilder.get(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
        if (curCompensation != null && curCompensation == compensation)
            return true;

        try {
            m_requestBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, compensation);
            m_captureSession.setRepeatingRequest(m_requestBuilder.build(), m_cameraCaptureSessionCaptureCallback, m_handler);
            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        }
    }

    private NvAndroidCamera2(Context ctx, int cameraIndex, String cameraId, Handler handler, int texId) throws Exception
    {
        m_cameraIndex = cameraIndex;
        m_cameraId = cameraId;
        m_handler = handler;
        m_texId = texId;

        CameraManager manager = (CameraManager)ctx.getSystemService(Context.CAMERA_SERVICE);
        m_cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
        m_cameraInfo = new CameraInfo(m_cameraCharacteristics);

        // Create orientation event listener
        m_orientationEventListener = new OrientationEventListener(ctx, SensorManager.SENSOR_DELAY_NORMAL)
        {
            @Override
            public void onOrientationChanged(int angle)
            {
                synchronized (NvAndroidCamera2.this) {
                    m_currentOrientationAngle = angle;
                    if (angle != OrientationEventListener.ORIENTATION_UNKNOWN)
                        m_lastValidOrientationAngle = angle;
                }
            }
        };
    }

    private boolean openCamera(CameraManager manager)
    {
        try {
            // Open camera device
            m_openingCameraDevice = true;
            manager.openCamera(m_cameraId, m_cameraDeviceStateCallback, m_handler);
            m_replyEvent.waitEvent(m_replyTimeout);
            m_openingCameraDevice = false;
            if (!m_openCameraDeviceSucceeded) {
                // Failed to open camera device
                m_cameraDevice.close();
                m_cameraDevice = null;
                return false;
            }

            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        }
    }

    private void initCaptureRequestBuilder(CaptureRequest.Builder builder)
    {
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
        if (m_cameraInfo.m_supportAutoFocus)
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);

        //
        // Set AE target fps range the one whose average frame rate are closest to 30 fps
        //
        Range<Integer>[] ranges = m_cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
        if (m_verbose) {
            for (Range<Integer> range : ranges)
                Log.d(TAG, "AE fps range: " + range.toString());
        }

        if (ranges.length > 0) {
            // NOTE: some devices(I guess all LEGACY hardware level camera) will report fps multiplied with 1000,
            // here we check the first fps range to detect this case.
            final float multiplier = ranges[0].getLower() >= 1000 ? 1000 : 1;

            int bestMatchIndex = -1;
            float bestMatchFps = 0;
            for (int i = 0; i < ranges.length; i++) {
                final Range<Integer> range = ranges[i];
                final float avgFps = (range.getLower() + range.getUpper()) / (2 * multiplier);
                if (bestMatchIndex < 0 || Math.abs(avgFps - 30) < Math.abs(bestMatchFps - 30)) {
                    bestMatchIndex = i;
                    bestMatchFps = avgFps;
                }
            }

            builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, ranges[bestMatchIndex]);
        }

        if (m_verbose)
            Log.d(TAG, "Current AE fps range: " + builder.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE));

        //
        // Setup flash mode
        //
        if (m_cameraInfo.m_supportFlash) {
            if (m_cameraInfo.m_isFlashOn)
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            else
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        }

        setupCropRegion(builder);
        if (m_verbose) {
            final Rect r = builder.get(CaptureRequest.SCALER_CROP_REGION);
            if (r != null)
                Log.d(TAG, "SCALER_CROP_REGION: " + r.toString());
            else
                Log.d(TAG, "SCALER_CROP_REGION: null");
        }
    }

    private void setupCropRegion(CaptureRequest.Builder builder)
    {
        //
        // Calculate crop rect according to preview size's and active array's aspect ratio and digital zoom
        //
        final int activeArrayWidth = m_cameraInfo.m_sensorActiveArrayRect.width();
        final int activeArrayHeight = m_cameraInfo.m_sensorActiveArrayRect.height();
        final float activeArrayAspectRatio = (float)activeArrayWidth / activeArrayHeight;
        final float previewAspectRatio = (float)m_preivewSize.getWidth() / m_preivewSize.getHeight();

        Rect cropRect = new Rect();
        if (previewAspectRatio >= activeArrayAspectRatio) {
            final float w = activeArrayWidth / m_cameraInfo.m_digitalZoom;
            final int vMargin = (int)((activeArrayHeight - w / previewAspectRatio) / 2);
            final int hMargin = (int)((activeArrayWidth - w) / 2);
            cropRect.set(hMargin, vMargin, activeArrayWidth - hMargin, activeArrayHeight - vMargin);
        } else {
            final float h = activeArrayHeight / m_cameraInfo.m_digitalZoom;
            final int hMargin = (int)((activeArrayWidth - h * previewAspectRatio) / 2);
            final int vMargin = (int)((activeArrayHeight - h) / 2);
            cropRect.set(hMargin, vMargin, activeArrayWidth - hMargin, activeArrayHeight - vMargin);
        }

        builder.set(CaptureRequest.SCALER_CROP_REGION, cropRect);
    }

    private boolean startPreview()
    {
        try {
            // We configure the size of default buffer to be the size of camera preview we want.
            m_texture.setDefaultBufferSize(m_preivewSize.getWidth(), m_preivewSize.getHeight());
            // Set frame available listener
            m_texture.setOnFrameAvailableListener(m_frameAvailableListener, m_handler);

            // Create capture request builder and setup(the old one will be released)
            m_requestBuilder = m_cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            initCaptureRequestBuilder(m_requestBuilder);

            // This is the output Surface we need to start preview.
            Surface previewSurface = new Surface(m_texture);
            m_requestBuilder.addTarget(previewSurface);

            List<Surface> surfaces = new ArrayList<Surface>();
            surfaces.add(previewSurface);

            // (Re)Start capture session
            // NOTE: According to android developer documentation: Using createCaptureSession(List, CameraCaptureSession.StateCallback, Handler)
            // directly without closing the previous session is the recommended approach for quickly switching to a new session,
            // since unchanged target outputs can be reused more efficiently
            m_cameraDevice.createCaptureSession(surfaces, m_cameraCaptureSessionStateCallback, m_handler);
            m_replyEvent.waitEvent(m_replyTimeout);
            if (m_captureSession == null) {
                Log.e(TAG, "Failed to create capture session!");
                return false;
            }

            m_captureSession.setRepeatingRequest(m_requestBuilder.build(), m_cameraCaptureSessionCaptureCallback, m_handler);
            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        }
    }

    private boolean setupMediaRecorder(String recordFilePath, Size recordingSize)
    {
        final int refVideoBitRateAVC720P = 7500000;

        int fileFormat = MediaRecorder.OutputFormat.MPEG_4;
        int videoBitRate = calcVideoBitRate(1280, 720, refVideoBitRateAVC720P, recordingSize.getWidth(), recordingSize.getHeight());
        int videoFrameRate = 30;
        int videoCodec = MediaRecorder.VideoEncoder.H264;
        int audioChannels = 2;
        int audioSampleRate = 44100;
        int audioBitRate = 128000;
        int audioCodec = MediaRecorder.AudioEncoder.AAC;

        // Get the predefined camcorder profile to setup media recorder
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        if (profile != null) {
            final int bitRate = calcVideoBitRate(profile.videoFrameWidth,
                                                 profile.videoFrameHeight,
                                                 profile.videoBitRate,
                                                 recordingSize.getWidth(),
                                                 recordingSize.getHeight());
            // Limit the video bitrate according to our 720P reference bitrate
            videoBitRate = Math.min(videoBitRate, bitRate);

            videoFrameRate = profile.videoFrameRate;
            videoCodec = profile.videoCodec;
            audioChannels = profile.audioChannels;
            audioSampleRate = profile.audioSampleRate;
            audioBitRate = profile.audioBitRate;
            audioCodec = profile.audioCodec;
        }

        try {
            m_mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            m_mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            m_mediaRecorder.setOutputFormat(fileFormat);
            m_mediaRecorder.setOutputFile(recordFilePath);
            m_mediaRecorder.setVideoEncodingBitRate(videoBitRate);
            m_mediaRecorder.setVideoFrameRate(videoFrameRate);
            m_mediaRecorder.setVideoSize(recordingSize.getWidth(), recordingSize.getHeight());
            m_mediaRecorder.setVideoEncoder(videoCodec);
            m_mediaRecorder.setAudioEncodingBitRate(audioBitRate);
            m_mediaRecorder.setAudioSamplingRate(audioSampleRate);
            m_mediaRecorder.setAudioChannels(audioChannels);
            m_mediaRecorder.setAudioEncoder(audioCodec);

            // Set orientation hint
            setMediaRecorderOrientationHint();

            if (m_verbose) {
                Log.d(TAG, "MediaRecroder conf: videoBitRate:" + videoBitRate + ", videoFrameRate:" + videoFrameRate +
                    ", audioBitRate:" + audioBitRate + ", audioSampleRate:" + audioSampleRate + ", audioChannels:" + audioChannels);
            }

            m_mediaRecorder.prepare();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            m_mediaRecorder.reset();
            return false;
        }
    }

    private int calcVideoBitRate(int refWidth, int refHeight, int refBitRate, int width, int height)
    {
        final int refArea = refWidth * refHeight;
        final int area = width * height;
        return (int)(((long)area * refBitRate + refArea / 2) / refArea);
    }

    private void setMediaRecorderOrientationHint()
    {
        int angle = 0;
        synchronized(this) {
            if (m_currentOrientationAngle != OrientationEventListener.ORIENTATION_UNKNOWN)
                angle = m_currentOrientationAngle;
            // The device is close to flat and the orientation cannot be determined.
            // In this case we use the last valid orienation angle to determine it.
            else if (m_lastValidOrientationAngle != OrientationEventListener.ORIENTATION_UNKNOWN)
                angle = m_lastValidOrientationAngle;
        }

        final int phasedOrienationAngle = getPhasedOrientationAngle(angle);

        // Determine camera facing
        boolean backFacing = false;
        if (m_cameraCharacteristics != null) {
            final int facing = m_cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            backFacing = facing == CameraCharacteristics.LENS_FACING_BACK;
        }

        // Get camera device orientation
        final int cameraOrienationAngle = m_cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        if (backFacing)
            angle = (cameraOrienationAngle + phasedOrienationAngle) % 360;
        else
            angle = (cameraOrienationAngle - phasedOrienationAngle + 360) % 360;

        m_mediaRecorder.setOrientationHint(angle);
    }

    private int getPhasedOrientationAngle(int angle)
    {
        if (angle <= 45 || angle >= 315)
            return 0;
        else if (angle < 135)
            return 90;
        else if (angle <= 225)
            return 180;
        else
            return 270;
    }

    private Rect calcFocusRect(float left, float right, float bottom, float top)
    {
        final int activeArrayWidth = m_cameraInfo.m_sensorActiveArrayRect.width();
        final int activeArrayHeight = m_cameraInfo.m_sensorActiveArrayRect.height();
        final Rect crop = m_requestBuilder.get(CaptureRequest.SCALER_CROP_REGION);
        final Rect cropRect = (crop != null) ? crop : new Rect(0, 0, activeArrayWidth, activeArrayHeight);
        final int refWidth = cropRect.width();
        final int refHeight = cropRect.height();
        int l = (int)Math.floor(refWidth * ((left + 1) / 2));
        int r = (int)Math.ceil(refWidth * ((right + 1) / 2));
        int t = (int)Math.floor(refHeight - refHeight * ((top + 1) / 2));
        int b = (int)Math.ceil(refHeight - refHeight * ((bottom + 1) / 2));
        l = Math.max(l, 0);
        l = Math.min(l, activeArrayWidth - 1);
        r = Math.max(r, l);
        r = Math.min(r, activeArrayWidth);
        t = Math.max(t, 0);
        t = Math.min(t, activeArrayHeight - 1);
        b = Math.max(b, t);
        b = Math.min(b, activeArrayHeight);
        Rect focusRect = new Rect(l, t, r, b);
        focusRect.offset(cropRect.left, cropRect.top);
        return focusRect;
    }

    private boolean doStartFocus()
    {
        try {
            m_requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
            m_captureSession.capture(m_requestBuilder.build(), m_cameraCaptureSessionCaptureCallback, m_handler);
            m_isFocusing = true;
            return true;
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
            return false;
        } finally {
            m_requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
        }
    }

    //
    // CameraDevice.StateCallback is called when CameraDevice changes its status.
    //
    final private CameraDevice.StateCallback m_cameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice)
        {
            m_cameraDevice = cameraDevice;
            m_openingCameraDevice = false;
            m_openCameraDeviceSucceeded = true;
            m_replyEvent.setEvent();
        }

        @Override
        public void onClosed(CameraDevice cameraDevice)
        {
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice)
        {
            Log.e(TAG, "Camera '" + cameraDevice.getId() + "' has been disconnected!");

            if (m_openingCameraDevice) {
                m_cameraDevice = cameraDevice;
                m_openingCameraDevice = false;
                m_openCameraDeviceSucceeded = false;
                m_replyEvent.setEvent();
            } else {
                notifyCameraDisconnected(m_cameraIndex);
            }
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error)
        {
            Log.e(TAG, "Camera '" + cameraDevice.getId() + "' has has encountered a serious error! errno=" + error);

            if (m_openingCameraDevice) {
                m_cameraDevice = cameraDevice;
                m_openingCameraDevice = false;
                m_openCameraDeviceSucceeded = false;
                m_replyEvent.setEvent();
            } else {
                notifyCameraError(m_cameraIndex, error);
            }
        }
    };

    //
    // CameraCaptureSession.StateCallback is a callback object for receiving updates about the state of a camera capture session.
    //
    final CameraCaptureSession.StateCallback m_cameraCaptureSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession cameraCaptureSession)
        {
            m_captureSession = cameraCaptureSession;
            m_replyEvent.setEvent();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession)
        {
            Log.e(TAG, "Failed to configure camera capture session!");
            m_captureSession = null;
            m_replyEvent.setEvent();
        }

        @Override
        public void onClosed(CameraCaptureSession session)
        {
            if (m_isWaitingForCaptureSessionClose) {
                m_isWaitingForCaptureSessionClose = false;
                m_replyEvent.setEvent();
            }
        }
    };

    //
    // CameraCaptureSession.CaptureCallback is a callback object for tracking the progress of a CaptureRequest submitted to the camera device.
    //
    final CameraCaptureSession.CaptureCallback m_cameraCaptureSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        private void process(CaptureResult result)
        {
             // Check AF state
            final Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
            // NOTE: afState may be null on some deives
            if (afState != null) {
                if ((afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) && m_isFocusing) {
                    m_isFocusing = false;
                    // Send focus completion notification
                    notifyAutoFocusComplete(m_cameraIndex, afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED);
                }
            }
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session,
                                        CaptureRequest request,
                                        CaptureResult partialResult)
        {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session,
                                       CaptureRequest request,
                                       TotalCaptureResult result)
        {
            process(result);
        }

        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber)
        {
            if (!m_syncingCaptureRequest)
                return;

            CaptureRequest captureRequestToSync = null;
            synchronized (NvAndroidCamera2.this) {
                captureRequestToSync = m_captureRequestToSync;
            }

            if (request == captureRequestToSync) {
                m_syncingCaptureRequest = false;
                synchronized (NvAndroidCamera2.this) {
                    m_captureRequestToSync = null;
                }
                m_replyEvent.setEvent();
            }
        }
    };

    //
    // SurfaceTexture.OnFrameAvailableListener is called when new stream frame of SurfaceTexture is available.
    //
    final SurfaceTexture.OnFrameAvailableListener m_frameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture)
        {
            notifyCameraFrameAvailable(m_cameraIndex);
        }
    };

    //
    // MediaRecorder.OnErrorListener will be invoked when an error occurs while recording.
    //
    final MediaRecorder.OnErrorListener m_mediaRecorderErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra)
        {
            notifyMediaRecorderError(m_cameraIndex, what, extra);
        }
    };

    //
    // MediaRecorder.OnInfoListener will be invoked when an error occurs while recording.
    //
    final MediaRecorder.OnInfoListener m_mediaRecorderInfoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra)
        {
            notifyMediaRecorderInfo(m_cameraIndex, what, extra);
        }
    };

    //
    // Nested class to collect camera information
    //
    public static class CameraInfo {
        public int m_hardwareLevel = CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
        public Rect m_sensorActiveArrayRect;

        public Size[] m_preivewVideoSizeArray;
        public Size[] m_recordingVideoSizeArray;

        public boolean m_supportFlash = false;
        public boolean m_isFlashOn = false;

        public boolean m_supportAutoFocus = false;
        public boolean m_supportFocusArea = false;

        public float m_maxDigitalZoom = 1;
        private float m_digitalZoom = 1;

        public int m_minAECompensation = 0;
        public int m_maxAECompensation = 0;
        public float m_aeCompenationStep = 0;

        private CameraInfo(CameraCharacteristics characteristics)
        {
            // Detect hardware level
            m_hardwareLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (m_hardwareLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY)
                Log.d(TAG, "Camera hardware level: LEGACY");
            else if (m_hardwareLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED)
                Log.d(TAG, "Camera hardware level: LIMITED");
            else if (m_hardwareLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)
                Log.d(TAG, "Camera hardware level: FULL");

            m_sensorActiveArrayRect = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            if (m_verbose) {
                Log.d(TAG, "SENSOR_INFO_PIXEL_ARRAY_SIZE: " + characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE).toString());
                Log.d(TAG, "SENSOR_INFO_ACTIVE_ARRAY_SIZE: " + m_sensorActiveArrayRect.toString());
            }

            // Collect supported video sizes
            final StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            m_preivewVideoSizeArray = map.getOutputSizes(SurfaceTexture.class);
            m_recordingVideoSizeArray = map.getOutputSizes(MediaRecorder.class);

            // Check flash unit support
            m_supportFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

            //
            // Check auto focus support
            //
            final int[] availabeAFModes = characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
            for (int m : availabeAFModes) {
                if (m == CameraMetadata.CONTROL_AF_MODE_AUTO) {
                    m_supportAutoFocus = true;
                    break;
                }
            }

            m_supportFocusArea = characteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF) >= 1;

            // Detect max digital zoom
            m_maxDigitalZoom = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
            if (m_verbose)
                Log.d(TAG, "SCALER_AVAILABLE_MAX_DIGITAL_ZOOM: " + m_maxDigitalZoom);

            //
            // Detect AE compenstation range and step
            //
            final Range<Integer> aeCompenstaionRange = characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
            m_minAECompensation = aeCompenstaionRange.getLower().intValue();
            m_maxAECompensation = aeCompenstaionRange.getUpper().intValue();
            final Rational aeCompensationStep = characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
            m_aeCompenationStep = aeCompensationStep.floatValue();
            if (m_verbose) {
                Log.d(TAG, "CONTROL_AE_COMPENSATION_RANGE: " + aeCompenstaionRange.toString());
                Log.d(TAG, "CONTROL_AE_COMPENSATION_STEP: " + aeCompensationStep.toString());
            }
        }
    }


    //
    // Native methods
    //
    private static native void notifyCameraDisconnected(int cameraIndex);
    private static native void notifyCameraError(int cameraIndex, int error);
    private static native void notifyCameraFrameAvailable(int cameraIndex);
    private static native void notifyAutoFocusComplete(int cameraIndex, boolean succeeded);
    private static native void notifyMediaRecorderError(int cameraIndex, int what, int extra);
    private static native void notifyMediaRecorderInfo(int cameraIndex, int what, int extra);
}

