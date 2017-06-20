package com.cdv.io;

import android.graphics.SurfaceTexture;

public class NvVideoSurfaceTexture implements SurfaceTexture.OnFrameAvailableListener
{
    private final int m_texId;

    public NvVideoSurfaceTexture(int texId)
    {
        m_texId = texId;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        notifyFrameAvailable(m_texId);
    }

    private static native void notifyFrameAvailable(int texId);
}

