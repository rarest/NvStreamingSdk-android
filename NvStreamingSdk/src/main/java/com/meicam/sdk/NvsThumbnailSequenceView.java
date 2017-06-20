//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Apr 12. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/*!
    \brief 缩略图序列

    缩略图序列，即视频按照某种程度缩放，来静态缩略显示的序列。视频编辑时，通过预览定位视频序列，可以设置序列的开始时间，时长，缩略图横纵比等。同时，依照缩略图在对应时段位置精确添加字幕和裁剪视频。
    \since 1.1.0
 */

public class NvsThumbnailSequenceView extends View {
    private String m_mediaFilePath;
    private long m_startTime;
    private long m_duration;
    private float m_thumbnailAspectRatio;
    private boolean m_stillImageHint;

    private long m_thumbnailSequenceView;
    private long m_iconGetter;
    private boolean m_painting;
    private Rect m_rect;

    class thumbnailInfo {
        long timestamp;
        int index;
        RectF rect;

        public thumbnailInfo() {
            rect = new RectF();
        }
    }

    public NvsThumbnailSequenceView(Context context) {
        super(context);
        init();
    }

    public NvsThumbnailSequenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NvsThumbnailSequenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NvsThumbnailSequenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
/*!
 *  \brief 设置媒体文件路径
 *  \param mediaFilePath 媒体文件路径
    \sa getMediaFilePath
 */
    public void setMediaFilePath(String mediaFilePath) {
        if (m_mediaFilePath != null && mediaFilePath != null && m_mediaFilePath.equals(mediaFilePath))
            return;

        m_mediaFilePath = mediaFilePath;
        cancelIconTask();
        invalidate();
    }
/*!
 *  \brief 获取媒体文件路径
 *  \return 返回媒体文件路径的字符串
    \sa setMediaFilePath
 */
    public String getMediaFilePath() {
        return m_mediaFilePath;
    }
/*!
 *  \brief 设置序列开始时间
 *  \param startTime 开始时间
    \sa getStartTime
 */
    public void setStartTime(long startTime) {
        if (startTime < 0)
            startTime = 0;

        if (startTime == m_startTime)
            return;

        m_startTime = startTime;
        cancelIconTask();
        invalidate();
    }
/*!
 *  \brief 获取序列开始时间
 *  \return 返回序列开始时间值
    \sa setStartTime
 */
    public long getStartTime() {
        return m_startTime;
    }
/*!
 *  \brief 设置序列时长
 *  \param duration 时长
    \sa getDuration
 */
    public void setDuration(long duration) {
        if (duration <= 0)
            duration = 1;

        if (duration == m_duration)
            return;

        m_duration = duration;
        cancelIconTask();
        invalidate();
    }
/*!
 *  \brief 获取序列时长
 *  \return 返回序列时长值
    \sa setDuration
 */
    public long getDuration() {
        return m_duration;
    }
/*!
 *  \brief 设置缩略图横纵比
 *  \param thumbnailAspectRatio 横纵比
    \sa getThumbnailAspectRatio
 */
    public void setThumbnailAspectRatio(float thumbnailAspectRatio) {
        if (thumbnailAspectRatio < 0.1f)
            thumbnailAspectRatio = 0.1f;
        else if (thumbnailAspectRatio > 10)
            thumbnailAspectRatio = 10;

        if (Math.abs(m_thumbnailAspectRatio - thumbnailAspectRatio) < 0.001f)
            return;

        m_thumbnailAspectRatio = thumbnailAspectRatio;
        cancelIconTask();
        invalidate();
    }
/*!
 *  \brief 获取缩略图横纵比
 *  \return 返回缩略图横纵比值
    \sa setThumbnailAspectRatio
 */
    public float getThumbnailAspectRatio() {
        return m_thumbnailAspectRatio;
    }
/*!
 *  \brief 设置加载视频时是否是静态图片
 *  \param stillImageHint 是否是静态图片。true表示是静态图片，false则非静态图片
    \sa getStillImageHint
 */
    public void setStillImageHint(boolean stillImageHint) {
        if (stillImageHint == m_stillImageHint)
            return;

        m_stillImageHint = stillImageHint;
        cancelIconTask();
        invalidate();
    }
/*!
 *  \brief 获取加载视频时是否是静态图片
 *  \return 返回值为boolean。true表示是静态图片，false则非静态图片
    \sa setStillImageHint
 */
    public boolean getStillImageHint() {
        return m_stillImageHint;
    }

    private float calcThumbnailWidth() {
        return m_rect.height() * m_thumbnailAspectRatio;
    }

    private long calcTimestampFromX(float x) {
        double ratio = (double)(x - m_rect.left) / m_rect.width();
        return m_startTime + (long)(m_duration * ratio + 0.5);
    }

