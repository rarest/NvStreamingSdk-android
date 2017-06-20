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

import android.graphics.SurfaceTexture;

public class NvCameraSurfaceTextureListener implements SurfaceTexture.OnFrameAvailableListener
{
    private final int m_texId;

    public NvCameraSurfaceTextureListener(int texId)
    {
        m_texId = texId;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        notifyCameraFrameAvailable(m_texId);
    }

    private static native void notifyCameraFrameAvailable(int texId);
}

