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
    \brief 视频解析度
*/
public class NvsVideoResolution
{
    public int imageWidth;    //!< \if ENGLISH \else 图像宽度 \endif
    public int imageHeight;   //!< \if ENGLISH \else 图像高度 \endif
    public NvsRational imagePAR; //!< \if ENGLISH \else 像素比（仅支持1:1）\endif
}

