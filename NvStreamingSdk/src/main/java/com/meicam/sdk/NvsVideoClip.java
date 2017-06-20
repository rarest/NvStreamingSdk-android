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

import com.meicam.sdk.NvsVideoFx;
import android.graphics.RectF;

/*!
    \brief 视频片段，对视频文件的描述

    视频片段源可以是视频或者图片。每个视频片段可以修改其裁剪入点、裁剪出点以及播放速度，也可以设置摇摄和扫描。编辑视频时，可以按特效类型的不同（内建特效，包裹式特效，美颜特效）添加或者插入多个视频特效。
 */
public class NvsVideoClip extends NvsClip
{

/*! \anchor VIDEO_CLIP_TYPE */
/*! @name 片段类型 */
//!@{
    public static final int VIDEO_CLIP_TYPE_AV = 0;     //!< 音视频类型
    public static final int VIDEO_CLIP_TYPE_IMAGE = 1;  //!< 图片类型

//!@}

/*! \anchor ROLE_IN_THEME */
/*! @name 片段在主题中的角色 */
//!@{
    public static final int ROLE_IN_THEME_GENERAL = 0;  //!< 通用
    public static final int ROLE_IN_THEME_TITLE = 1;    //!< 片头
    public static final int ROLE_IN_THEME_TRAILER = 2;  //!< 片尾

//!@}

/*! \anchor ClIP_MOTIONMODE */
/*! @name 片段运作模式 */
//!@{
    public static final int CLIP_MOTIONMODE_LETTERBOX_ZOOMIN = 0;  //!< \if ENGLISH \else 放大 \since 1.1.0 \endif
    public static final int ClIP_MOTIONMODE_LETTERBOX_ZOOMOUT = 1; //!< \if ENGLISH \else 缩小 \since 1.1.0 \endif
    public static final int IMAGE_CLIP_MOTIONMMODE_ROI = 2;        //!< \if ENGLISH \else 图片片段ROI(Region Of Interesting) \since 1.1.0 \endif

//!@}

/*! \anchor ClIP_BACKGROUNDMODE */
/*! @name 片段背景模式 */
//!@{
    public static final int ClIP_BACKGROUNDMODE_COLOR_SOLID = 0;  //!< \if ENGLISH \else 纯色 \since 1.4.0 \endif
    public static final int ClIP_BACKGROUNDMODE_BLUR = 1; //!< \if ENGLISH \else 模糊 \since 1.4.0 \endif
//!@}

/*!
    \brief 获取片段类型
    \return 返回[片段类型] (@ref VIDEO_CLIP_TYPE)
 */
    public int getVideoType()
    {
        return nativeGetVideoType(m_internalObject);
    }

/*!
    \brief 获取片段在主题中的角色
    \return 返回[片段在主题中的角色] (@ref ROLE_IN_THEME)
 */
    public int getRoleInTheme()
    {
        return nativeGetRoleInTheme(m_internalObject);
    }

/*!
    \brief 设置摇摄和扫描

    摇摄和扫描功能是用来适配图像在显示窗口上的位置与大小的。具体请参见[摇摄与扫描(Pan and Scan)] (\ref Pan_Scan.md)专题。
    \param pan 摇摄
    \param scan 扫描
    \sa getPanAndScan
 */
    public void setPanAndScan(float pan, float scan)
    {
        nativeSetPanAndScan(m_internalObject, pan, scan);
        return;
    }
/*!
    \brief 获取摇摄和扫描
    \return 返回获取的摇摄扫描对象
    \sa setPanAndScan
 */
    public NvsPanAndScan getPanAndScan()
    {
       return nativeGetPanAndScan(m_internalObject);
    }
/*!
    \brief 获取背景模式
    \return 返回获取的背景模式对象
    \sa setClipBackgroundMode
 */
    public int getSourceBackgroundMode()
    {
       return nativeGetSourceBackgroundMode(m_internalObject);
    }

/*!
    \brief 设置背景模式
    \param mode 背景模式
    \sa getSourceBackgroundMode
 */
    public void setSourceBackgroundMode(int mode)
    {
        nativeSetSourceBackgroundMode(m_internalObject, mode);
    }

/*!
    \brief 获取图片片段运作模式
    \return 返回表示图片片段运作模式的整形值。请参见[片段运作模式] (@ref ClIP_MOTIONMODE)
    \since 1.1.0
    \sa setImageMotionMode
 */
    public int getImageMotionMode()
    {
        return nativeGetImageMotionMode(m_internalObject);
    }
/*!
    \brief 设置图片运作模式
    \param mode 图片片段运作模式。请参见[片段运作模式] (@ref ClIP_MOTIONMODE)
    \since 1.1.0
    \sa getImageMotionMode
 */
    public void setImageMotionMode(int mode)
    {
        nativeSetImageMotionMode(m_internalObject, mode);
    }
/*!
    \brief 获取图片动画状态
    \return 返回boolean值，表示图片动画状态。值为true则支持图片动画，false则不支持。
    \since 1.1.0
    \sa setImageMotionAnimationEnabled
 */
    public boolean getImageMotionAnimationEnabled()
    {
        return nativeGetImageMotionAnimationEnabled(m_internalObject);
    }
/*!
    \brief 设置是否支持图片动画
    \param enabled 是否支持图片动画。值为true支持图片动画，false则不支持。
    \since 1.1.0
    \sa getImageMotionAnimationEnabled
 */
    public void setImageMotionAnimationEnabled(boolean enabled)
    {
        nativeSetImageMotionAnimationEnabled(m_internalObject, enabled);
    }
/*!
    \brief 获取图片片段起始ROI
    \return 返回RectF对象，表示图片片段起始ROI。
    \since 1.1.0
    \sa setImageMotionROI
 */
    public RectF getStartROI()
    {
        return nativeGetStartROI(m_internalObject);
    }
/*!
    \brief 获取图片片段终止ROI
    \return 返回RectF对象，表示图片片段终止ROI。
    \since 1.1.0
    \sa setImageMotionROI
 */
    public RectF getEndROI()
    {
        return nativeGetEndROI(m_internalObject);
    }
/*!
    \brief 设置图片片段动态移动ROI

    具体情况请参见[图片片段ROI专题] (\ref ImageROI.md)
    \param startROI 图片片段起始ROI
    \param endROI 图片片段终止ROI
    \since 1.1.0
    \sa getStartROI
    \sa getEndROI
 */
    public void setImageMotionROI(RectF startROI, RectF endROI)
    {
        nativeSetImageMotionROI(m_internalObject, startROI, endROI);
    }

/*!
    \brief 在片段末尾追加内嵌式特效
    \param fxName 特效名称。获取视频特效名称，请参见[getAllBuiltinVideoFxNames()] (@ref com.meicam.sdk.NvsStreamingContext.getAllBuiltinVideoFxNames)或[内建特效名称列表] (\ref FxNameList.md)
    \return 返回追加的视频特效对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa insertBuiltinFx
    \sa appendBeautyFx
    \sa appendPackagedFx

*/
    public NvsVideoFx appendBuiltinFx(String fxName)
    {
        return nativeAppendBuiltinFx(m_internalObject, fxName);
    }

/*!
    \brief 在片段上指定特效索引处插入内嵌式特效
    \param fxName 特效名称。获取视频特效名称，请参见[getAllBuiltinVideoFxNames()] (@ref com.meicam.sdk.NvsStreamingContext.getAllBuiltinVideoFxNames)或[内建特效名称列表] (\ref FxNameList.md)
    \param fxIndex 插入特效索引
    \return 返回插入的视频特效对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa appendBuiltinFx
    \sa insertPackagedFx
    \sa insertBeautyFx
 */
    public NvsVideoFx insertBuiltinFx(String fxName, int fxIndex)
    {
        return nativeInsertBuiltinFx(m_internalObject, fxName, fxIndex);
    }

/*!
    \brief 在片段末尾追加资源包特效
    \param fxPackageId 特效资源包ID
    \return 返回追加的视频特效对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa insertPackagedFx
    \sa appendBuiltinFx
    \sa appendBeautyFx
 */
    public NvsVideoFx appendPackagedFx(String fxPackageId)
    {
        return nativeAppendPackagedFx(m_internalObject, fxPackageId);
    }

/*!
    \brief 在片段上指定特效索引处插入资源包特效
    \param fxPackageId 特效资源包ID
    \param fxIndex 插入特效索引，参见[getFxCount()] (@ref com.meicam.sdk.NvsClip.getFxCount)
    \return 返回插入的视频特效对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa appendPackagedFx
    \sa insertBeautyFx
    \sa insertBuiltinFx
 */
    public NvsVideoFx insertPackagedFx(String fxPackageId, int fxIndex)
    {
        return nativeInsertPackagedFx(m_internalObject, fxPackageId, fxIndex);
    }
/*!
    \brief 在片段末尾追加美颜特效
    \return 返回追加的视频特效对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa insertBeautyFx
    \sa appendPackagedFx
    \sa appendBuiltinFx
 */
    public NvsVideoFx appendBeautyFx()
    {
        return nativeAppendBeautyFx(m_internalObject);
    }
/*!
    \brief 在片段上指定特效索引处插入美颜特效
    \param fxIndex 插入特效索引，参见[getFxCount()] (@ref com.meicam.sdk.NvsClip.getFxCount)
    \return 返回插入的视频特效对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa appendBeautyFx
    \sa insertBuiltinFx
    \sa insertPackagedFx
 */
    public NvsVideoFx insertBeautyFx(int fxIndex)
    {
        return nativeInsertBeautyFx(m_internalObject, fxIndex);
    }
/*!
    \brief 移除特效
    \param fxIndex 特效索引，参见[getFxCount()] (@ref com.meicam.sdk.NvsClip.getFxCount)
    \return 判断是否移除成功。返回true则移除成功，false则移除失败
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeAllFx
    \sa getFxByIndex
 */
    public boolean removeFx(int fxIndex)
    {
        return nativeRemoveFx(m_internalObject, fxIndex);
    }
/*!
    \brief 移除所有特效
    \return 判断是否移除成功。返回true则移除成功，false则移除失败
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeFx
 */
    public boolean removeAllFx()
    {
        return nativeRemoveAllFx(m_internalObject);
    }

/*!
    \brief 通过索引获取特效
    \param fxIndex 特效索引，参见[getFxCount()] (@ref com.meicam.sdk.NvsClip.getFxCount)
    \return 返回获取的视频特效对象
 */
    public NvsVideoFx getFxByIndex(int fxIndex)
    {
        return nativeGetFxByIndex(m_internalObject, fxIndex);
    }

