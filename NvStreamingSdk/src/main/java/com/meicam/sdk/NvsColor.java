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
    \brief 自定义颜色类

    SDK中，NvsColor类属性r,g,b,a取值范围是[0,1]，而非[0,255]。
 */
public class NvsColor
{
    public float r;
    public float g;
    public float b;
    public float a;

    public NvsColor(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
