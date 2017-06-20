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

/*!
    \brief 比例值
*/
public class NvsRational
{
    public int num;   //!< \if ENGLISH Numerator \else 分子 \endif
    public int den;   //!< \if ENGLISH Denominator \else 分母 \endif

/*!
    \brief 构造函数，设置比例值的分子和分母
*/
    public NvsRational(int n, int d)
    {
        num = n;
        den = d;
    }
}

