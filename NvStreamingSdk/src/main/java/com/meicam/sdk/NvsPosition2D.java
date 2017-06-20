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
    \brief 二维坐标结构
*/
public class NvsPosition2D
{
    public float x;
    public float y;

/*!
    \brief 构造函数，设置二维坐标结构的横纵坐标
*/
    public NvsPosition2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}