    private native int nativeGetVideoType(long internalObject);
    private native int nativeGetRoleInTheme(long internalObject);
    private native void nativeSetPanAndScan(long internalObject, float pan, float scan);
    private native NvsPanAndScan nativeGetPanAndScan(long internalObject);
    private native void nativeSetSourceBackgroundMode(long internalObject, int mode);
    private native int nativeGetSourceBackgroundMode(long internalObject);

    private native int nativeGetImageMotionMode(long internalObject);
    private native void nativeSetImageMotionMode(long internalObject, int mode);
    private native boolean nativeGetImageMotionAnimationEnabled(long internalObject);
    private native void nativeSetImageMotionAnimationEnabled(long internalObject, boolean enabled);
    private native RectF nativeGetStartROI(long internalObject);
    private native RectF nativeGetEndROI(long internalObject);
    private native void nativeSetImageMotionROI(long internalObject, RectF startROI, RectF endROI);

    private native NvsVideoFx nativeAppendBuiltinFx(long internalObject, String fxName);
    private native NvsVideoFx nativeInsertBuiltinFx(long internalObject, String fxName, int fxIndex);
    private native NvsVideoFx nativeAppendPackagedFx(long internalObject, String fxPackageId);
    private native NvsVideoFx nativeInsertPackagedFx(long internalObject, String fxPackageId, int fxIndex);
    private native NvsVideoFx nativeAppendBeautyFx(long internalObject);
    private native NvsVideoFx nativeInsertBeautyFx(long internalObject, int fxIndex);
    private native boolean nativeRemoveFx(long internalObject, int fxIndex);
    private native boolean nativeRemoveAllFx(long internalObject);
    private native NvsVideoFx nativeGetFxByIndex(long internalObject, int fxIndex);
}
