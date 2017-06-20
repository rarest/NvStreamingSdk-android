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

import com.meicam.sdk.NvsObject;

/*!
 *  \brief 音频轨道，音频片段的集合
 *
 *  音频轨道是容纳音频片段的实体。每条音频轨道可以添加或者移除多个音频片段。一个音频片段播放到另一个音频片段时，需要进行音频转场设置，以便过渡衔接。
 *
 *  注：对于音频轨道的一系列接口及所其属参数含义,请参照视频轨道[NvsVideoTrack] (@ref com.meicam.sdk.NvsVideoTrack)的对应接口来对照理解。
 */
public class NvsAudioTrack extends NvsTrack
{

    /*!
     *  \brief 在轨道尾部追加片段
     *  \param filePath 片段路径。对于片段路径方式，请参见NvsVideoTrack的接口[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     *  \return 返回追加的音频片段对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa insertClip
        \sa getClipByIndex
     */
    public NvsAudioClip appendClip(String filePath)
    {
        return insertClip(filePath, getClipCount());
    }
    /*!
     *  \brief 在轨道尾部追加片段
     *  \param filePath 片段路径。对于片段路径方式，请参见NvsVideoTrack的接口[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     *  \param trimIn 片段的裁剪入点
     *  \param trimOut 片段的裁剪出点
     *  \return 返回追加的音频片段对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa insertClip
        \sa getClipByIndex
     */
    public NvsAudioClip appendClip(String filePath, long trimIn, long trimOut)
    {
        return insertClip(filePath, trimIn, trimOut, getClipCount());
    }

    /*!
     *  \brief 在轨道上指定片段索引处插入片段
     *  \param filePath 片段路径。对于片段路径方式，请参见NvsVideoTrack的接口[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     *  \param clipIndex 插入片段索引
     *  \return 返回插入的音频片段对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     *  \sa appendClip
     *  \sa getClipByIndex
     */
    public NvsAudioClip insertClip(String filePath, int clipIndex)
    {
        return nativeInsertClip(m_internalObject, filePath, clipIndex);
    }

    /*!
     *  \brief 在轨道上指定片段索引处插入片段
     *  \param filePath 片段路径。对于片段路径方式，请参见NvsVideoTrack的接口[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     *  \param trimIn 片段的裁剪入点
     *  \param trimOut 片段的裁剪出点
     *  \param clipIndex 插入片段索引
     *  \return 返回插入的音频片段对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     *  \sa appendClip
     *  \sa getClipByIndex
     */
    public NvsAudioClip insertClip(String filePath, long trimIn, long trimOut, int clipIndex)
    {
        return nativeInsertClip(m_internalObject, filePath, trimIn, trimOut, clipIndex);
    }

/*!
     \brief 添加音频片段
     \param filePath 音频片段路径。对于片段路径方式，请参见NvsVideoTrack的接口[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     \param inPoint 要插入的音频片段在时间线上的入点
     \return 返回添加的音频片段对象
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa appendClip
     \sa insertClip
 */
    public NvsAudioClip addClip(String filePath, long inPoint)
    {
        return nativeAddClip(m_internalObject, filePath, inPoint);
    }

/*!
     \brief 添加音频片段
     \param filePath 音频片段路径。对于片段路径方式，请参见NvsVideoTrack的接口[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     \param inPoint 要插入的音频片段在时间线上的入点
     \param trimIn 音频片段的裁剪入点
     \param trimOut 音频片段的裁剪出点
     \return 返回添加的音频片段对象
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa appendClip
     \sa insertClip
 */
    public NvsAudioClip addClip(String filePath, long inPoint, long trimIn, long trimOut)
    {
        return nativeAddClip(m_internalObject, filePath, inPoint, trimIn, trimOut);
    }

    /*!
     *  \brief 通过索引获取片段
     *  \param clipIndex 片段索引
     *  \return 返回获取的音频片段对象
     *  \sa getClipByTimelinePosition
     *  \sa appendClip
     *  \sa insertClip
     */
    public NvsAudioClip getClipByIndex(int clipIndex)
    {
        return nativeGetClipByIndex(m_internalObject, clipIndex);
    }

    /*!
         \brief 通过时间获取片段
         \param timelinePos 时间线上的位置(微秒)
         \return 返回获取的音频片段对象
         \sa getClipByIndex
     */
    public NvsAudioClip getClipByTimelinePosition(long timelinePos)
    {
        return nativeGetClipByTimelinePosition(m_internalObject, timelinePos);
    }

    /*!
     *  \brief 设置内嵌式转场
     *  \param srcClipIndex 转场的源片段索引
     *  \param transitionName 音频转场名称。注意：目前音频转场只支持淡入淡出(Fade)模式；如果设为空字符串，则删除原有转场
     *  \return 返回音频转场对象
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     *  \sa getTransitionWithSourceClipIndex
     */
    public NvsAudioTransition setBuiltinTransition(int srcClipIndex, String transitionName)
    {
        return nativeSetBuiltinTransition(m_internalObject, srcClipIndex, transitionName);
    }
    /*!
     *  \brief 通过源片段索引获取音频转场
     *  \param srcClipIndex 源片段索引
     *  \return 返回获取的音频转场对象
     *  \sa setBuiltinTransition
     */
    public NvsAudioTransition getTransitionWithSourceClipIndex(int srcClipIndex)
    {
        return nativeGetTransitionWithSourceClipIndex(m_internalObject, srcClipIndex);
    }

    private native NvsAudioClip nativeInsertClip(long internalObject, String filePath, int clipIndex);
    private native NvsAudioClip nativeInsertClip(long internalObject, String filePath, long trimIn, long trimOut, int clipIndex);
    private native NvsAudioClip nativeAddClip(long internalObject, String filePath, long inPoint);
    private native NvsAudioClip nativeAddClip(long internalObject, String filePath, long inPoint, long trimIn, long trimOut);
    private native NvsAudioClip nativeGetClipByIndex(long internalObject, int clipIndex);
    private native NvsAudioClip nativeGetClipByTimelinePosition(long internalObject, long timelinePos);
    private native NvsAudioTransition nativeSetBuiltinTransition(long internalObject, int srcClipIndex, String transitionName);
    private native NvsAudioTransition nativeGetTransitionWithSourceClipIndex(long internalObject, int srcClipIndex);
}
