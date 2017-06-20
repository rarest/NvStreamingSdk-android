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
 *  \brief 音频特效
 *
 *  音频特效是叠加显示在音频片段上的特效，可以改变音频片段的声调和速率。获取音频片段(Audio Clip)对象实例后，根据需要来添加或者移除多个音频特效。
 */
public class NvsAudioFx extends NvsFx
{
    /*!
    *  \brief 获取音频特效索引
    *  \return 返回音频特效索引值
    */
    public int getIndex()
    {
        return nativeGetIndex(m_internalObject);
    }
    /*!
    *  \brief 获取音频特效描述
    *  \return 返回音频特效描述对象
    */
    public NvsFxDescription getDescription()
    {
        return nativeGetDescription(m_internalObject);
    }

    private native int nativeGetIndex(long internalObject);
    private native NvsFxDescription nativeGetDescription(long internalObject);
}
