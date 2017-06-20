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
import android.graphics.PointF;
import android.graphics.RectF;

/*!
    \brief 时间线字幕

    时间线字幕是视频上叠加的自定义文字。编辑视频时，可以添加和移除时间线字幕，并对字幕位置进行调整处理。添加完字幕，还可以进行样式设置，包括字体大小，颜色，阴影，描边等。
 */
public class NvsTimelineCaption extends NvsFx
{
    /*! \anchor DEFAULT_CATEGORY */
    /*! @name 字幕种类 */
    //!@{
    public static final int DEFAULT_CATEGORY = 0;  //!< \if ENGLISH \else 默认种类 \endif
    public static final int USER_CATEGORY = 1;     //!< \if ENGLISH \else 用户自定义种类 \endif
    public static final int THEME_CATEGORY = 2;    //!< \if ENGLISH \else 主题种类 \endif

    public static final int ROLE_IN_THEME_GENERAL = 0;  //!< \if ENGLISH \else 通用 \endif
    public static final int ROLE_IN_THEME_TITLE = 1;    //!< \if ENGLISH \else 片头 \endif
    public static final int ROLE_IN_THEME_TRAILER = 2;  //!< \if ENGLISH \else 片尾 \endif

    //!@}

/*!
     \brief 字幕在时间线上显示的入点
     \return 字幕在时间线上显示的入点
     \sa getOutPoint
     \sa changeInPoint
*/
    public long getInPoint()
    {
        return nativeGetInPoint(m_internalObject);
    }

/*!
     \brief 字幕在时间线显示上的出点
     \return 字幕在时间线显示上的出点
     \sa getInPoint
     \sa changeOutPoint
*/
    public long getOutPoint()
    {
        return nativeGetOutPoint(m_internalObject);
    }

/*!
    \brief 改变字幕在时间线上显示的入点
    \param newInPoint 字幕在时间线上的新的入点
    \return 返回字幕在时间线上的显示的入点
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
     \brief 改变字幕在时间线上显示的出点
     \param newOutPoint 字幕在时间线上的新的出点
     \return 返回字幕在时间线上显示的出点
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
     \brief 改变字幕在时间线上的显示位置(入点和出点同时偏移offset值)
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
     \brief 字幕在主题中的角色
     \return 返回[字幕在主题中的角色] (@ref ROLE_IN_THEME)
*/
    public int getRoleInTheme()
    {
        return nativeGetRoleInTheme(m_internalObject);
    }

/*!
     \brief 获得字幕的种类
     \return 返回获得的[字幕种类] (@ref DEFAULT_CATEGORY)
*/
    public int getCategory()
    {
        return nativeGetCategory(m_internalObject);
    }

/*!
     \brief 获得字幕样式包裹ID
     \return 返回获得的字幕样式资源包ID
*/
    public String getCaptionStylePackageId()
    {
        return nativeGetCaptionStylePackageId(m_internalObject);
    }

/*!
     \brief 运用字幕样式
     \param captionStylePackageId 字幕样式资源包ID
     \return 返回boolean值。true表示成功运用字幕样式，false则运用失败
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
 */

