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
 *  \brief 片段，音视频文件的具体描述
 *
 *  片段是容纳音视频内容的实体，是对视频、音频文件的描述，分为音频片段(Audio Clip)和视频片段(Video Clip)。它定义了不同类型片段所拥有的共同属性和行为，
 *  即派生的音频片段和和视频片段可根据需要修改各自的裁剪出入点，左右声道，播放速度等。在SDK框架中，在轨道(Track)上可添加相应的音频片段和视频片段。
 */

public class NvsClip extends NvsObject
{

/*! \anchor CLIP_TYPE */
/*! @name 片段类型 */
//!@{
    public static final int CLIP_TYPE_VIDEO = 0;  //!< \if ENGLISH brief element description \else 视频片段类型 \endif
    public static final int CLIP_TYPE_AUDIO = 1;  //!< \if ENGLISH brief element description \else 音频片段类型 \endif

//!@}

    /*!
     *  \brief 获取片段的裁剪入点(单位微秒)
     *  \return 返回片段的裁剪入点
        \sa getTrimOut
        \sa changeTrimInPoint
        \sa getInPoint
     */
    public long getTrimIn()
    {
        return nativeGetTrimIn(m_internalObject);
    }

    /*!
     *  \brief 获取片段的裁剪出点(单位微秒)
     *  \return 返回片段的裁剪出点
        \sa getTrimIn
        \sa changeTrimOutPoint
        \sa getOutPoint
     */
    public long getTrimOut()
    {
        return nativeGetTrimOut(m_internalObject);
    }
    /*!
     *  \brief 获取片段在时间线上的入点
     *  \return 返回片段在时间线上的入点值
        \sa getOutPoint
        \sa getTrimIn
     */
    public long getInPoint()
    {
       return nativeGetInPoint(m_internalObject);
    }

    /*!
     *  \brief 获取片段在时间线上的出点
     *  \return 返回片段在时间线上的出点值
        \sa getInPoint
        \sa getTrimOut
     */
    public long getOutPoint()
    {
        return nativeGetOutPoint(m_internalObject);
    }

    /*!
     *  \brief 获取片段类型（视频片段，音频片段）
     *  \return 返回片段类型值。
     *
     *  返回值是CLIP_TYPE打头的静态int属性值。包括两种片段类型，即音频片段类型和视频片段类型。请参见[片段类型](@ref CLIP_TYPE)
     */
    public int getType()
    {
        return nativeGetType(m_internalObject);
    }

    /*!
     *  \brief 获取片段在轨道上的索引
     *  \return 返回片段在轨道上的索引值
     */
    public int getIndex()
    {
        return nativeGetIndex(m_internalObject);
    }

    /*!
     *  \brief 获取片段文件路径
     *  \return 返回片段路径的字符串
     */
    public String getFilePath()
    {
        return nativeGetFilePath(m_internalObject);
    }

    /*!
     *  \brief 获取片段上的特效数量。注：片段上的特效索引是从0开始
     *  \return 返回片段上的特效数
     */
    public int getFxCount()
    {
        return nativeGetFxCount(m_internalObject);
    }

    /*!
     *  \brief 修改片段的裁剪入点(单位微秒)
     *  \param newTrimInPoint 新的裁剪入点
     *  \param affectSibling 是否影响同轨道上其他片段(true/false)
     *  \return 返回实际可到达的裁剪入点。注意：实际可达到的裁剪入点范围在[0,trimOut - 1]
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa changeTrimOutPoint
        \sa getTrimIn
     */
    public long changeTrimInPoint(long newTrimInPoint, boolean affectSibling)
    {
        return nativeChangeTrimInPoint(m_internalObject, newTrimInPoint, affectSibling);
    }

    /*!
     *  \brief 修改片段的裁剪出点(单位微秒)
     *  \param newTrimOutPoint 新的裁剪出点
     *  \param affectSibling 是否影响同轨道上其他片段(true/false)
     *  \return 返回实际可到达的裁剪出点。注意：实际可达到的裁剪出点范围在[trimIn + 1,clipDuration],clipDuration为片段时长。
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa changeTrimInPoint
        \sa getTrimOut
     */
    public long changeTrimOutPoint(long newTrimOutPoint, boolean affectSibling)
    {
        return nativeChangeTrimOutPoint(m_internalObject, newTrimOutPoint, affectSibling);
    }

    /*!
     *  \brief 获取片段的播放速度。
     *
     *  默认值为1，表示按正常速度播放;小于1的值表示慢放;大于1的值表示快放
     *  \return 返回当前片段的播放速度
        \sa changeSpeed
     */
    public double getSpeed()
    {
        return nativeGetSpeed(m_internalObject);
    }

    /*!
     *  \brief 改变片段的播放速度
     *  \param newSpeed 新的播放速度
     *  \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
        \sa getSpeed
     */
    public void changeSpeed(double newSpeed)
    {
        nativeChangeSpeed(m_internalObject, newSpeed);
    }

    /*!
     *  \brief 设置音量
     *  \param leftVolumeGain 左声道
     *  \param rightVolumeGain 右声道
     *  \sa getVolumeGain
    */
    public void setVolumeGain(float leftVolumeGain, float rightVolumeGain)
    {
        nativeSetVolumeGain(m_internalObject, leftVolumeGain, rightVolumeGain);
        return;
    }
    /*!
     *  \brief 获取音量
     *  \return 返回获取的音量对象
     *  \sa getVolumeGain
    */
    public NvsVolume getVolumeGain()
    {
        return nativeGetVolumeGain(m_internalObject);
    }

    private native long nativeGetTrimIn(long internalObject);
    private native long nativeGetTrimOut(long internalObject);
    private native long nativeGetInPoint(long internalObject);
    private native long nativeGetOutPoint(long internalObject);
    private native int nativeGetType(long internalObject);
    private native int nativeGetIndex(long internalObject);
    private native String nativeGetFilePath(long internalObject);
    private native int nativeGetFxCount(long internalObject);
    private native long nativeChangeTrimInPoint(long internalObject, long newTrimInPoint, boolean affectSibling);
    private native long nativeChangeTrimOutPoint(long internalObject, long newTrimOutPoint, boolean affectSibling);
    private native double nativeGetSpeed(long internalObject);
    private native void nativeChangeSpeed(long internalObject, double newSpeed);
    private native void nativeSetVolumeGain(long internalObject, float leftVolumeGain, float rightVolumeGain);
    private native NvsVolume nativeGetVolumeGain(long internalObject);
}
