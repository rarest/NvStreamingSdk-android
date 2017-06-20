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

import android.graphics.PointF;
import android.graphics.RectF;
import java.util.List;
import com.meicam.sdk.NvsObject;

/*!
    \brief 时间线动画贴纸，带有动画效果的贴纸

    时间线动画贴纸是视频编辑时使用的一种美化特效，叠加在视频上会产生一些特殊效果。编辑视频时，可通过时间线(Time Line)来添加和移除动画贴纸。如果添加的贴纸位置不合理，还可进行调整移动。

    注：动画贴纸在时间线上的入点和出点单位都为微秒
 */
public class NvsTimelineAnimatedSticker extends NvsFx
{

/*!
     \brief 动画贴纸在时间线上显示的入点
     \return 动画贴纸在时间线上显示的入点
     \sa getOutPoint
     \sa changeInPoint
*/
    public long getInPoint()
    {
        return nativeGetInPoint(m_internalObject);
    }
/*!
     \brief 动画贴纸在时间线上显示的出点
     \return 动画贴纸在时间线上显示的出点
     \sa getInPoint
     \sa changeOutPoint
*/
    public long getOutPoint()
    {
        return nativeGetOutPoint(m_internalObject);
    }
/*!
     \brief 改变动画贴纸在时间线上显示的入点
     \param newInPoint 动画贴纸在时间线上的新的入点
     \return 返回动画贴纸在时间线上的显示的入点
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa changeOutPoint
     \sa getInPoint
     \sa movePosition
 */
    public long changeInPoint(long newInPoint)
    {
        return nativeChangeInPoint(m_internalObject, newInPoint);
    }
/*!
     \brief 改变动画贴纸在时间线上显示的出点
     \param newOutPoint 动画贴纸在时间线上的新的出点
     \return 返回动画贴纸在时间线上的显示的出点
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa changeInPoint
     \sa getOutPoint
     \sa movePosition
 */
    public long changeOutPoint(long newOutPoint)
    {
        return nativeChangeOutPoint(m_internalObject, newOutPoint);
    }
/*!
     \brief 改变动画贴纸在时间线上的显示位置(入点和出点同时偏移offset值)
     \param offset 入点和出点改变的偏移值
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa changeInPoint
     \sa changeOutPoint
 */
    public void movePosition(long offset)
    {
        nativeMovePosition(m_internalObject, offset);
        return;
    }

/*!
     \brief 设置动画贴纸的缩放值
     \param scale 缩放值
     \sa getScale
 */
    public void setScale(float scale)
    {
        nativeSetScale(m_internalObject, scale);
    }
/*!
     \brief 获取动画贴纸的缩放值
     \return 返回获取的缩放值
     \sa setScale
 */
    public float getScale()
    {
        return nativeGetScale(m_internalObject);
    }
/*!
     \brief 设置动画贴纸的水平翻转
     \param flip 是否水平翻转。true表示水平翻转，false则不翻转
     \sa getHorizontalFlip
 */
    public void setHorizontalFlip(boolean flip)
    {
        nativeSetHorizontalFlip(m_internalObject, flip);
    }
/*!
     \brief 获取动画贴纸的水平翻转状态
     \return 返回boolean值，表示水平翻转状态。true表示已翻转，false则未翻转
     \sa setHorizontalFlip
 */
    public boolean getHorizontalFlip()
    {
        return nativeGetHorizontalFlip(m_internalObject);
    }
/*!
     \brief 设置动画贴纸的竖直翻转
     \param flip 是否竖直翻转。true表示竖直翻转，false则不翻转
     \sa getVerticalFlip
 */
    public void setVerticalFlip(boolean flip)
    {
        nativeSetVerticalFlip(m_internalObject, flip);
    }
/*!
     \brief 获取动画贴纸的竖直翻转状态
     \return 返回boolean值，表示竖直翻转状态。true表示已翻转，false则未翻转
     \sa setVerticalFlip
 */
    public boolean getVerticalFlip()
    {
        return nativeGetVerticalFlip(m_internalObject);
    }
/*!
     \brief 设置动画贴纸的旋转角度
     \param angle 旋转角度值
     \sa getRotationZ
 */
    public void setRotationZ(float angle)
    {
        nativeSetRotationZ(m_internalObject, angle);
    }
/*!
     \brief 获取动画贴纸的旋转角度值
     \return 返回获取的旋转角度值
     \sa setRotationZ
 */
    public float getRotationZ()
    {
        return nativeGetRotationZ(m_internalObject);
    }
/*!
     \brief 设置动画贴纸的平移
     \param translation 平移位置
     \sa getTransltion
 */
    public void setTranslation(PointF translation)
    {
        nativeSetTranslation(m_internalObject, translation);
    }
/*!
     \brief 获取动画贴纸的平移位置
     \return 返回PointF对象，表示获取的平移位置
     \sa setTranslation
 */
    public PointF getTranslation()
    {
        return nativeGetTranslation(m_internalObject);
    }

/*!
     \brief 获取动画贴纸的原始的包围矩形框
     \return 返回RectF对象，表示获取的原始的包围矩形框
 */
    public RectF getOriginalBoundingRect()
    {
        return nativeGetOriginalBoundingRect(m_internalObject);
    }

/*!
    \brief 获取动画贴纸的原始包围矩形框变换后的顶点位置
    \return 返回List<PointF>对象，包含四个顶点位置，依次分对应原始包围矩形框的左上，左下，右下，右上顶点
    \since 1.4.0
 */
    public List<PointF> getBoundingRectangleVertices()
    {
        return nativeGetBoundingRectangleVertices(m_internalObject);
    }

    private native long nativeGetInPoint(long internalObject);
    private native long nativeGetOutPoint(long internalObject);
    private native long nativeChangeInPoint(long internalObject, long newInPoint);
    private native long nativeChangeOutPoint(long internalObject, long newOutPoint);
    private native void nativeMovePosition(long internalObject, long offset);
    private native void nativeSetScale(long internalObject, float scale);
    private native float nativeGetScale(long internalObject);
    private native void nativeSetHorizontalFlip(long internalObject, boolean flip);
    private native boolean nativeGetHorizontalFlip(long internalObject);
    private native void nativeSetVerticalFlip(long internalObject, boolean flip);
    private native boolean nativeGetVerticalFlip(long internalObject);
    private native void nativeSetRotationZ(long internalObject, float angle);
    private native float nativeGetRotationZ(long internalObject);
    private native void nativeSetTranslation(long internalObject, PointF translation);
    private native PointF nativeGetTranslation(long internalObject);
    private native RectF nativeGetOriginalBoundingRect(long internalObject);
    private native List<PointF> nativeGetBoundingRectangleVertices(long internalObject);
}
