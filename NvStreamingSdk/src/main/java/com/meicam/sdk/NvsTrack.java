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
     \brief 轨道，容纳片段的结构

     轨道可视作片段的集合,分为音频轨道(Audio Track)和视频轨道(Video Track)。创建时间线实例后，可添加或移除多条轨道。在每一条轨道上，可以添加多个要编辑的视音频片段，并对片段进行音量设置，也可以进行移除和位置移动。
 */
public class NvsTrack extends NvsObject
{

/*! \anchor TRACK_TYPE */
/*! @name 轨道类型 */
//!@{
    public static final int TRACK_TYPE_VIDEO = 0;  //!< 视频轨道
    public static final int TRACK_TYPE_AUDIO = 1;  //!< 音频轨道

//!@}

/*!
    \brief 获取轨道类型
    \return 返回[轨道类型] (@ref TRACK_TYPE)
 */
    public int getType()
    {
        return nativeGetType(m_internalObject);
    }

/*!
    \brief 获取轨道索引
    \return 返回轨道索引号
 */
    public int getIndex()
    {
        return nativeGetIndex(m_internalObject);
    }

/*!
    \brief 获取轨道长度(单位微秒)
    \return 返回轨道的长度
 */
    public long getDuration()
    {
        return nativeGetDuration(m_internalObject);
    }

/*!
    \brief 获取轨道上的片段数量
    \return 返回轨道上的片段数量
 */
    public int getClipCount()
    {
        return nativeGetClipCount(m_internalObject);
    }

/*!
    \brief 分割指定的片段
    \param clipIndex 片段索引
    \param splitPoint 分割点
    \return 判断是否分割成功。返回true则分割成功，false则不成功

    分割片段，即对指定索引值的片段进行分割而变为两个片段的操作，对应的轨道上片段的索引值也会进行相应变化。

    示例如下:

    ![] (@ref TrackClip.PNG)
    上图中轨道上有三个视频片段C1、C2、C3，对片段C2进行分割，分割后的片段分别命名为C2和C4。通过获取轨道上当前片段数来判定是否分割成功，分割成功则C2和C4索引值对应为1和2。

    结果如下图：
    ![] (@ref afterSplitClip.PNG)
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeClip
 */
    public boolean splitClip(int clipIndex, long splitPoint)
    {
        return nativeSplitClip(m_internalObject, clipIndex, splitPoint);
    }

/*!
    \brief 移除指定的片段
    \param clipIndex 片段索引
    \param keepSpace 片段移除后，是否保留该片段在轨道上的空间。值为true则保留，false则不保留
    \return 判断是否移除成功。返回true则移除成功，false则移除不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeAllClips
 */
    public boolean removeClip(int clipIndex, boolean keepSpace)
    {
       return nativeRemoveClip(m_internalObject, clipIndex, keepSpace);
    }

/*!
    \brief 移除指定的区间内的所有片段，如果片段只有部分与该区间重合则调整其时间线入点或者出点
    \param startTimelinePos 区间的起始时间线位置(微秒)
    \param endTimelinePos 区间的结束时间线位置(微秒)
    \param keepSpace 区间内的片段移除后，是否保留该区间所占轨道上的空间。值为true则保留，false则不保留
    \return 是否移除成功。返回true则移除成功，false则移除不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 */
    public boolean removeRange(long startTimelinePos, long endTimelinePos, boolean keepSpace)
    {
        return nativeRemoveRange(m_internalObject, startTimelinePos, endTimelinePos, keepSpace);
    }

/*!
    \brief 移动指定的片段
    \param clipIndex 片段索引
    \param destClipIndex 片段移动的目标索引
    \return 判断是否移动成功。返回true则移动成功，false则移动不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 */
    public boolean moveClip(int clipIndex, int destClipIndex)
    {
        return nativeMoveClip(m_internalObject, clipIndex, destClipIndex);
    }

/*!
    \brief 移除所有片段
    \return 判断是否移除成功。返回true则移除成功，false则移除不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeClip
 */
    public boolean removeAllClips()
    {
        return nativeRemoveAllClips(m_internalObject);
    }

    /* unused */
    /*public boolean removeRegionClips(long inPoint, long outPoint)
    {
        return nativeRemoveRegionClips(m_internalObject, inPoint, outPoint)
    }
    */

/*!
    \brief 设置音量
    \param leftVolumeGain 左声道
    \param rightVolumeGain 右声道
    \sa getVolumeGain
*/
    public void setVolumeGain(float leftVolumeGain, float rightVolumeGain)
    {
        nativeSetVolumeGain(m_internalObject, leftVolumeGain, rightVolumeGain);
    }
/*!
    \brief 获取音量
    \return 返回获取的音量对象
    \sa setVolumeGain
 */
    public NvsVolume getVolumeGain()
    {
       return nativeGetVolumeGain(m_internalObject);
    }

    private native int nativeGetType(long internalObject);
    private native int nativeGetIndex(long internalObject);
    private native long nativeGetDuration(long internalObject);
    private native int nativeGetClipCount(long internalObject);
    private native boolean nativeSplitClip(long internalObject, int clipIndex, long splitPoint);
    private native boolean nativeRemoveClip(long internalObject, int clipIndex, boolean keepSpace);
    private native boolean nativeRemoveRange(long internalObject, long startTimelinePos, long endTimelinePos, boolean keepSpace);
    private native boolean nativeMoveClip(long internalObject, int clipIndex, int destClipIndex);
    private native boolean nativeRemoveAllClips(long internalObject);
    private native void nativeSetVolumeGain(long internalObject, float leftVolumeGain, float rightVolumeGain);
    private native NvsVolume nativeGetVolumeGain(long internalObject);
}
