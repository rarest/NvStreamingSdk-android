//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2015. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Oct 13. 2014
//   Author:        NewAuto video team
//================================================================================
package com.cdv.io;

import android.os.SystemClock;
import android.util.Log;


//
// Helper class to simulate a win32 event sync object
//
public class NvSyncEvent
{
    private boolean m_manualReset = false;
    private volatile boolean m_signaled = false;

    public NvSyncEvent(boolean manualReset)
    {
        m_manualReset = manualReset;
    }

    public void setEvent()
    {
        synchronized(this) {
            if (!m_signaled) {
                m_signaled = true;
                this.notifyAll();
            }
        }
    }

    public void resetEvent()
    {
        synchronized(this) {
            m_signaled = false;
        }
    }

    // Negative time value means wait forever until event signaled
    // 0 time value means check whether the event is signaled right now
    // Positive time means wait expiration time
    public boolean waitEvent(long time)
    {
        if (time == 0) {
            synchronized(this) {
                return m_signaled;
            }
        } else if (time > 0) {
            return waitEventWithTimeout(time);
        }

        try {
            synchronized(this) {
                while (!m_signaled)
                    this.wait();

                if (!m_manualReset)
                    m_signaled = false;
            }

            return true;
        } catch (Exception e) {
            Log.e("SyncEvent", "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean waitEventWithTimeout(long time)
    {
        try {
            synchronized(this) {
                if (m_signaled) {
                    if (!m_manualReset)
                        m_signaled = false;
                    return true;
                }

                long remainTime = time;
                long timeStart = SystemClock.elapsedRealtime();
                while (!m_signaled) {
                    this.wait(remainTime);
                    // Check how many millisecondes have passed
                    final long timeEnd = SystemClock.elapsedRealtime();
                    final long timePassed = timeEnd - timeStart;
                    if (timePassed >= remainTime)
                        return false; // timed out!

                    remainTime -= timePassed;
                    timeStart = timeEnd;
                }

                if (!m_manualReset)
                    m_signaled = false;
            }

            return true;
        } catch (Exception e) {
            Log.e("SyncEvent", "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

