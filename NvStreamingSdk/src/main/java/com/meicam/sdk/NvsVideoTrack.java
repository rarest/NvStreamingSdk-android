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
/*!
    \brief 视频轨道，视频片段的集合

    视频轨道是容纳视频片段的实体，可以添加、插入、删移多个视频片段。视频轨道随着片段的增加不断延展，而片段与片段之间可进行视频转场设置。

    注：视频片段的索引都是从0开始。
 */
public class NvsVideoTrack extends NvsTrack
{
/*!
     \brief 在视频轨道尾部追加视频片段
     \param filePath 视频片段路径。对于片段路径方式，请参见[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     \return 返回追加的视频片段对象

     例:

     ![] (@ref TrackClip.PNG)
     上图中视频轨道上有三个片段,分别为C1,C2,C3, 在视频轨道尾部追加一个新的片段C4

              NvsVideoTrack m_videoTrack;//m_videoTrack省略创建
              NvsVideoClip m_videoClip;
              m_videoClip = m_videoTrack.appendClip(filepath)  //filepath为片段C4的路径。

     追加后变为：
     ![] (@ref TrackAppendClip.PNG)
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa insertClip
*/
    public NvsVideoClip appendClip(String filePath)
    {
        return insertClip(filePath, getClipCount());
    }

/*!
    \brief 在视频轨道尾部追加指定长度的视频片段
    \param filePath 视频片段路径。对于片段路径方式，请参见[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
    \param trimIn 视频片段的裁剪入点。
    \param trimOut 视频片段的裁剪出点
    \return 返回追加的视频片段对象

    对于片段裁剪入点与出点的理解，具体请参见[裁剪入点与出点(trimIn and trimOut)] (\ref TrimInOut.md)专题。
    \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
    \sa insertClip
 */
    public NvsVideoClip appendClip(String filePath, long trimIn, long trimOut)
    {
        return insertClip(filePath, trimIn, trimOut, getClipCount());
    }

/*!
     \brief 在视频轨道上指定片段索引处插入视频片段
     \param filePath 视频片段路径。对于片段路径方式，请参见[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     \param clipIndex 插入后片段的索引
     \return 返回插入的视频片段对象

     调用insertClip()执行的是前插入即在clipIndex所表示的片段之前进行插入。示例如下:

     ![] (@ref TrackClip.PNG)
     上图中轨道上有三个视频片段C1、C2、C3，待插入的片段记为C4。如果在C1之前插入,则clipIndex值为0,其它以此类推。相应代码实现如下：

            NvsVideoTrack m_videoTrack;//m_videoTrack省略创建
            int clipCount = m_videoTrack.getClipCount();
            NvsVideoClip m_videoClip;
            m_videoClip = m_videoTrack.insertClip(filepath,0);  //filepath为片段C4的路径,clipIndex取值为[0,clipCount-1]

     插入后变为:
          ![] (@ref TrackInsertClip.PNG)
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa appendClip
 */
    public NvsVideoClip insertClip(String filePath, int clipIndex)
    {
        return nativeInsertClip(m_internalObject, filePath, clipIndex);
    }

/*!
     \brief 在视频轨道上指定片段索引处插入指定长度的视频片段
     \param filePath 视频片段路径。对于片段路径方式，请参见[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     \param clipIndex 插入片段索引
     \param trimIn 视频片段的裁剪入点
     \param trimOut 视频片段的裁剪出点
     \return 返回插入的视频片段对象

     ![] (@ref beforeInsertClip.PNG)
     上图中轨道上有三个视频片段C1、C2、C3，以及待插入的片段C4。C4片段上箭头所指处即为片段裁剪的的入点和出点，裁剪后将对应片段插入到轨道上。注意：裁剪入点和出点值必须在此片段的时长范围里，且裁剪入点值必须小于裁剪出点值。实现代码如下：

                         NvsVideoTrack m_videoTrack;//m_videoTrack省略创建
                         int clipCount = m_videoTrack.getClipCount();
                         NvsVideoClip m_videoClip;
                         m_videoClip = m_videoTrack.insertClip(filepath,trimIn,trimOut,0);//filepath为片段C4的路径,trimIn和trimOut为上图C4片段上箭头所指位置处的值，clipIndex取值为[0,clipCount-1]

     插入后结果:
     ![] (@ref TrackInsertClip.PNG)
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa appendClip
 */
    public NvsVideoClip insertClip(String filePath, long trimIn, long trimOut, int clipIndex)
    {
        return nativeInsertClip(m_internalObject, filePath, trimIn, trimOut, clipIndex);
    }

/*!
     \brief 添加片段
     \param filePath 片段路径

     对于Android里的片段路径，有两种方式：

     a. 在本地程序路径下创建一个assets文件夹，一般放在跟res文件夹同级的目录里。可将相应的音视频等资源放在里面，路径形式："assets:/video20170406101714.mp4"

     b. 通过绝对路径导入手机里的音视频等资源，路径形式："/storage/Media/DCIM/IMG_0646.MP4"
     \param inPoint 片段在时间线上的入点
     \return 返回添加的视频片段对象

     对于片段在时间线上的入点与出点的理解，具体请参见[裁剪入点与出点(trimIn and trimOut)] (\ref TrimInOut.md)专题。
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa appendClip
     \sa insertClip
 */
    public NvsVideoClip addClip(String filePath, long inPoint)
    {
        return nativeAddClip(m_internalObject, filePath, inPoint);
    }

/*!
     \brief 添加指定长度的片段
     \param filePath 片段路径。对于片段路径方式，请参见[addClip()] (@ref com.meicam.sdk.NvsVideoTrack.addClip)的参数filePath。
     \param inPoint 片段在时间线上的入点
     \param trimIn 片段裁剪入点
     \param trimOut 片段裁剪出点
     \return 返回添加的视频片段对象

     使用addClip()添加片段时，待添加的片段会在时间线上指定的入点分割当前片段，再从当前指定的入点起，在轨道上按待添加片段的长度删除对应长度的位置里面所包含的片段，然后添加需要添加的片段。如果在对应位置上有空隙，则直接覆盖。

     示例如下:

     ![] (@ref beforeAddClip.PNG)
     如上图，轨道上有三个视频片段C1、C2、C3，以及待添加的片段C4。C4片段上箭头所指处即为片段裁剪的的入点和出点，轨道上箭头所指即为片段在时间线上的入点，裁剪后将对应片段添加到轨道上。代码实现如下：

            NvsVideoTrack videoTrack; //videoTrack这里省略创建
            videoTrack.addClip(filepath,inPoint,trimIn,trimOut);//filepath为片段C4的路径,inPoint为图中轨道上箭头所指位置的值，trimIn和trimOut为上图C4片段上箭头所指位置处的值

     添加后结果如下：

     ![] (@ref afterAddClip.PNG)
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa appendClip
     \sa insertClip
 */
    public NvsVideoClip addClip(String filePath, long inPoint, long trimIn, long trimOut)
    {
        return nativeAddClip(m_internalObject, filePath, inPoint, trimIn, trimOut);
    }
/*!
     \brief 通过索引获取视频片段
     \param clipIndex 视频片段索引

     片段索引取值范围:

     NvsVideoTrack m_videoTrack;//m_videoTrack省略创建

     int clipCount = m_videoTrack.getClipCount();

     clipIndex的取值范围为[0,clipCount-1]

     \return 返回获取的视频片段对象
     \sa getClipByTimelinePosition
     \sa insertClip
     \sa appendClip
 */
    public NvsVideoClip getClipByIndex(int clipIndex)
    {
        return nativeGetClipByIndex(m_internalObject, clipIndex);
    }
/*!
     \brief 通过时间获取视频片段
     \param timelinePos 时间线上的位置(微秒)
     \return 返回获取的视频片段对象
     \sa getClipByIndex
 */
    public NvsVideoClip getClipByTimelinePosition(long timelinePos)
    {
        return nativeGetClipByTimelinePosition(m_internalObject, timelinePos);
    }
/*!
     \brief 设置内嵌式转场
     \param srcClipIndex 源视频片段索引
     \param transitionName 转场名称。获取视频转场名称，请参见[getAllBuiltinVideoTransitionNames()] (@ref com.meicam.sdk.NvsStreamingContext.getAllBuiltinVideoTransitionNames)或[内建特效名称列表] (@ref FxNameList.md)。注意：转场名称若设为空字符串，则删除原有转场。
     \return 返回设置的视频转场对象。若transitionName设为空字符串(即删除原有转场),返回null

     例:

     ![] (@ref Clip.PNG)

     上图中轨道上有四个视频片段,分别为C1,C2,C3,C4,其中片段C1、C2、C3相邻，而片段C3与C4之间存在空隙。若在C1和C2之间
     设置一个内嵌式转场后变为:
     ![] (@ref TrackTransition.PNG )

             NvsVideoTrack m_videoTrack;//m_videoTrack省略创建
             int clipCount = m_videoTrack.getClipCount();
             NvsVideoTransition m_videoTransition;
             m_videoTransition = m_videoTrack.setBuiltinTransition(0,transitionName) //srcClipIndex取值范围为(1,clipCount-1),transitionName为要设置的转场的名称

     若在C2和C3之间添加转场，则索引为1。转场只能设置在两个相邻片段之间，上图中的C3和C4片段之间存在空隙，不能在两片段之间添加转场。
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa setPackagedTransition
 */
    public NvsVideoTransition setBuiltinTransition(int srcClipIndex, String transitionName)
    {
        return nativeSetBuiltinTransition(m_internalObject, srcClipIndex, transitionName);
    }
/*!
     \brief 设置资源包转场
     \param srcClipIndex 源视频片段索引
     \param packageId 转场资源包ID

     若packageId为空，则删除该视频片段处的转场；若packageId设为"theme",则该视频片段处的转场设为当前主题的转场，
     若无主题，则删除该视频片段处的转场
     \return 返回设置的视频转场对象
     \warning 此接口会引发流媒体引擎状态跳转到引擎停止状态，具体情况请参见[引擎变化专题] (\ref EngineChange.md)。
     \sa setBuiltinTransition
 */
    public NvsVideoTransition setPackagedTransition(int srcClipIndex, String packageId)
    {
        return nativeSetPackagedTransition(m_internalObject, srcClipIndex, packageId);
    }

/*!
     \brief 通过源视频片段索引获取转场
     \param srcClipIndex 源视频片段索引
     \return 返回获取的视频转场对象
     \sa setBuiltinTransition
 */
    public NvsVideoTransition getTransitionBySourceClipIndex(int srcClipIndex)
    {
        return nativeGetTransitionBySourceClipIndex(m_internalObject, srcClipIndex);
    }

    private native NvsVideoClip nativeInsertClip(long internalObject, String filePath, int clipIndex);
    private native NvsVideoClip nativeInsertClip(long internalObject, String filePath, long trimIn, long trimOut, int clipIndex);
    private native NvsVideoClip nativeAddClip(long internalObject, String filePath, long inPoint);
    private native NvsVideoClip nativeAddClip(long internalObject, String filePath, long inPoint, long trimIn, long trimOut);
    private native NvsVideoClip nativeGetClipByIndex(long internalObject, int clipIndex);
    private native NvsVideoClip nativeGetClipByTimelinePosition(long internalObject, long timelinePos);
    private native NvsVideoTransition nativeSetBuiltinTransition(long internalObject, int srcClipIndex, String transitionName);
    private native NvsVideoTransition nativeSetPackagedTransition(long internalObject, int srcClipIndex, String packageId);
    private native NvsVideoTransition nativeGetTransitionBySourceClipIndex(long internalObject, int srcClipIndex);
}
