//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2014. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Jan 13. 2015
//   Author:        NewAuto video team
//================================================================================
package com.cdv.utils;

import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.content.Context;
import android.util.Log;


public class NvAndroidDisplayListener implements DisplayManager.DisplayListener
{
    private int m_id = 0;

    private NvAndroidDisplayListener(int id)
    {
        m_id = id;
    }

    public boolean Register(Context ctx)
    {
        if (ctx == null)
            return false;

        Object obj = ctx.getSystemService(Context.DISPLAY_SERVICE);
        if (obj == null)
            return false;

        DisplayManager displayMgr = (DisplayManager)obj;

        try {
            displayMgr.registerDisplayListener(this, new Handler(Looper.getMainLooper()));
        } catch (Exception e) {
            Log.e("NvAndroidDisplayListener", "" + e.getMessage());
            return false;
        }

        return true;
    }

    public void Unregister(Context ctx)
    {
        if (ctx == null)
            return;

        Object obj = ctx.getSystemService(Context.DISPLAY_SERVICE);
        if (obj == null)
            return;

        DisplayManager displayMgr = (DisplayManager)obj;

        displayMgr.unregisterDisplayListener(this);
    }

    @Override
    public void onDisplayAdded(int displayId)
    {
    }

    @Override
    public void onDisplayChanged(int displayId)
    {
        notifyDisplayChanged(m_id, displayId);
    }

    @Override
    public void onDisplayRemoved(int displayId)
    {
    }

    private static native void notifyDisplayChanged(int id, int displayId);
}

