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

import com.meicam.sdk.NvsAudioFx;
/*!
 *  \brief 音频片段，对音频文件的描述
 *
 *  音频片段既可以修改其裁剪入点和出点，播放速度等，还可以添加、插入、移除以及获取多个音频特效(Audio Fx)。
 */
public class NvsAudioClip extends NvsClip
{
    /*!
     *  \brief 在音频片段尾部追加音频特效
     *  \param fxName 音频特效名称。获取特效名称，请参照[getAllBuiltinAudioFxNames()](@ref com.meicam.sdk.NvsStreamingContext.getAllBuiltinAudioFxNames)或[内建特效名称列表] (\ref FxNameList.md)
     *  \return 返回追加的音频特效对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa insertFx
        \sa removeFx
        \sa getFxByIndex
     */
    public NvsAudioFx appendFx(String fxName)
    {
        return nativeAppendFx(m_internalObject, fxName);
    }
    /*!
     *  \brief 在音频片段上指定特效索引处插入音频特效
     *  \param fxName 音频特效名称。获取特效名称，请参照[getAllBuiltinAudioFxNames()](@ref com.meicam.sdk.NvsStreamingContext.getAllBuiltinAudioFxNames)或[内建特效名称列表] (\ref FxNameList.md)
     *  \param fxIndex 插入音频特效索引
     *  \return 返回插入的音频特效对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa appendFx
        \sa removeFx
        \sa getFxByIndex
     */
    public NvsAudioFx insertFx(String fxName, int fxIndex)
    {
        return nativeInsertFx(m_internalObject, fxName, fxIndex);
    }
    /*!
     *  \brief 移除指定索引的音频特效
     *  \param fxIndex 音频特效索引
     *  \return 判断是否移除音频特效成功。返回true则移除成功，false则失败。
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa appendFx
        \sa insertFx
        \sa getFxByIndex
     */
    public boolean removeFx(int fxIndex)
    {
        return nativeRemoveFx(m_internalObject, fxIndex);
    }
    /*!
     *  \brief 通过索引获取音频特效
     *  \param fxIndex 音频特效索引
     *  \return 返回获取的音频特效对象
     *  \sa appendFx
     *  \sa insertFx
     *  \sa removeFx
     */
    public NvsAudioFx getFxByIndex(int fxIndex)
    {
        return nativeGetFxByIndex(m_internalObject, fxIndex);
    }

    private native NvsAudioFx nativeAppendFx(long internalObject, String fxName);
    private native NvsAudioFx nativeInsertFx(long internalObject, String fxName, int fxIndex);
    private native boolean nativeRemoveFx(long internalObject, int fxIndex);
    private native NvsAudioFx nativeGetFxByIndex(long internalObject, int fxIndex);
}