    public boolean applyCaptionStyle(String captionStylePackageId)
    {
        return nativeApplyCaptionStyle(m_internalObject, captionStylePackageId);
    }
/*!
     \brief 设置字幕文本
     \param text 字幕文本
     \sa getText
 */
    public void setText(String text)
    {
        nativeSetText(m_internalObject, text);
    }

/*!
     \brief 获取字幕文本
     \return 返回获取的字幕文本
     \sa setText
 */
    public String getText()
    {
        return nativeGetText(m_internalObject);
    }

/*!
     \brief 设置字幕字体加粗
     \param bold 表示字幕字体是否加粗。true表示加粗，false则不加粗
     \sa getBold
 */
    public void setBold(boolean bold)
    {
        nativeSetBold(m_internalObject, bold);
    }

/*!
     \brief 获取字幕字体加粗状态
     \return 返回字幕字体加粗状态。true表示字体已加粗，false则字体未加粗
     \sa setBold
 */
    public boolean getBold()
    {
        return nativeGetBold(m_internalObject);
    }

/*!
     \brief 设置字幕字体斜体
     \param italic 是否设为斜体。true表示设置字幕字体为斜体，false则不设为斜体
     \sa getItalic
 */
    public void setItalic(boolean italic)
    {
        nativeSetItalic(m_internalObject, italic);
    }
/*!
     \brief 获取字幕斜体状态
     \return 返回boolean值，true表示是斜体字体，false则不是斜体
     \sa setItalic
 */
    public boolean getItalic()
    {
        return nativeGetItalic(m_internalObject);
    }

/*!
     \brief 设置字幕文本颜色
     \param textColor 字幕文本颜色值
     \sa getTextColor
 */
    public void setTextColor(NvsColor textColor)
    {
        nativeSetTextColor(m_internalObject, textColor);
    }

/*!
     \brief 获取字幕字体的当前颜色值
     \return 返回NvsColor对象，表示获得的颜色值
     \sa setTextColor
 */
    public NvsColor getTextColor()
    {
        return nativeGetTextColor(m_internalObject);
    }

/*!
     \brief 设置字幕描边
     \param drawOutline 是否对字幕描边。true表示对字幕进行描边，false则不描边
     \sa getDrawOutline
 */
    public void setDrawOutline(boolean drawOutline)
    {
        nativeSetDrawOutline(m_internalObject, drawOutline);
    }

/*!
     \brief 获取字幕描边状态
     \return 返回boolean值，true表示已描边，false则未描边
     \sa setDrawOutline
 */
    public boolean getDrawOutline()
    {
        return nativeGetDrawOutline(m_internalObject);
    }

/*!
     \brief 设置字幕描边的颜色
     \param outlineColor 字幕描边颜色值
     \sa getOutlineColor
 */
    public void setOutlineColor(NvsColor outlineColor)
    {
        nativeSetOutlineColor(m_internalObject, outlineColor);
    }

/*!
     \brief 获取当前字幕描边的颜色值
     \return 返回NvsColor对象，表示获得的字幕描边的颜色
     \sa setOutlineColor
 */
    public NvsColor getOutlineColor()
    {
        return nativeGetOutlineColor(m_internalObject);
    }

/*!
     \brief 设置字幕描边的宽度
     \param outlineWidth 字幕描边的宽度
     \sa getOutlineWidth
 */
    public void setOutlineWidth(float outlineWidth)
    {
        nativeSetOutlineWidth(m_internalObject, outlineWidth);
    }

/*!
     \brief 获取当前字幕描边的宽度
     \return 返回获得的字幕描边的宽度值
     \sa setOutlineWidth
 */
    public float getOutlineWidth()
    {
        return nativeGetOutlineWidth(m_internalObject);
    }

/*!
     \brief 设置字幕阴影
     \param drawShadow 是否设置字幕阴影。true表示设置字幕阴影，false则不设置
     \sa getDrawShadow
 */
    public void setDrawShadow(boolean drawShadow)
    {
        nativeSetDrawShadow(m_internalObject, drawShadow);
    }

/*!
     \brief 获取当前字幕阴影状态
     \return 返回boolean值，true表示有字幕阴影，false则没有字幕阴影
     \sa setDrawShadow
 */
    public boolean getDrawShadow()
    {
        return nativeGetDrawShadow(m_internalObject);
    }

/*!
     \brief 设置字幕阴影的颜色
     \param shadowColor 字幕阴影颜色值
     \sa getShadowColor
 */
    public void setShadowColor(NvsColor shadowColor)
    {
        nativeSetShadowColor(m_internalObject, shadowColor);
    }

/*!
     \brief 获取当前字幕阴影的颜色
     \return 返回NvsColor对象，表示获得的字幕阴影颜色值
     \sa setShadowColor
 */
    public NvsColor getShadowColor()
    {
        return nativeGetShadowColor(m_internalObject);
    }

/*!
     \brief 设置字幕阴影的偏移量
     \param shadowOffset 字幕阴影的偏移量
     \sa getShadowOffset
 */
    public void setShadowOffset(PointF shadowOffset)
    {
        nativeSetShadowOffset(m_internalObject, shadowOffset);
    }

/*!
     \brief 获取当前字幕阴影的偏移量
     \return 返回PointF对象，表示获得的字幕阴影偏移量
     \sa setShadowOffset
 */
    public PointF getShadowOffset()
    {
        return nativeGetShadowOffset(m_internalObject);
    }

/*!
     \brief 设置字幕字体大小
     \param fontSize 字幕字体大小值
     \sa getFontSize
 */
    public void setFontSize(float fontSize)
    {
        nativeSetFontSize(m_internalObject, fontSize);
    }

/*!
     \brief 获取当前字幕的字体大小
     \return 返回获得的字幕字体大小值
     \sa setFontSize
 */
    public float getFontSize()
    {
        return nativeGetFontSize(m_internalObject);
    }

/*!
     \brief 设置字幕字体
     \param filePath 字幕字体的文件路径，若设为空字符串，则设为默认字体
     \since 1.3.0
     \sa getFontFilePath
 */
    public void setFontByFilePath(String filePath)
    {
        nativeSetFontByFilePath(m_internalObject, filePath);
    }
/*!
     \brief 获取字幕字体的文件路径
     \return 返回字幕字体的文件路径
     \since 1.3.0
     \sa setFontByFilePath
 */
    public String getFontFilePath()
    {
        return nativeGetFontFilePath(m_internalObject);
    }

/*!
     \brief 字幕平移
     \param translation 字幕平移的x,y值
     \sa getCaptionTranslation
 */
    public void setCaptionTranslation(PointF translation)
    {
        nativeSetCaptionTranslation(m_internalObject, translation);
    }

/*!
     \brief 获取字幕的平移量
     \return 返回PointF对象，表示获得的字幕平移量
     \sa setCaptionTranslation
 */
    public PointF getCaptionTranslation()
    {
        return nativeGetCaptionTranslation(m_internalObject);
    }

/*!
     \brief 获取字幕文本矩形框
     \return 返回RectF对象，表示获得的字幕文本矩形框
 */
    public RectF getTextBoundingRect()
    {
        return nativeGetTextBoundingRect(m_internalObject);
    }

