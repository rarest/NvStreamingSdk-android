//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2014. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Jul 24. 2014
//   Author:        NewAuto video team
//================================================================================
package com.cdv.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.util.Log;


public class NvAndroidHandler implements Handler.Callback
{
    private Handler m_handler = null;
    private int m_id = 0;

    private NvAndroidHandler(int id, Looper looper)
    {
        try {
            if (looper == null)
                m_handler = new Handler(this);
            else
                m_handler = new Handler(looper, this);
        } catch (Exception e) {
            Log.e("NvAndroidHandler", "" + e.getMessage());
        }

        m_id = id;
    }

    public boolean sendMessage(int what, int arg1, int arg2)
    {
        if (m_handler == null)
            return false;

        Message msg = Message.obtain();
        if (msg == null)
            return false;

        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        return m_handler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        notifyHandlerMessage(m_id, msg.what, msg.arg1, msg.arg2);
        return true;
    }

    private static native void notifyHandlerMessage(int id, int what, int arg1, int arg2);
}

