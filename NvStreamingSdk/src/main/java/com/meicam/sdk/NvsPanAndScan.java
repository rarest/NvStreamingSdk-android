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
    \brief 摇摄和扫描类
*/
public class NvsPanAndScan {

    public float pan;  //!< \if ENGLISH Numerator \else 摇摄 \endif
    public float scan; //!< \if ENGLISH Numerator \else 扫描 \endif

    public NvsPanAndScan(float pan, float scan)
    {
        this.pan = pan;
        this.scan = scan;
    }
}
