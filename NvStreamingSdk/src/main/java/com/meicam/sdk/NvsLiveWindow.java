//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 5. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import android.content.Context;
import android.graphics.PointF;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.Surface;
import android.util.AttributeSet;
import android.util.Log;


public class NvsLiveWindow extends SurfaceView implements SurfaceHolder.Callback
{
    /*! \anchor FILLMODE_PRESERVEASPECTCROP */
    /*! @name Live Window 填充模式 */
    //!@{

    /*!
        \brief 图像按比例均匀填充，必要时进行裁剪(默认模式)

        ![] (PreserveAspectCrop.png)
    */
    public static final int FILLMODE_PRESERVEASPECTCROP = 0;

    /*!
        \brief 图像均匀地缩放来适合窗口，没有裁剪

        ![] (PreserveAspectFit.png)
    */
    public static final int FILLMODE_PRESERVEASPECTFIT = 1;

    /*!
        \brief 图像被缩放来适合窗口

        ![] (Stretch.png)
    */
    public static final int FILLMODE_STRETCH = 2;

    //!@}
    protected long m_internalObject = 0;
    protected int m_fillMode = FILLMODE_PRESERVEASPECTCROP;

    public NvsLiveWindow(Context context)
    {
        super(context);
        init();
    }

    public NvsLiveWindow(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public NvsLiveWindow(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NvsLiveWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /*!
        \brief 设置LiveWindow的填充模式
        \param fillMode 填充模式。具体参见[Live Window 填充模式] (@ref FILLMODE_PRESERVEASPECTCROP)
    */
    public void setFillMode(int fillMode)
    {
        if (fillMode != FILLMODE_PRESERVEASPECTCROP &&
            fillMode != FILLMODE_PRESERVEASPECTFIT &&
            fillMode != FILLMODE_STRETCH)
            fillMode = FILLMODE_PRESERVEASPECTCROP;

        if (fillMode == m_fillMode)
            return;

        m_fillMode = fillMode;
        nativeSetFillMode(m_internalObject, fillMode);
    }

    /*!
        \brief 获取LiveWindow的填充模式
        \return 返回LiveWindow的[填充模式] (@ref FILLMODE_PRESERVEASPECTCROP)
    */
    public int getFillMode()
    {
        return m_fillMode;
    }
    /*!
        \brief 时间线坐标转换成视图坐标
        \param ptCanonical 时间线坐标点
        \return 返回转换后的视图坐标点PointF对象
        \since 1.1.0
        \sa mapViewToCanonical
    */
    public PointF mapCanonicalToView(PointF ptCanonical)
    {
        return nativeMapCanonicalToView(m_internalObject, ptCanonical);
    }
    /*!
        \brief 视图坐标转换成时间线坐标
        \param ptView 视图坐标点
        \return 返回转换后的时间线坐标点PointF对象
        \since 1.1.0
        \sa mapCanonicalToView
    */
    public PointF mapViewToCanonical(PointF ptView)
    {
        return nativeMapViewToCanonical(m_internalObject, ptView);
    }
    /*!
        \brief 归一化坐标转换成视图坐标
        \param ptNormalized 归一化坐标点
        \return 返回转换后的视图坐标点PointF对象
        \since 1.1.0
        \sa mapViewToNormalized
    */
    public PointF mapNormalizedToView(PointF ptNormalized)
    {
        return nativeMapNormalizedToView(m_internalObject, ptNormalized);
    }
    /*!
        \brief 视图坐标转换成归一化坐标
        \param ptView 视图坐标点
        \return 返回转换后的归一化坐标点PointF对象
        \since 1.1.0
        \sa mapNormalizedToView
    */
    public PointF mapViewToNormalized(PointF ptView)
    {
        return nativeMapViewToNormalized(m_internalObject, ptView);
    }

    /*!
        \brief 清除LiveWindow的视频帧
    */
    public void clearVideoFrame()
    {
        nativeClearVideoFrame(m_internalObject);
    }

    /*! \cond */
    @Override
    protected void onDetachedFromWindow()
    {
        if (!isInEditMode() && m_internalObject != 0)
            nativeClose(m_internalObject);

        getHolder().removeCallback(this);

        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (!isInEditMode())
            nativeOnSizeChanged(m_internalObject, w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if (!isInEditMode() && width >= 1 && height >= 1)
            nativeSurfaceChanged(m_internalObject, holder.getSurface(), width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Nothing to do
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (!isInEditMode())
            nativeSurfaceDestroyed(m_internalObject, holder.getSurface());
    }
    /*! \endcond */

    private void init()
    {
        getHolder().addCallback(this);

        if (!isInEditMode())
            nativeInit();
    }

    private native void nativeInit();
    private native void nativeClose(long internalObject);
    private native void nativeSetFillMode(long internalObject, int fillMode);
    private native PointF nativeMapCanonicalToView(long internalObject, PointF ptCanonical);
    private native PointF nativeMapViewToCanonical(long internalObject, PointF ptView);
    private native PointF nativeMapNormalizedToView(long internalObject, PointF ptNormalized);
    private native PointF nativeMapViewToNormalized(long internalObject, PointF ptView);
    private native void nativeClearVideoFrame(long internalObject);
    private native void nativeOnSizeChanged(long internalObject, int w, int h);
    private native void nativeSurfaceChanged(long internalObject, Surface surface, int width, int height);
    private native void nativeSurfaceDestroyed(long internalObject, Surface surface);
}

