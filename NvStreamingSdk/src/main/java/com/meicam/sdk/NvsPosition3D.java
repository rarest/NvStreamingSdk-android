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
    \brief 三维坐标结构
*/
public class NvsPosition3D
{
    public float x;
    public float y;
    public float z;

/*!
    \brief 构造函数，设置三维坐标结构的x,y,z坐标
*/
    public NvsPosition3D(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
