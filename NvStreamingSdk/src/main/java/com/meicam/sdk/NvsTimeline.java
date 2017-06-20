//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 2. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import com.meicam.sdk.NvsObject;
import java.util.List;

/*!
     \brief 时间线，编辑场景的时间轴实体

     时间线由轨道组成，可视作一系列音视频轨道的集合。在时间线上可添加或者移除多条视频轨道和音轨轨道，多条轨道之间是相互叠加合成的关系。
     当编辑视频时，根据需要还会添加上时间线字幕，主题以及相应的动画贴纸，以制作出美观的视频。

     注：时间线上时间单位都为微秒。
 */
public class NvsTimeline extends NvsObject
{    
/*!
     \brief 获取视频解析度(图像宽高及像素比)
     \return 返回获取的视频解析度对象
     \sa getAudioRes
*/
    public NvsVideoResolution getVideoRes() {
       return nativeGetVideoRes(m_internalObject);
    }

/*!
     \brief 获取音频解析度(采样率、采样格式及声道数)
     \return 返回获取的音频解析度
     \sa getVideoRes
*/
    public NvsAudioResolution getAudioRes() {
        return nativeGetAudioRes(m_internalObject);
    }
/*!
     \brief 获取视频帧率
     \return 返回获取的视频帧率
*/
    public NvsRational getVideoFps()
    {
       return nativeGetVideoFps(m_internalObject);
    }
/*!
     \brief 获取时间线的时长(单位为微秒)
     \return 返回获取的时间线的时长
*/
    public long getDuration()
    {
        return nativeGetDuration(m_internalObject);
    }

/*!
    \brief 追加视频轨道
    \return 返回追加的视频轨道对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeVideoTrack
    \sa videoTrackCount
    \sa getVideoTrackByIndex
 */
    public NvsVideoTrack appendVideoTrack()
    {
        return nativeAppendVideoTrack(m_internalObject);
    }

/*!
    \brief 追加音频轨道
    \return 返回追加的音频轨道对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa audioTrackCount
    \sa removeAudioTrack
    \sa getAudioTrackByIndex
 */
    public NvsAudioTrack appendAudioTrack()
    {
       return nativeAppendAudioTrack(m_internalObject);
    }

/*!
    \brief 移除视频轨道
    \param trackIndex 视频轨道索引
    \return 判断是否移除成功。true表示移除成功，false则移除不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa appendVideoTrack
    \sa videoTrackCount
    \sa getVideoTrackByIndex
 */
    public boolean removeVideoTrack(int trackIndex)
    {
        return nativeRemoveVideoTrack(m_internalObject, trackIndex);
    }

/*!
    \brief 移除音频轨道
    \param trackIndex 音频轨道索引
    \return  判断是否移除成功。true表示移除成功，false则移除不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa appendAudioTrack
    \sa audioTrackCount
    \sa getAudioTrackByIndex
 */
    public boolean removeAudioTrack(int trackIndex)
    {
        return nativeRemoveAudioTrack(m_internalObject, trackIndex);
    }

/*!
    \brief 获取视频轨道数量
    \return 返回视频轨道的数量
    \sa appendVideoTrack
    \sa removeVideoTrack
 */
    public int videoTrackCount()
    {
        return nativeVideoTrackCount(m_internalObject);
    }

/*!
    \brief 获取音频轨道数量
    \return 返回音频轨道的数量
    \sa appendAudioTrack
    \sa removeAudioTrack
 */
    public int audioTrackCount()
    {
        return nativeAudioTrackCount(m_internalObject);
    }

/*!
    \brief 通过索引获取视频轨道
    \param trackIndex 视频轨道索引
    \return 返回获取的视频轨道对象
    \sa appendVideoTrack
    \sa videoTrackCount
    \sa removeVideoTrack
 */
    public NvsVideoTrack getVideoTrackByIndex(int trackIndex)
    {
        return nativeGetVideoTrackByIndex(m_internalObject, trackIndex);
    }

/*!
    \brief 通过索引获取音频轨道
    \param trackIndex 音频轨道索引
    \return 返回获取的音频轨道对象
    \sa appendAudioTrack
    \sa audioTrackCount
    \sa removeAudioTrack
 */
    public NvsAudioTrack getAudioTrackByIndex(int trackIndex)
    {
        return nativeGetAudioTrackByIndex(m_internalObject, trackIndex);
    }

/*!
    \brief 获得时间线上的第一个字幕
    \return 返回获取的时间线字幕对象
    \sa getLastCaption
    \sa addCaption
 */
    public NvsTimelineCaption getFirstCaption()
    {
        return natvieGetFirstCaption(m_internalObject);
    }

/*!
    \brief 获得时间线上的最后一个字幕
    \return 返回获取的时间线字幕对象
    \sa getFirstCaption
    \sa addCaption
    \sa
 */
    public NvsTimelineCaption getLastCaption()
    {
        return natvieGetLastCaption(m_internalObject);
    }

/*!
    \brief 获得时间线上的当前字幕的前一个字幕
    \param caption 时间线上当前字幕对象
    \return 返回获取的时间线字幕对象
    \sa getNextCaption
 */
    public NvsTimelineCaption getPrevCaption(NvsTimelineCaption caption)
    {
        return nativeGetPrevCaption(m_internalObject, caption);
    }

/*!
    \brief 获得时间线上的当前字幕的后一个字幕
    \param caption 时间线上当前字幕对象
    \return 返回获取的时间线字幕对象
    \sa getPrevCaption
 */
    public NvsTimelineCaption getNextCaption(NvsTimelineCaption caption)
    {
        return nativeGetNextCaption(m_internalObject, caption);
    }

/*!
    \brief 根据时间线上的位置获得字幕
    \param timelinePos 时间线上的位置(微秒)
    \return 返回保存当前位置字幕的List集合
    <br>获取的字幕列表排序规则如下：
    <br>1.添加时字幕入点不同，按入点的先后顺序排列；
    <br>2.添加时字幕入点相同，按添加字幕的先后顺序排列。
    \sa addCaption
 */
    public List<NvsTimelineCaption> getCaptionsByTimelinePosition(long timelinePos)
    {
        return nativeGetCaptionsByTimelinePosition(m_internalObject, timelinePos);
    }

/*!
    \brief 在时间线上添加字幕
    \param captionText 添加的字幕
    \param inPoint 字幕在时间线上的起点
    \param duration 字幕显示时长(微秒)
    \param captionStylePackageId 字幕样式资源包ID
    \return 返回时间线字幕对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeCaption
 */
    public NvsTimelineCaption addCaption(String captionText, long inPoint, long duration, String captionStylePackageId)
    {
        return nativeAddCaption(m_internalObject, captionText, inPoint, duration, captionStylePackageId);
    }

/*!
    \brief 移除时间上的字幕
    \param caption 要移除的时间线字幕对象
    \return 返回下一个时间线字幕对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa addCaption
 */
    public NvsTimelineCaption removeCaption(NvsTimelineCaption caption)
    {
        return nativeRemoveCaption(m_internalObject, caption);
    }

/*!
    \brief 获取时间线上第一个动画贴纸
    \return 返回获取的时间线动画贴纸对象
    \sa getLastAnimatedSticker
    \sa addAnimatedSticker
 */
    public NvsTimelineAnimatedSticker getFirstAnimatedSticker()
    {
        return nativeGetFirstAnimatedSticker(m_internalObject);
    }

/*!
    \brief 获取时间线上最后一个动画贴纸
    \return 返回获取的时间线动画贴纸对象
    \sa getFirstAnimatedSticker
 */
    public NvsTimelineAnimatedSticker getLastAnimatedSticker()
    {
        return nativeGetLastAnimatedSticker(m_internalObject);
    }

/*!
    \brief 获取时间线当前动画贴纸的前一个动画贴纸
    \param animatedSticker 时间线动画贴纸对象
    \return 返回获取的时间线动画贴纸对象
    \sa getNextAnimatedSticker
 */
    public NvsTimelineAnimatedSticker getPrevAnimatedSticker(NvsTimelineAnimatedSticker animatedSticker)
    {
        return nativeGetPrevAnimatedSticker(m_internalObject, animatedSticker);
    }

/*!
    \brief 获取时间线当前动画贴纸的后一个动画贴纸
    \param animatedSticker 时间线动画贴纸对象
    \return 返回获取的时间线动画贴纸对象
    \sa getPrevAnimatedSticker
 */
    public NvsTimelineAnimatedSticker getNextAnimatedSticker(NvsTimelineAnimatedSticker animatedSticker)
    {
         return nativeGetNextAnimatedSticker(m_internalObject, animatedSticker);
    }

/*!
    \brief 根据时间线上的位置获得动画贴纸
    \param timelinePos 时间线上的位置(微秒)
    \return 返回保存当前位置动画贴纸对象的List集合
    <br>获取的动画贴纸列表排序规则如下：
    <br>1.添加时入点不同，按入点的先后顺序排列；
    <br>2.添加时入点相同，按添加动画贴纸的先后顺序排列。
    \sa addAnimatedSticker
 */
    public List<NvsTimelineAnimatedSticker> getAnimatedStickersByTimelinePosition(long timelinePos)
    {
        return nativeGetAnimatedStickersByTimelinePosition(m_internalObject, timelinePos);
    }

/*!
    \brief 在时间线上添加动画贴纸
    \param inPoint 动画贴纸在时间线上的起点
    \param duration 动画贴纸的显示时长(微秒)
    \param animatedStickerPackageId 动画贴纸资源包ID
    \return 返回时间线动画贴纸对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa removeAnimatedSticker
 */
    public NvsTimelineAnimatedSticker addAnimatedSticker(long inPoint, long duration, String animatedStickerPackageId)
    {
        return nativeAddAnimatedSticker(m_internalObject, inPoint, duration, animatedStickerPackageId);
    }

/*!
    \brief 移除时间上的动画贴纸
    \param animatedSticker 要移除的动画贴纸对象
    \return 返回下一个时间线动画贴纸对象
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa addAnimatedSticker
 */
    public NvsTimelineAnimatedSticker removeAnimatedSticker(NvsTimelineAnimatedSticker animatedSticker)
    {
        return nativeRemoveAnimatedSticker(m_internalObject, animatedSticker);
    }

/*!
    \brief 获得当前主题的id
    \return 当前主题id，若无主题返回null
    \sa applyTheme
    \sa removeCurrentTheme
 */
    public String getCurrentThemeId()
    {
        return nativeGetCurrentThemeId(m_internalObject);
    }

/*!
    \brief 对当前时间线应用主题(主题是相对于整个时间线而言)

    对于主题，可能包含有片头或片尾，或片头片尾都有，也有可能都没有。应用含有片头或片尾的主题时，片头和片尾都被视作一个clip，从而导致clip数量的增加。使用此接口时请注意判断片段的数量。
    \param themeId 主题资源包的id
    \return 判断是否应用主题成功，true表示成功；false表示不成功
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa getCurrentThemeId
    \sa removeCurrentTheme
 */
    public boolean applyTheme(String themeId)
    {
        return nativeApplyTheme(m_internalObject, themeId);
    }

/*!
    \brief 移除当前主题
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa applyTheme
    \sa getCurrentThemeId
 */
    public void removeCurrentTheme()
    {
        nativeRemoveCurrentTheme(m_internalObject);
    }

/*!
    \brief 设置主题片头的字幕文本
    \param text 字幕文本
    \sa applyTheme
    \sa setThemeTrailerCaptionText
 */
    public void setThemeTitleCaptionText(String text)
    {
        nativeSetThemeTitleCaptionText(m_internalObject, text);
    }

/*!
    \brief 设置主题片尾的字幕文本
    \param text 字幕文本
    \sa setThemeTitleCaptionText
    \sa applyTheme
 */
    public void setThemeTrailerCaptionText(String text)
    {
        nativeSetThemeTrailerCaptionText(m_internalObject, text);
    }

/*!
    \brief 设置主题音乐音量
    \param leftVolumeGain 音量的左声道
    \param rightVolumeGain 音量的右声道
    \sa getThemeMusicVolumeGain
 */
    public void setThemeMusicVolumeGain(float leftVolumeGain, float rightVolumeGain)
    {
        nativeSetThemeMusicVolumeGain(m_internalObject, leftVolumeGain, rightVolumeGain);
        return;
    }

/*!
    \brief 获取主题音乐音量
    \return 返回获取的音量对象
    \sa setThemeMusicVolumeGain
 */
    public NvsVolume getThemeMusicVolumeGain()
    {
        return nativeGetThemeMusicVolumeGain(m_internalObject);
    }


