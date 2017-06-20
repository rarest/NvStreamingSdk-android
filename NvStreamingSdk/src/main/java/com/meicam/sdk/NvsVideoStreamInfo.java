//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 3. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import com.meicam.sdk.NvsRational;
/*!
    \brief 视频流信息
*/
public class NvsVideoStreamInfo
{
    /*! \anchor VIDEO_ROTATION */
    /*! @name 视频旋转角度类型(顺时针方向) */
    //!@{
    public static final int VIDEO_ROTATION_0 = 0;
    public static final int VIDEO_ROTATION_90 = 1;
    public static final int VIDEO_ROTATION_180 = 2;
    public static final int VIDEO_ROTATION_270 = 3;

    //!@}

    public long duration; //!< \if ENGLISH \else 视频时长 \endif
    public int imageWidth;//!< \if ENGLISH \else 图像宽度 \endif
    public int imageHeight;//!< \if ENGLISH \else 图像高度 \endif
    public NvsRational pixelAspectRatio;//!< \if ENGLISH \else 像素比 \endif
    public NvsRational frameRate;//!< \if ENGLISH \else 帧速率 \endif
    public int displayRotation;//!< \if ENGLISH \else 显示的视频旋转角度。请参见[VIDEO_ROTATION_*] (@ref VIDEO_ROTATION) \endif
}
