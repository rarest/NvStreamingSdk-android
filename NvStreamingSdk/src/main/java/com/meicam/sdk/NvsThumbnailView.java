//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Apr 21. 2017
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
    \brief 缩略图展示

    视频预览时，按照某种程度缩放，静态展示缩略图。
    \since 1.1.0
 */
public class NvsThumbnailView extends View {
    private String m_mediaFilePath;
    private Bitmap m_thumbnail;
    private long m_thumbnailView;
    private long m_iconGetter;
    private boolean m_painting;
    private boolean m_needUpdate;

    public NvsThumbnailView(Context context) {
        super(context);
        init();
    }

    public NvsThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NvsThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NvsThumbnailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        m_needUpdate = true;
        cancelIconTask();
        invalidate();
    }

/*!
 *  \brief 获取媒体文件路径
 *  \return 返回媒体文件路径的字符串
 */
    public String getMediaFilePath() {
        return m_mediaFilePath;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (m_mediaFilePath == null || m_mediaFilePath.isEmpty())
            return;

        if (m_thumbnail == null || m_needUpdate) {
            m_needUpdate = false;
            m_painting = true;
            if (!isInEditMode())
                nativeGetThumbnail(m_iconGetter, m_mediaFilePath);
            m_painting = false;
            return;
        }

        Rect rect = new Rect();
        getDrawingRect(rect);
        canvas.drawBitmap(m_thumbnail, null, rect, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelIconTask();
        if (!isInEditMode())
            nativeClose(m_thumbnailView);
        m_thumbnailView = 0;
        m_thumbnail = null;
        super.onDetachedFromWindow();
    }

    private void init() {
        m_thumbnail = null;
        m_iconGetter = 0;
        if (!isInEditMode())
            m_thumbnailView = nativeInit();
        m_painting = false;
        m_mediaFilePath = null;
        m_needUpdate = false;
    }

    private void cancelIconTask() {
        if (!isInEditMode())
            nativeCancelIconTask(m_iconGetter);
    }

    private void notifyThumbnailArrived(final Bitmap bitmap) {
        if (!m_painting) {
            m_thumbnail = bitmap;
            invalidate();
        } else {
            // NOTE: invalidate() will not take effect while we are in onDraw
            new Handler().post(
                    new Runnable() {
                        @Override
                        public void run() {
                            m_thumbnail = bitmap;
                            invalidate();
                        }
                    }
            );
        }
    }

    private native long nativeInit();
    private native void nativeClose(long thumbnailView);
    private native void nativeCancelIconTask(long iconGetter);
    private native void nativeGetThumbnail(long iconGetter, String mediaFilePath);
}
