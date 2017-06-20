//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Apr 26. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import android.graphics.Bitmap;

/*!
 *  \brief 视频帧提取
 *
 *  视频帧提取类，可以获取某一时刻的原始视频帧图像。
 *  \since 1.2.0
 */
public class NvsVideoFrameRetriever {
    /*! \anchor VIDEO_FRAME_HEIGHT_GRADE */
    /*! @name 生成提取视频帧的高度级别 */
    //!@{
    public static final int VIDEO_FRAME_HEIGHT_GRADE_360 = 0;   //!< \if ENGLISH \else 生成视频帧高度360像素 \endif
    public static final int VIDEO_FRAME_HEIGHT_GRADE_480 = 1;   //!< \if ENGLISH \else 生成视频帧高度480像素 \endif
    public static final int VIDEO_FRAME_HEIGHT_GRADE_720 = 2;   //!< \if ENGLISH \else 生成视频帧高度720像素 \endif

    //!@}
    String m_videoFilePath;
    /*!
     *  \brief 获取某一时刻视频帧图像
     *  \param time 获取视频帧的时间值(单位微秒)
     *  \param videoFrameHeightGrade 生成视频帧图像的高度级别。具体参见[生成提取视频帧的高度级别] (@ref VIDEO_FRAME_HEIGHT_GRADE)
     *  \return 返回Bitmap对象，表示某一时刻视频帧图像
     */
    public Bitmap getFrameAtTime(long time, int videoFrameHeightGrade)
    {
        if (m_videoFilePath == null || m_videoFilePath.isEmpty())
            return null;
        return nativeGetFrameAtTime(m_videoFilePath, time, videoFrameHeightGrade);
    }

    private native Bitmap nativeGetFrameAtTime(String videoFilePath, long time, int videoFrameHeightGrade);
}
