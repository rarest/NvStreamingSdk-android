//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 8. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import com.meicam.sdk.NvsObject;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsPosition2D;
import com.meicam.sdk.NvsPosition3D;

/*!
 *  \brief 特效
 *
 *   特效类是视频特效(Video Fx)，音频特效(Audio Fx)，音频转场(Audio Transition)，视频转场(Video Transition)等不同类型特效的基类。在SDK框架中，特效是很关键的一部分，派生自NvsFx的每种不同类型的特效，或通过片段实例，或时间线实例，或轨道实例来添加，移除和获取。同时，特效类中提供了不同的API接口来设置和获取特效参数类型。
 */
public class NvsFx extends NvsObject {


/*!
    \brief 设置特效的整数参数值

    \param fxParam 特效的整数参数的类型。请参见[PARAM_TYPE_INT] (@ref PARAM_TYPE)
    \param val 整数
    \sa getIntVal
*/
    public void setIntVal(String fxParam, int val) {
        nativeSetIntVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效的整数参数值

   \param fxParam 特效的整数参数的类型。请参见[PARAM_TYPE_INT] (@ref PARAM_TYPE)

   \return 返回获得的整数值
   \sa setIntVal
*/
    public int getIntVal(String fxParam) {
        return nativeGetIntVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效的浮点值参数值

   \param fxParam 特效的浮点数参数的类型。请参见[PARAM_TYPE_FLOAT] (@ref PARAM_TYPE)
   \param val 浮点值
   \sa getFloatVal
*/
    public void setFloatVal(String fxParam, double val) {
        nativeSetFloatVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效浮点值参数值

   \param fxParam 特效的浮点数参数的类型。请参见[PARAM_TYPE_FLOAT] (@ref PARAM_TYPE)
   \return 返回获得的double值
   \sa setFloatVal
*/
    public double getFloatVal(String fxParam) {
        return nativeGetFloatVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效布尔值参数值

   \param fxParam 特效的布尔值参数的类型。请参见[PARAM_TYPE_BOOL] (@ref PARAM_TYPE)
   \param val 布尔值
   \sa getBooleanVal
*/
    public void setBooleanVal(String fxParam, boolean val) {
        nativeSetBooleanVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效的布尔值参数值

   \param fxParam 特效的布尔值参数的类型。请参见[PARAM_TYPE_BOOL] (@ref PARAM_TYPE)
   \return 返回获得的布尔值
   \sa setBooleanVal
*/
    public boolean getBooleanVal(String fxParam) {
        return nativeGetBooleanVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效字符串参数值

   \param fxParam 特效的字符串参数的类型。请参见[PARAM_TYPE_STRING] (@ref PARAM_TYPE)
   \param val 字符串
   \sa getStringVal
*/
    public void setStringVal(String fxParam, String val) {
        nativeSetStringVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效字符串参数值

   \param fxParam 特效的字符串参数的类型。请参见[PARAM_TYPE_STRING] (@ref PARAM_TYPE)
   \return 返回获得的字符串
   \sa setStringVal
*/
    public String getStringVal(String fxParam) {
        return nativeGetStringVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效颜色值参数值

   \param fxParam 特效的颜色参数的类型。请参见[PARAM_TYPE_COLOR] (@ref PARAM_TYPE)
   \param val [NvsColor] (@ref NvsColor)自定义颜色对象
   \sa getColorVal
*/
    public void setColorVal(String fxParam, NvsColor val)
    {
        nativeSetColorVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效颜色值参数值
   \param fxParam 特效的颜色参数的类型。请参见[PARAM_TYPE_COLOR] (@ref PARAM_TYPE)
   \return 返回获得的自定义颜色[NvsColor] (@ref NvsColor)对象
   \sa setColorVal
*/
    public NvsColor getColorVal(String fxParam)
    {
        return nativeGetColorVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效二维坐标参数值

   \param fxParam 特效的二维坐标参数的类型。请参见[PARAM_TYPE_POSITION2D] (@ref PARAM_TYPE)
   \param val 二维坐标[NvsPosition2D] (@ref NvsPosition2D)对象
   \sa getPosition2DVal
   \sa setPosition3DVal
*/
    public void setPosition2DVal(String fxParam, NvsPosition2D val)
    {
        nativeSetPosition2DVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效二维坐标参数值
   \param fxParam 特效的二维坐标参数的类型。请参见[PARAM_TYPE_POSITION2D] (@ref PARAM_TYPE)
   \return 返回获得的二维坐标[NvPosition2D] (@ref NvsPosition2D)对象
   \sa setPosition2DVal
   \sa getPosition3DVal
*/
    public NvsPosition2D getPosition2DVal(String fxParam)
    {
        return nativeGetPosition2DVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效三维坐标参数值
   \param fxParam 特效的三维坐标参数的类型。请参见[PARAM_TYPE_POSITION3D] (@ref PARAM_TYPE)
   \param val 三维坐标[NvsPosition3D] (@ref NvsPosition3D)对象
   \sa getPosition3DVal
   \sa setPosition2DVal
*/
    public void setPosition3DVal(String fxParam, NvsPosition3D val)
    {
        nativeSetPosition3DVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效三维坐标参数值
   \param fxParam 特效的三维坐标参数的类型。请参见[PARAM_TYPE_POSITION3D] (@ref PARAM_TYPE)
   \return 返回获得的三维坐标[NvsPosition3D] (@ref NvsPosition3D)对象
   \sa setPosition3DVal
   \sa getPosition2DVal
*/
    public NvsPosition3D getPosition3DVal(String fxParam)
    {
        return nativeGetPosition3DVal(getInternalObject(), fxParam);
    }

/*!
   \brief 设置特效菜单参数值

   \param fxParam 特效的菜单参数的类型。请参见[PARAM_TYPE_MENU] (@ref PARAM_TYPE)
   \param val 字符串
   \sa getMenuVal
*/
    public void setMenuVal(String fxParam, String val)
    {
        nativeSetMenuVal(getInternalObject(), fxParam, val);
    }

/*!
   \brief 获得特效菜单参数值
   \param fxParam 特效的菜单参数的类型。请参见[PARAM_TYPE_MENU] (@ref PARAM_TYPE)
   \return 返回获得的菜单
   \sa setMenuVal
*/
    public String getMenuVal(String fxParam)
    {
        return nativeGetMenuVal(getInternalObject(), fxParam);
    }

    private native void nativeSetIntVal(long internalObject, String fxParam, int val);
    private native int nativeGetIntVal(long internalObject, String fxParam);
    private native void nativeSetFloatVal(long internalObject, String fxParam, double val);
    private native double nativeGetFloatVal(long internalObject, String fxParam);
    private native void nativeSetBooleanVal(long internalObject, String fxParam, boolean val);
    private native boolean nativeGetBooleanVal(long internalObject, String fxParam);
    private native void nativeSetStringVal(long internalObject, String fxParam, String val);
    private native String nativeGetStringVal(long internalObject, String fxParam);
    private native void nativeSetColorVal(long internalObject, String fxParam, NvsColor val);
    private native NvsColor nativeGetColorVal(long internalObject, String fxParam);
    private native void nativeSetPosition2DVal(long internalObject, String fxParam, NvsPosition2D val);
    private native NvsPosition2D nativeGetPosition2DVal(long internalObject, String fxParam);
    private native void nativeSetPosition3DVal(long internalObject, String fxParam, NvsPosition3D val);
    private native NvsPosition3D nativeGetPosition3DVal(long internalObject, String fxParam);
    private native void nativeSetMenuVal(long internalObject, String fxParam, String val);
    private native String nativeGetMenuVal(long internalObject, String fxParam);
}