    private native NvsVideoResolution nativeGetVideoRes(long internalObject);
    private native NvsAudioResolution nativeGetAudioRes(long internalObject);
    private native NvsRational nativeGetVideoFps(long internalObject);
    private native long nativeGetDuration(long internalObject);
    private native NvsVideoTrack nativeAppendVideoTrack(long internalObject);
    private native NvsAudioTrack nativeAppendAudioTrack(long internalObject);
    private native boolean nativeRemoveVideoTrack(long internalObject, int trackIndex);
    private native boolean nativeRemoveAudioTrack(long internalObject, int trackIndex);
    private native int nativeVideoTrackCount(long internalObject);
    private native int nativeAudioTrackCount(long internalObject);
    private native NvsVideoTrack nativeGetVideoTrackByIndex(long internalObject, int trackIndex);
    private native NvsAudioTrack nativeGetAudioTrackByIndex(long internalObject, int trackIndex);
    private native NvsTimelineCaption natvieGetFirstCaption(long internalObject);
    private native NvsTimelineCaption natvieGetLastCaption(long internalObject);
    private native NvsTimelineCaption nativeGetPrevCaption(long internalObject, NvsTimelineCaption caption);
    private native NvsTimelineCaption nativeGetNextCaption(long internalObject, NvsTimelineCaption caption);
    private native List<NvsTimelineCaption> nativeGetCaptionsByTimelinePosition(long internalObject, long timelinePos);
    private native NvsTimelineCaption nativeAddCaption(long internalObject, String captionText, long inPoint, long duration, String captionStylePackageId);
    private native NvsTimelineCaption nativeRemoveCaption(long internalObject, NvsTimelineCaption caption);
    private native NvsTimelineAnimatedSticker nativeGetFirstAnimatedSticker(long internalObject);
    private native NvsTimelineAnimatedSticker nativeGetLastAnimatedSticker(long internalObject);
    private native NvsTimelineAnimatedSticker nativeGetPrevAnimatedSticker(long internalObject, NvsTimelineAnimatedSticker animatedSticker);
    private native NvsTimelineAnimatedSticker nativeGetNextAnimatedSticker(long internalObject, NvsTimelineAnimatedSticker animatedSticker);
    private native List<NvsTimelineAnimatedSticker> nativeGetAnimatedStickersByTimelinePosition(long internalObject, long timelinePos);
    private native NvsTimelineAnimatedSticker nativeAddAnimatedSticker(long internalObject, long inPoint, long duration, String animatedStickerPackageId);
    private native NvsTimelineAnimatedSticker nativeRemoveAnimatedSticker(long internalObject, NvsTimelineAnimatedSticker animatedSticker);
    private native String nativeGetCurrentThemeId(long internalObject);
    private native boolean nativeApplyTheme(long internalObject, String themeId);
    private native void nativeRemoveCurrentTheme(long internalObject);
    private native void nativeSetThemeTitleCaptionText(long internalObject, String text);
    private native void nativeSetThemeTrailerCaptionText(long internalObject, String text);
    private native void nativeSetThemeMusicVolumeGain(long internalObject, float leftVolumeGain, float rightVolumeGain);
    private native NvsVolume nativeGetThemeMusicVolumeGain(long internalObject);
}

