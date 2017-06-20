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

import com.meicam.sdk.NvsVideoStreamInfo;
import com.meicam.sdk.NvsAudioStreamInfo;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSize;
/*!
 *  \brief 音视频文件信息
 *
 *  显示音视频文件的信息，包括音视频文件的时长，数据速率，像素横纵比，音视频流数目等。
 *
 *  音视频文件信息
 */
public class NvsAVFileInfo
{   
    /*! \anchor AV_FILE_TYPE */
    /*! @name 文件类型 */
    //!@{
    public static final int AV_FILE_TYPE_UNKNOWN = -1;    //!< 未知类型
    public static final int AV_FILE_TYPE_AUDIOVIDEO = 0;  //!< 视频类型
    public static final int AV_FILE_TYPE_AUDIO = 1;       //!< 音频类型
    public static final int AV_FILE_TYPE_IMAGE = 2;       //!< 图片类型

    //!@}
    static final int AUDIO_MAX_STREAM_COUNT = 4;

    int m_type;
    int m_numVideoStreams;
    int m_numAudioStreams;
    long m_duration;
    long m_dataRate;
    NvsVideoStreamInfo m_videoStreamInfo;
    NvsAudioStreamInfo[] m_audioStreamInfo = new NvsAudioStreamInfo[AUDIO_MAX_STREAM_COUNT];
/*!
    \brief 获取文件的类型
    \return 返回获取的[文件类型] (@ref AV_FILE_TYPE)
*/
    public int getAVFileType()
    {
        return m_type;
    }
/*!
    \brief 获取文件的时长
    \return 返回文件的时长(单位微秒)
*/
    public long getDuration()
    {
        return m_duration;
    }
/*!
    \brief 获取文件的数据速率
    \return 返回文件的数据速率
*/
    public long getDataRate()
    {
        return m_dataRate;
    }
/*!
    \brief 获取文件的视频流数量
    \return 返回文件的视频流数量
*/
    public int getVideoStreamCount()
    {
        return m_numVideoStreams;
    }
/*!
    \brief 获取文件的音频流数量
    \return 返回文件的音频流数量
*/
    public int getAudioStreamCount()
    {
        return m_numAudioStreams;
    }
/*!
 *  \brief 通过视频流索引获取当前视频流的时长
 *  \param videoStreamIndex 视频流索引
 *  \return 返回当前视频流的时长
    \sa getAudioStreamDuration
 */
    public long getVideoStreamDuration(int videoStreamIndex)
    {
        if (videoStreamIndex < m_numVideoStreams)
            return m_videoStreamInfo.duration;
        return 0;
    }
/*!
 *  \brief 通过视频流索引获取当前视频流的尺寸
 *  \param videoStreamIndex 视频流索引
 *  \return 返回值为NvsSize对象，表示视频流的尺寸(宽度与高度)
 */
    public NvsSize getVideoStreamDimension(int videoStreamIndex)
    {
        NvsSize sz = new NvsSize(0, 0);
        if (videoStreamIndex < m_numVideoStreams) {
            sz.width = m_videoStreamInfo.imageWidth;
            sz.height = m_videoStreamInfo.imageHeight;
        }
        return sz;
    }

/*!
 *  \brief 通过视频流索引获取当前视频流的像素横纵比
 *  \param videoStreamIndex 视频流索引
 *  \return 返回NvsRational对象，表示当前视频流的像素横纵比
 */
    public NvsRational getVideoStreamPixelAspectRatio(int videoStreamIndex)
    {
        NvsRational par = new NvsRational(1, 1);
        if (videoStreamIndex < m_numVideoStreams) {
            par.num = m_videoStreamInfo.pixelAspectRatio.num;
            par.den = m_videoStreamInfo.pixelAspectRatio.den;
        }
        return par;
    }

/*!
 *  \brief 通过视频流索引获取当前视频流的帧速率
 *  \param videoStreamIndex 视频流索引
 *  \return 返回NvsRational对象，表示当前视频流的帧速率
 */
    public NvsRational getVideoStreamFrameRate(int videoStreamIndex)
    {
        NvsRational fps = new NvsRational(1, 1);
        if (videoStreamIndex < m_numVideoStreams) {
            fps.num = m_videoStreamInfo.frameRate.num;
            fps.den = m_videoStreamInfo.frameRate.den;
        }
        return fps;
    }
/*!
 *  \brief 通过视频流索引获取当前视频流的旋转角度类型
 *  \param videoStreamIndex 视频流索引
 *  \return 返回当前视频流的旋转角度类型值。请参见[视频旋转角度类型] (@ref VIDEO_ROTATION)
 */
    public int getVideoStreamRotation(int videoStreamIndex)
    {
        if (videoStreamIndex < m_numVideoStreams)
            return m_videoStreamInfo.displayRotation;
        return NvsVideoStreamInfo.VIDEO_ROTATION_0;
    }

/*!
 *  \brief 通过音频流索引获取当前音频流的时长
 *  \param audioStreamIndex 音频流索引
 *  \return 返回当前音频流的时长
    \sa getVideoStreamDuration
 */
    public long getAudioStreamDuration(int audioStreamIndex)
    {
        if (audioStreamIndex < m_numAudioStreams)
            return m_audioStreamInfo[audioStreamIndex].duration;
        return 0;
    }
/*!
 *  \brief 通过音频流索引获取当前音频流的采样率
 *  \param audioStreamIndex 音频流索引
 *  \return 返回当前音频流的采样率
 */
    public int getAudioStreamSampleRate(int audioStreamIndex)
    {
        if (audioStreamIndex < m_numAudioStreams)
            return m_audioStreamInfo[audioStreamIndex].sampleRate;
        return 0;
    }
/*!
 *  \brief 通过音频流索引获取当前音频流的声道数
 *  \param audioStreamIndex 音频流索引
 *  \return 返回当前音频流的声道数
 */
    public int getAudioStreamChannelCount(int audioStreamIndex)
    {
        if (audioStreamIndex < m_numAudioStreams)
            return m_audioStreamInfo[audioStreamIndex].channelCount;
        return 0;
    }

    void setAudioStreamInfo(int audioStreamIndex, NvsAudioStreamInfo audioStreamInfo)
    {
        if (audioStreamIndex < m_numAudioStreams)
            m_audioStreamInfo[audioStreamIndex] = audioStreamInfo;
    }
}