    private int calcThumbnailIndexFromX(float x) {
        return (int)(Math.floor(x / calcThumbnailWidth()));
    }

    private float calcThumbnailStartXByIndex(int index) {
        return calcThumbnailWidth() * index;
    }

    private void collectThumbnailInfo(RectF rect, ArrayList<thumbnailInfo> thumbnailInfoArray) {
        int thumbnailIndex = calcThumbnailIndexFromX(rect.left);
        float x = calcThumbnailStartXByIndex(thumbnailIndex);
        float endX = rect.left + rect.width();
        while (x < endX) {
            thumbnailInfo info = new thumbnailInfo();
            info.timestamp = m_stillImageHint ? 0 : calcTimestampFromX(x);
            info.index = thumbnailIndex;
            info.rect.left = x;
            info.rect.top = m_rect.top;
            ++thumbnailIndex;
            x = calcThumbnailStartXByIndex(thumbnailIndex);
            info.rect.right = x;
            info.rect.bottom = m_rect.bottom;
            thumbnailInfoArray.add(info);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (m_mediaFilePath == null || m_mediaFilePath.isEmpty())
            return;

        // Collect thumbnails' information affected by rect
        if (m_rect == null)
            m_rect = new Rect();
        getDrawingRect(m_rect);
        ArrayList<thumbnailInfo> thumbnailInfoArray = new ArrayList<thumbnailInfo>();
        collectThumbnailInfo(new RectF(m_rect), thumbnailInfoArray);
        if (thumbnailInfoArray.isEmpty())
            return;

        //
        // Collect icons from cache
        //
        TreeMap<Number, Bitmap> iconMap = new TreeMap<Number, Bitmap>();
        for (thumbnailInfo info : thumbnailInfoArray) {
            if (!isInEditMode()) {
                Bitmap icon = nativeGetIconFromCache(m_mediaFilePath, info.timestamp);
                if (icon != null)
                    iconMap.put(info.timestamp, icon);
            }
        }

        if (iconMap.isEmpty()) {
            float x;
            if (thumbnailInfoArray.get(0).index > 0)
                x = calcThumbnailStartXByIndex(thumbnailInfoArray.get(0).index - 1);
            else
                x = calcThumbnailStartXByIndex(thumbnailInfoArray.get(thumbnailInfoArray.size() - 1).index + 1);
            long t = calcTimestampFromX(x);
            if (!isInEditMode()) {
                Bitmap icon = nativeGetIconFromCache(m_mediaFilePath, t);
                if (icon != null)
                    iconMap.put(t, icon);
            }
        }

        TreeSet<Number> imagesToFetch = new TreeSet<Number>();
        for (thumbnailInfo info : thumbnailInfoArray) {
            Map.Entry<Number, Bitmap> entry = iconMap.floorEntry(info.timestamp);
            // We fail to find an image with the given timestamp value,
            // To make thumbnail sequence looks better we use an image whose
            // timestamp is close to the given timestamp
            if (entry == null || entry.getKey().longValue() != info.timestamp) {
                imagesToFetch.add(info.timestamp);
                if (entry == null && iconMap.size() > 0)
                    entry = iconMap.lastEntry();
            }

            if (entry != null && entry.getValue() != null)
                canvas.drawBitmap(entry.getValue(), null, info.rect, null);
        }

        if (imagesToFetch.isEmpty())
            return;

        m_painting = true;

        // Fetch missing images
        if (!isInEditMode())
            for (Number t : imagesToFetch)
                nativeGetIcon(m_iconGetter, m_mediaFilePath, t.longValue());

        m_painting = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelIconTask();
        if (!isInEditMode())
            nativeClose(m_thumbnailSequenceView);
        m_thumbnailSequenceView = 0;
        super.onDetachedFromWindow();
    }

    private void init() {
        m_iconGetter = 0;
        m_rect = new Rect();
        if (!isInEditMode())
            m_thumbnailSequenceView = nativeInit();
        m_painting = false;
        m_mediaFilePath = null;
        m_startTime = 0;
        m_duration = 4000000;
        m_thumbnailAspectRatio = 9.0f / 16;
        m_stillImageHint = false;
    }

    private void cancelIconTask() {
        if (!isInEditMode())
            nativeCancelIconTask(m_iconGetter);
    }

    private void notifyIconArrived(Bitmap bitmap, long timestamp) {
        if (!m_painting) {
            invalidate();
        } else {
            // NOTE: invalidate() will not take effect while we are in onDraw
            new Handler().post(
                    new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    }
            );
        }
    }

    private native long nativeInit();
    private native void nativeClose(long thumbnailSequenceView);
    private native void nativeCancelIconTask(long iconGetter);
    private native Bitmap nativeGetIconFromCache(String mediaFilePath, long timestamp);
    private native void nativeGetIcon(long iconGetter, String mediaFilePath, long timestamp);
}
