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
 *  \brief 音频转场,音频片段间切换的特效
 *
 *  一般通过音频轨道(Audio Track)来设置和获取音频转场。目前默认音频转场是淡入淡出转场(fade)。
 */

public class NvsAudioTransition extends NvsFx
{
    /*!
    *  \brief 获取音频特效转场描述
    *  \return 返回音频特效转场描述对象
    */
    public NvsFxDescription getDescription()
    {
        return nativeGetDescription(m_internalObject);
    }

    private native NvsFxDescription nativeGetDescription(long internalObject);
}
