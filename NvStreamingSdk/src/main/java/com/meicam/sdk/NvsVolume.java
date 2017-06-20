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

/*!
     \brief 音量类，设置音量的左右声道
*/
public class NvsVolume {

    public float leftVolume;  //!< 左声道
    public float rightVolume; //!< 右声道

/*!
    \brief 构造函数，初始化音量的左右声道
*/
    public NvsVolume(float left, float right)
    {
        leftVolume = left;
        rightVolume = right;
    }

}
