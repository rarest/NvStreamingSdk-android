//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2014. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 24. 2014
//   Author:        NewAuto video team
//================================================================================
package com.cdv.io;

import com.cdv.io.NvAndroidAudioRecorder;

import android.hardware.Camera;
import android.graphics.SurfaceTexture;
import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;
import android.util.Log;

import java.nio.ByteBuffer;


public class NvCamera implements Camera.ErrorCallback,
                                 Camera.AutoFocusCallback,
                                 Camera.OnZoomChangeListener,
                                 Camera.PreviewCallback,
                                 NvAndroidAudioRecorder.RecordDataCallback
{
    private int m_cameraId = -1;
    private Camera m_camera = null;
    private NvAndroidAudioRecorder m_audioRecorder = null;
    private Camera.Size m_previewSize = null;
    private OrientationEventListener m_orientationEventListener;

    private static final String TAG = "CDV Camera";

    private NvCamera(int cameraId, Camera cam, Context ctx)
    {
        m_cameraId = cameraId;
        m_camera = cam;
        cam.setErrorCallback(this);
        cam.setZoomChangeListener(this);

        m_orientationEventListener = new OrientationEventListener(ctx, SensorManager.SENSOR_DELAY_NORMAL)
        {
            @Override
            public void onOrientationChanged(int angle)
            {
                notifyOrientationChange(m_cameraId, angle);
            }
        };
    }

    public static NvCamera open(int cameraId, Context ctx)
    {
        try {
            Camera cam = Camera.open(cameraId);
            return new NvCamera(cameraId, cam, ctx);
        } catch(Exception e) {
            Log.e(TAG, "Failed to open camera(index=" + cameraId + ")!");
            Log.e(TAG, "" + e.getMessage());
        }
        return null;
    }

    public Camera.Parameters getParameters()
    {
        return m_camera.getParameters();
    }

    public void lock()
    {
        try {
            m_camera.lock();
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void unlock()
    {
        try {
            m_camera.unlock();
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void release()
    {
        m_camera.release();
    }

    public void reconnect()
    {
        try {
            m_camera.reconnect();
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void setDisplayOrientation(int degrees)
    {
        m_camera.setDisplayOrientation(degrees);
    }

    public void setParameters(Camera.Parameters params)
    {
        try {
            m_camera.setParameters(params);
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void setPreviewTexture(SurfaceTexture surfaceTexture)
    {
        try {
            m_camera.setPreviewTexture(surfaceTexture);
        } catch(Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public boolean startPreview(boolean setPreviewCallback, boolean startAudioRecord)
    {
        if (startAudioRecord) {
            m_audioRecorder = new NvAndroidAudioRecorder();
            if (!m_audioRecorder.startRecord(this)) {
                m_audioRecorder = null;
                return false;
            }
        }

        if (m_orientationEventListener.canDetectOrientation())
            m_orientationEventListener.enable();

        if (setPreviewCallback) {
            final Camera.Parameters params = m_camera.getParameters();
            m_previewSize = params.getPreviewSize();
            m_camera.setPreviewCallback(this);
        }

        m_camera.startPreview();
        return true;
    }

    public void stopPreview()
    {
        if (m_audioRecorder != null) {
            m_audioRecorder.stopRecord();
            m_audioRecorder = null;
        }

        if (m_orientationEventListener.canDetectOrientation())
            m_orientationEventListener.disable();

        m_camera.stopPreview();
        m_camera.setPreviewCallback(null);
    }

    public void autoFocus()
    {
        m_camera.autoFocus(this);
    }

    public void cancelAutoFocus()
    {
        m_camera.cancelAutoFocus();
    }

    @Override
    public void onError(int error, Camera camera)
    {
        notifyError(m_cameraId, error);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera)
    {
        notifyAutoFocusComplete(m_cameraId, success);
    }

    @Override
    public void onZoomChange(int zoomValue, boolean stopped, Camera camera)
    {
        notifyZoomChange(m_cameraId, zoomValue, stopped);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        if (data != null)
            notifyNewPreviewFrame(m_cameraId, data, m_previewSize.width, m_previewSize.height);
    }

    @Override
    public void onAudioRecordDataArrived(ByteBuffer buffer, int sampleCount)
    {
        notifyAudioRecordData(m_cameraId, buffer, sampleCount);
    }

    private static native void notifyError(int id, int error);
    private static native void notifyAutoFocusComplete(int id, boolean success);
    private static native void notifyZoomChange(int id, int zoomValue, boolean stopped);
    private static native void notifyNewPreviewFrame(int id, byte[] data, int width, int height);
    private static native void notifyAudioRecordData(int id, ByteBuffer buffer, int sampleCount);
    private static native void notifyOrientationChange(int id, int angle);
}

