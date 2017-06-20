//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 2. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import android.util.Log;


public class NvsObject
{
    // NOTE: We don't hold a reference count of the C++ project object!
    protected long m_internalObject = 0;

    protected void setInternalObject(long internalObject)
    {
        m_internalObject = internalObject;
    }

    protected long getInternalObject()
    {
        return m_internalObject;
    }
}

