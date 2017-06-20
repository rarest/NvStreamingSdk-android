//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2014. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    May 27 2014
//   Author:        NewAuto video team
//================================================================================
package com.cdv.io;

import android.media.MediaRecorder;

public class NvMediaRecorderListener implements MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener
{
    private int m_id = -1;

    public NvMediaRecorderListener(int id)
    {
        m_id = id;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra)
    {
        notifyMediaRecorderError(m_id, what, extra);
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra)
    {
        notifyMediaRecorderInfo(m_id, what, extra);
    }

    private static native void notifyMediaRecorderError(int id, int what, int extra);
    private static native void notifyMediaRecorderInfo(int id, int what, int extra);
}

