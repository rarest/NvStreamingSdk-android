//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 8. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import com.meicam.sdk.NvsFx;
import com.meicam.sdk.NvsFxDescription;
/*!
    \brief 视频特效

    视频特效是显示在视频片段上的特效，能够改变视频图像整体或者局部的颜色、亮度、透明度等，使视频显示出特殊的效果。在视频片段(Video Clip)上，可以添加、移除、获取多个视频特效。
 */
public class NvsVideoFx extends NvsFx
{
/*!
    \brief 获取视频特效索引
    \return 返回视频特效的索引号
*/
    public int getIndex()
    {
        return nativeGetIndex(m_internalObject);
    }

/*!
    \brief 获取视频特效描述
    \return 返回视频特效的描述
*/
    public NvsFxDescription getDescription()
    {
        return nativeGetDescription(m_internalObject);
    }

    private native int nativeGetIndex(long internalObject);
    private native NvsFxDescription nativeGetDescription(long internalObject);
}
