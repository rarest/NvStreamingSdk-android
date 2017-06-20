//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 9. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import com.meicam.sdk.NvsFx;

/*!
 *  \brief 采集视频特效
 *
 *  采集视频特效是一种应用在视频采集时的特效。获取流媒体上下文(Streaming Context)实例后，可按内建方式，包裹方式，美颜方式来添加或移除多个采集视频特效。
 */
public class NvsCaptureVideoFx extends NvsFx
{
    /*!
     *  \brief 获取视频采集特效索引
     *  \return 返回获取的视频采集特效索引值
     */

    public int getIndex()
    {
        return nativeGetIndex(m_internalObject);
    }

    private native int nativeGetIndex(long internalObject);
}