    private native long nativeGetInPoint(long internalObject);
    private native long nativeGetOutPoint(long internalObject);
    private native long nativeChangeInPoint(long internalObject, long newInPoint);
    private native long nativeChangeOutPoint(long internalObject, long newOutPoint);
    private native void nativeMovePosition(long internalObject, long offset);
    private native int nativeGetRoleInTheme(long internalObject);
    private native int nativeGetCategory(long internalObject);
    private native String nativeGetCaptionStylePackageId(long internalObject);
    private native boolean nativeApplyCaptionStyle(long internalObject, String captionStylePackageId);
    private native void nativeSetText(long internalObject, String text);
    private native String nativeGetText(long internalObject);
    private native void nativeSetBold(long internalObject, boolean bold);
    private native boolean nativeGetBold(long internalObject);
    private native void nativeSetItalic(long internalObject, boolean italic);
    private native boolean nativeGetItalic(long internalObject);
    private native void nativeSetTextColor(long internalObject, NvsColor textColor);
    private native NvsColor nativeGetTextColor(long internalObject);
    private native void nativeSetDrawOutline(long internalObject, boolean drawOutline);
    private native boolean nativeGetDrawOutline(long internalObject);
    private native void nativeSetOutlineColor(long internalObject, NvsColor outlineColor);
    private native NvsColor nativeGetOutlineColor(long internalObject);
    private native void nativeSetOutlineWidth(long internalObject, float outlineWidth);
    private native float nativeGetOutlineWidth(long internalObject);
    private native void nativeSetDrawShadow(long internalObject, boolean drawShadow);
    private native boolean nativeGetDrawShadow(long internalObject);
    private native void nativeSetShadowColor(long internalObject, NvsColor shadowColor);
    private native NvsColor nativeGetShadowColor(long internalObject);
    private native void nativeSetShadowOffset(long internalObject, PointF shadowOffset);
    private native PointF nativeGetShadowOffset(long internalObject);
    private native void nativeSetFontSize(long internalObject, float fontSize);
    private native float nativeGetFontSize(long internalObject);
    private native void nativeSetFontByFilePath(long internalObject, String filePath);
    private native String nativeGetFontFilePath(long internalObject);
    private native void nativeSetCaptionTranslation(long internalObject, PointF translation);
    private native PointF nativeGetCaptionTranslation(long internalObject);
    private native RectF nativeGetTextBoundingRect(long internalObject);
}
